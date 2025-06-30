package core.hubby.backend.business.services;
import org.springframework.stereotype.Service;

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import core.hubby.backend.auth.util.CurrentUserUtil;
import core.hubby.backend.business.dto.param.CreateOrganizationDTO;
import core.hubby.backend.business.dto.vo.OrganizationVO;
import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.entities.OrganizationType;
import core.hubby.backend.business.entities.User;
import core.hubby.backend.business.entities.embedded.AddressDetails;
import core.hubby.backend.business.entities.embedded.ExternalLinks;
import core.hubby.backend.business.entities.embedded.FinancialSettings;
import core.hubby.backend.business.entities.embedded.OrganizationUserInvites;
import core.hubby.backend.business.entities.embedded.OrganizationUsers;
import core.hubby.backend.business.entities.embedded.TaxDetails;
import core.hubby.backend.business.enums.Roles;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.business.repositories.OrganizationTypeRepository;
import core.hubby.backend.business.repositories.UserRepository;
import core.hubby.backend.core.dto.ExternalLink;
import core.hubby.backend.core.dto.PhoneDetail;
import core.hubby.backend.core.helper.AddressHelper;
import core.hubby.backend.core.helper.ContactNumberHelper;
import core.hubby.backend.core.helper.CountriesApiHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service("OrganizationService")
@RequiredArgsConstructor
public class OrganizationService {
	private static final Set<String> externalLinks = new HashSet<>();
	
	private final OrganizationRepository organizationRepository;
	private final UserRepository userRepository;
	private final OrganizationTypeRepository organizationTypeRepository;
	private final ContactNumberHelper contactNumberHelper;
	private final AddressHelper addressHelper;
	private final CountriesApiHelper countriesApiHelper;
	
	// Current authenticated user
	private final User authenticatedUser = (User) CurrentUserUtil.getCurrentUserDetails();
	
	static {
		// Initialize external links
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_FACEBOOK);
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_TIKTOK);
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_INSTAGRAM);
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_WEBSITE);
	}
	
	@Transactional
	public OrganizationVO createOrganization(CreateOrganizationDTO data) {
		
		// Validate Phone Number
		Set<PhoneDetail> phoneSet = contactNumberHelper.parsePhoneNumbers(data.phoneNo());
		
		// Add current authenticated user as the organization's subscriber
		OrganizationUsers subscriber = OrganizationUsers.builder()
				.userId(authenticatedUser)
				.userRole(Roles.SUBSCRIBER.toString())
				.build();
		// Create organization user
		Set<OrganizationUsers> organizationUsers = Set.of(subscriber);
		
		// Check if there are invites
		Set<OrganizationUserInvites> userInvites = new HashSet<>();
		if (!data.inviteOtherUser().isEmpty()) {
			
			Set<UUID> userId = data.inviteOtherUser().stream().map(id -> id.userId())
					.collect(Collectors.toSet());
			
			if (userRepository.checkIfUsersExists(userId)) {
				for(CreateOrganizationDTO.InviteUser user: data.inviteOtherUser()) {
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
				.phoneNo(Map.of("phones", phoneSet))
				.email(data.email())
				.address(Map.of("address", transformedAddress))
				.taxDetails(createTaxDetails(data.taxDetails()))
				.financialSettings(createFinancialSetting(data.financialSettings()))
				.externalLinks(
						data.externalLinks().stream()
						.filter(lkFilter ->
						externalLinks.contains(lkFilter.getLinkType().toLowerCase())
						).collect(Collectors.toSet())
				)
				.build();
		
		Organization org = organizationRepository.save(newOrganization);
		return getOrganization(org);
		
	}
	
	public TaxDetails createTaxDetails(CreateOrganizationDTO.AddTaxDetails tax) {
		TaxDetails taxDetails = new TaxDetails();
		taxDetails.setTaxIdNo(tax.taxIdNo());
		taxDetails.setTaxBasis(tax.taxBasis()!= null ? tax.taxBasis() : "");
		taxDetails.setTaxPeriod(tax.taxPeriod() != null ? tax.taxPeriod() : "");
		return taxDetails;
	}
	
	public Map<String, String> generateTaxDetails(TaxDetails details) {
		Map<String, String> taxDetails = new HashMap<>();
		taxDetails.put("taxtIdNumber", details.getTaxIdNo());
		taxDetails.put("taxBasis", details.getTaxBasis());
		taxDetails.put("taxPeriod", details.getTaxPeriod());
		
		return taxDetails;
	}
	
	/* INFO: Get industry type */
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
			
			organizationDetails = new OrganizationVO.Details(
					transformOrganizationUsers(findOrganization.getOrganizationUsers()),
					findOrganization.getName(),
					findOrganization.getLegalName(),
					findOrganization.getCountry(),
					findOrganization.getOrganizationType().getName(),
					findOrganization.getPhoneNo().get("phones"),
					findOrganization.getEmail(),
					findOrganization.getWebsite(),
					generateTaxDetails(findOrganization.getTaxDetails())
			);
		} else if (org instanceof Organization organizationObject) {
			TaxDetails taxDetails = organizationObject.getTaxDetails();
			Map<String, String> getTaxDetails = generateTaxDetails(taxDetails);
			
			// Get organization users
			Set<OrganizationVO.OrganizationUsers> organizationUsers = transformOrganizationUsers(
					organizationObject.getOrganizationUsers()
					);
			
			organizationDetails = new OrganizationVO.Details(
					organizationUsers,
					organizationObject.getName(),
					organizationObject.getLegalName(),
					organizationObject.getCountry(),
					organizationObject.getOrganizationType().getName(),
					organizationObject.getPhoneNo().get("phones"),
					organizationObject.getEmail(),
					organizationObject.getWebsite(),
					getTaxDetails
			);
		}
		
		List<OrganizationVO.Details> details = List.of(organizationDetails);
		List<Map<String, String>> orgTypes = getOrganizationTypes();
		
		return new OrganizationVO(details, orgTypes);
	}
	
	// Transform organization users to OrganizationVO.OrganizationUsers object type
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
	
	public FinancialSettings createFinancialSetting(CreateOrganizationDTO.AddFinancialSetting financial) {
		/* GET CURRENCY TYPE */
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
}
