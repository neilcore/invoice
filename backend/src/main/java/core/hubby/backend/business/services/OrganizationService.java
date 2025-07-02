package core.hubby.backend.business.services;
import org.springframework.stereotype.Service;

import core.hubby.backend.auth.util.CurrentUserUtil;
import core.hubby.backend.business.dto.param.OrganizationDetailsDTO;
import core.hubby.backend.business.dto.param.UpdateUserOrganizationInvitation;
import core.hubby.backend.business.dto.vo.OrganizationVO;
import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.entities.OrganizationNameUpdate;
import core.hubby.backend.business.entities.OrganizationType;
import core.hubby.backend.business.entities.User;
import core.hubby.backend.business.entities.embedded.AddressDetails;
import core.hubby.backend.business.entities.embedded.BillAndSalesPaymentTermElement;
import core.hubby.backend.business.entities.embedded.FinancialSettings;
import core.hubby.backend.business.entities.embedded.OrganizationUserInvites;
import core.hubby.backend.business.entities.embedded.OrganizationUsers;
import core.hubby.backend.business.entities.embedded.TaxDetails;
import core.hubby.backend.business.enums.Roles;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.business.repositories.OrganizationTypeRepository;
import core.hubby.backend.business.repositories.PaymentTermsRepository;
import core.hubby.backend.business.repositories.UserRepository;
import core.hubby.backend.core.dto.PhoneDetail;
import core.hubby.backend.core.helper.AddressHelper;
import core.hubby.backend.core.helper.ContactNumberHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service("OrganizationService")
@RequiredArgsConstructor
public class OrganizationService {
	private static final Set<String> externalLinks = new HashSet<>();
	private static final Set<String> paymentTermsElements = new HashSet<>();
	
	private final OrganizationRepository organizationRepository;
	private final PaymentTermsRepository paymentTermsRepository;
	private final UserRepository userRepository;
	private final OrganizationTypeRepository organizationTypeRepository;
	private final ContactNumberHelper contactNumberHelper;
	private final AddressHelper addressHelper;
	
	/**
	 * This will hold the current authenticated user
	 */
	private final User authenticatedUser = (User) CurrentUserUtil.getCurrentUserDetails();
	
	static {
		// Add values to external links
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_FACEBOOK);
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_TIKTOK);
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_INSTAGRAM);
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_WEBSITE);
		
		// Add values to paymentTermsElements
		paymentTermsElements.add(OrganizationRepository.PAYMENT_TERMS_BILLS);
		paymentTermsElements.add(OrganizationRepository.PAYMENT_TERMS_SALES);
	}
	
	@Transactional
	public OrganizationVO createOrganization(OrganizationDetailsDTO data) {
		
		// Add current authenticated user as the organization's subscriber
		OrganizationUsers subscriber = OrganizationUsers.builder()
				.userId(authenticatedUser)
				.userRole(Roles.SUBSCRIBER.toString())
				.build();
		// Create organization user
		Set<OrganizationUsers> organizationUsers = Set.of(subscriber);
		
		// Validate payment terms keys
		Map<String, BillAndSalesPaymentTermElement> paymentTerms = validatePaymentTerms(data.paymentTerms());
		
		// Check if there are invites
		Set<OrganizationUserInvites> userInvites = new HashSet<>();
		if (!data.inviteOtherUser().isEmpty()) {
			
			Set<UUID> userId = data.inviteOtherUser().stream().map(id -> id.userId())
					.collect(Collectors.toSet());
			
			if (userRepository.checkIfUsersExists(userId)) {
				for(OrganizationDetailsDTO.InviteUser user: data.inviteOtherUser()) {
					Optional<User> getUser = userRepository.findById(user.userId());
					String userRole = user.role().toUpperCase() == Roles.READ_ONLY.toString() ?
							Roles.READ_ONLY.toString() : user.role().toUpperCase() == Roles.STANDARD.toString()
							? Roles.STANDARD.toString() : Roles.INVOICE_ONLY.toString();
					
					userInvites.add(
							OrganizationUserInvites.builder()
							.invitationBy(authenticatedUser)
							.invitationFor(getUser.get())
							.invitationRole(userRole)
							.build()
					);
				}
			}
		}
		
		// Transform organization address data to fit the internal models
		Set<Map<String, String>> transformedAddress = addressHelper
				.transformOrganizationAddressData(data.address());
		
		Organization newOrganization = Organization.builder()
				.organizationUsers(organizationUsers)
				.organizationUserInvites(userInvites)
				.name(data.name())
				.legalName(data.legalName())
				.tradingName(data.tradingName())
				.country(data.countryCode())
				.organizationType(getOrganizationType(data.organizationType()))
				.phoneNo(Map.of("phones", contactNumberHelper.parsePhoneNumbers(data.phoneNo())))
				.email(data.email())
				.address(Map.of("address", transformedAddress.toString()))
				.taxDetails(data.taxDetails())
				.financialSettings(createFinancialSetting(data.financialSettings()))
				.externalLinks(
						data.externalLinks().stream()
						.filter(lkFilter ->
						externalLinks.contains(lkFilter.getLinkType().toLowerCase())
						).collect(Collectors.toSet())
				)
				.paymentTerms(Map.of("paymentTerms", Set.of(paymentTerms).toString()))
				.build();
		
		Organization org = organizationRepository.save(newOrganization);
		return getOrganization(org);
		
	}
	
	/**
	 * This method will validated payment terms data.
	 * This will check every given entry key if it is a valid
	 * payment terms key by checking if the key exists in paymentTermsElements set.
	 * @param data - of type Map<String, BillAndSalesPaymentTermElement>
	 * @return - Map<String, BillAndSalesPaymentTermElement> object type
	 */
	private Map<String, BillAndSalesPaymentTermElement> validatePaymentTerms(
			Map<String, BillAndSalesPaymentTermElement> data
	) {
		List<String> paymentTermLists = paymentTermsRepository.findAll().stream()
				.map(trms -> trms.getName())
				.toList();
		
		/**
		 * This is check if the map keys follow the proper
		 * naming conventions.
		 * @throw IllegalArgumentException - if found
		 * any violations.
		 */
		data.keySet().forEach(
				ky -> {
					if (!ky.matches("^[A-Z]+$")) {
						throw new IllegalArgumentException("Unknown key name: " + ky);
					}
				}
		);
		
		/**
		 * This will retain payment terms values that are only
		 * contained in the specified collections (paymentTermLists).
		 * this will remove elements that are not contained in the 
		 * specified collections
		 */
		List<String> paymentTermsValues = data.values().stream()
				.map(pymt -> pymt.getPaymentTerm())
				.toList();
		
		Map<String, BillAndSalesPaymentTermElement> paymentTerms = new HashMap<>();
		if (!paymentTermLists.containsAll(paymentTermsValues)) {
			
			throw new IllegalArgumentException("Unknown payment term values.");
			
		} else {
			
			for (Map.Entry<String, BillAndSalesPaymentTermElement> entry: data.entrySet()) {
				paymentTerms.put(entry.getKey().toUpperCase(), entry.getValue());
			}
		}
		
		return paymentTerms;
	}
	
	public OrganizationVO.TaxDetails generateTaxDetails(TaxDetails details) {
		return new OrganizationVO.TaxDetails(details.getTaxIdNo(), details.getTaxBasis(), details.getTaxPeriod());
	}
	
	/**
	 * This method will retrieve the industry type
	 * @param type - the id of type java.util.UUID
	 * @return OrganizationType object
	 */
	private OrganizationType getOrganizationType(UUID type) {
		Optional<OrganizationType> getOrganizationType = organizationTypeRepository.findById(type);
		
		if (getOrganizationType.isEmpty()) {
			throw new IllegalArgumentException("Organization type not found");
		}
		return getOrganizationType.get();
	}
	
	public OrganizationVO getOrganization(Object org) {
		OrganizationVO.Details organizationDetails = null;
		
		if (org instanceof UUID organizationId) {
			Organization findOrganization = organizationRepository.findOrganizationById(organizationId)
					.orElseThrow(() -> new IllegalArgumentException("Organization entity cannot be found."));
			
			// Map Json phone details in string format to Set<PhoneDetail> format
			Set<PhoneDetail> phoneDetails = contactNumberHelper.mapPhoneJsonStringToPhoneDetailObject(
					findOrganization.getPhoneNo()
					);
			
			organizationDetails = new OrganizationVO.Details(
					transformOrganizationUsers(findOrganization.getOrganizationUsers()),
					findOrganization.getName(),
					findOrganization.getLegalName(),
					findOrganization.getCountry(),
					findOrganization.getOrganizationType().getName(),
					phoneDetails,
					findOrganization.getEmail(),
					findOrganization.getWebsite(),
					generateTaxDetails(findOrganization.getTaxDetails())
			);
		} else if (org instanceof Organization organizationObject) {			
			// Get organization users
			Set<OrganizationVO.OrganizationUsers> organizationUsers = transformOrganizationUsers(
					organizationObject.getOrganizationUsers()
					);
			
			// Map Json phone details in string format to Set<PhoneDetail> format
			Set<PhoneDetail> phoneDetails = contactNumberHelper.mapPhoneJsonStringToPhoneDetailObject(
					organizationObject.getPhoneNo()
					);
			
			organizationDetails = new OrganizationVO.Details(
					organizationUsers,
					organizationObject.getName(),
					organizationObject.getLegalName(),
					organizationObject.getCountry(),
					organizationObject.getOrganizationType().getName(),
					phoneDetails,
					organizationObject.getEmail(),
					organizationObject.getWebsite(),
					generateTaxDetails(organizationObject.getTaxDetails())
			);
		}
		
		List<OrganizationVO.Details> details = List.of(organizationDetails);
		List<Map<String, String>> orgTypes = getOrganizationTypes();
		
		return new OrganizationVO(details, orgTypes);
	}
	
	/**
	 * This method will transform organization users to
	 * OrganizationVO.OrganizationUsers object type.
	 * @param orgUsers: type Set<OrganizationUsers>
	 * @return Set<OrganizationVO.OrganizationUsers> object
	 */
	private Set<OrganizationVO.OrganizationUsers> transformOrganizationUsers(Set<OrganizationUsers> orgUsers) {
		Set<OrganizationVO.OrganizationUsers> organizationUsers = new HashSet<>();
		
		for(OrganizationUsers orgUser: orgUsers) {
			organizationUsers.add(
					new OrganizationVO.OrganizationUsers(
							new OrganizationVO.OrganizationUsers.User(
									orgUser.getUserId().getId().toString(),
									orgUser.getUserId().getFirstName(),
									orgUser.getUserId().getLastName(),
									orgUser.getUserId().getEmail()
									),
							orgUser.getUserRole(),
							orgUser.getUserJoined()
							
					)
					);
		}
		
		return organizationUsers;
	}
	
	private List<Map<String, String>> getOrganizationTypes(){
		return organizationTypeRepository.findAll()
				.stream()
				.map(type -> {
					Map<String, String> types = new HashMap<>();
					types.put("id", type.getId().toString());
					types.put("key", type.getName());
					types.put("value", type.getName().contains("_") ? type.getName().replace("_", " ") : type.getName());
					return types;
				})
				.toList();
	}
	
	public AddressDetails createAddressDetails(Map<String, String> data) {
		AddressDetails addressDetails = new AddressDetails();
		addressDetails.setPhysicalAddressStreet((String) data.get("PhysicalAddressStreet"));
		addressDetails.setPhysicalAddressCity((String) data.get("PhysicalAddressCity"));
		addressDetails.setPhysicalAddressState((String) data.get("PhysicalAddressState"));
		addressDetails.setPhysicalAddressPostalCode((String) data.get("PhysicalAddressPostalCode"));
		addressDetails.setPostalAddressStreet((String) data.get("PostalAddressStreet"));
		addressDetails.setPostalAddressCity((String) data.get("PostalAddressCity"));
		addressDetails.setPostalAddressState((String) data.get("PostalAddressState"));
		addressDetails.setPostalAddressPostalCode((String) data.get("PostalAddressPostalCode"));
		return addressDetails;
	}
	
	public FinancialSettings createFinancialSetting(OrganizationDetailsDTO.AddFinancialSetting financial) {
		// Get currency type
		String currency = "";
		switch (financial.defaultCurrency()) {
		case OrganizationRepository.CURRENCY_BRAZIL -> currency = OrganizationRepository.CURRENCY_BRAZIL;
		case OrganizationRepository.CURRENCY_CHINA -> currency = OrganizationRepository.CURRENCY_CHINA;
		case OrganizationRepository.CURRENCY_EGYPT -> currency = OrganizationRepository.CURRENCY_EGYPT;
		case OrganizationRepository.CURRENCY_INDIA -> currency = OrganizationRepository.CURRENCY_INDIA;
		case OrganizationRepository.CURRENCY_MEXICO -> currency = OrganizationRepository.CURRENCY_MEXICO;
		case OrganizationRepository.CURRENCY_PHILIPPINES -> currency = OrganizationRepository.CURRENCY_PHILIPPINES;
		case OrganizationRepository.CURRENY_JAPAN -> currency = OrganizationRepository.CURRENY_JAPAN;
		case OrganizationRepository.CURRENCY_SOUTH_AFRICA -> currency = OrganizationRepository.CURRENCY_SOUTH_AFRICA;
		case OrganizationRepository.CURRENCY_SWITZERLAND -> currency = OrganizationRepository.CURRENCY_SWITZERLAND;
		default -> currency = "n/a";
		}
		
		FinancialSettings data = new FinancialSettings();
		data.setDefaultCurrency(currency);
		data.setTimeZone(financial.timeZone());
		
		return data;
	}
	
	/**
	 * This method is used to updated an organization entity
	 * organization names (name, legalName, tradingName) are only Updatable
	 * once every 20 days
	 * TODO work with updateOrganization service method
	 * @param organizationId
	 * @param updatedData
	 * @return
	 */
	public OrganizationVO updateOrganization(UUID organizationId, OrganizationDetailsDTO updatedData) {
		Organization getOrganization = organizationRepository.findById(organizationId)
				.orElseThrow(() -> new IllegalArgumentException("Organization object cannot be found."));
		OrganizationNameUpdate organizationNameState = getOrganization.getOrganizationNameUpdate();
		
		if (!Objects.equals(getOrganization.getName(), updatedData.name())) {
			if (organizationNameState.isOrganizationNameUpdatable()) {
				getOrganization.setName(updatedData.name());
				organizationNameState.setUpdatedDate(LocalDate.now());
			}
		}
		
		if (!Objects.equals(getOrganization.getLegalName(), updatedData.legalName())) {
			if (organizationNameState.isOrganizationNameUpdatable()) {
				getOrganization.setLegalName(updatedData.legalName());
				organizationNameState.setUpdatedDate(LocalDate.now());
			}
		}
		
		if (!Objects.equals(getOrganization.getTradingName(), updatedData.tradingName())) {
			if (organizationNameState.isOrganizationNameUpdatable()) {
				getOrganization.setTradingName(updatedData.tradingName());
				organizationNameState.setUpdatedDate(LocalDate.now());
			}
		}
		
		OrganizationType getOrganizationType = getOrganizationType(updatedData.organizationType());
		if (!Objects.equals(getOrganization.getOrganizationType(), getOrganizationType)) {
			getOrganization.setOrganizationType(getOrganizationType);
		}
		
		if (!Objects.equals(updatedData.countryCode(), getOrganization.getCountry())) {
			getOrganization.setCountry(updatedData.countryCode());
		}
		
		// TODO changing email needs verification feature
		if (!Objects.equals(getOrganization.getEmail(), updatedData.email())) {
			getOrganization.setEmail(updatedData.email());
		}
		
		Set<Map<String, String>> addressDetails = addressHelper.jsonAddressStringToSetObject(
				getOrganization.getAddress().get("address")
				);
				
		if (!Objects.equals(updatedData.address(), addressDetails)) {
			getOrganization.setAddress(
					Map.of("address", addressHelper
				.transformOrganizationAddressData(updatedData.address()).toString())
					);
		}
		
		if (!Objects.equals(updatedData.taxDetails(), getOrganization.getTaxDetails())) {
			getOrganization.setTaxDetails(updatedData.taxDetails());
		}
		return null;
	}
	
	/**
	 * This method will update the organization's user invitation
	 * @param organizationId
	 * @param updatedInvitationDetail
	 */
	public void updateUserOrganizationInvitation(UUID organizationId, UpdateUserOrganizationInvitation updatedInvitationDetail) {
		
	}
	
	// TODO retrieve organization elements
//	public OrganizationElementVO getOrganizationElements() {
//		Set<OrganizationElementVO.OrganizationTypes> organizationTypes = organizationTypeRepository
//				.findAll()
//				.stream()
//				.map(orgType -> new OrganizationElementVO.OrganizationTypes(orgType.getId().toString(), orgType.getName()))
//				.collect(Collectors.toSet());
//		
//		return new OrganizationElementVO(
//				Set.of(Roles.SUBSCRIBER.toString(), Roles.READ_ONLY.toString(), Roles.STANDARD.toString(), Roles.INVOICE_ONLY.toString()),
//				organizationTypes,
//				
//				);
//	}
}
