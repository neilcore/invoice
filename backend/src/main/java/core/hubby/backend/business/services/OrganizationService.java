package core.hubby.backend.business.services;
import org.springframework.stereotype.Service;
import core.hubby.backend.business.dto.param.OrganizationCreateRequest;
import core.hubby.backend.business.dto.vo.OrganizationDetailsResponse;
import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.entities.OrganizationType;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.business.repositories.OrganizationTypeRepository;
import core.hubby.backend.core.dto.PhoneDetail;
import core.hubby.backend.core.helper.AddressHelper;
import core.hubby.backend.core.helper.ContactNumberHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("OrganizationService")
@RequiredArgsConstructor
public class OrganizationService {
	private static final Set<String> externalLinks = new HashSet<>();
	private static final Set<String> paymentTermsElements = new HashSet<>();
	
	private final OrganizationRepository organizationRepository;
	private final OrganizationTypeRepository organizationTypeRepository;
	private final ContactNumberHelper contactNumberHelper;
	private final AddressHelper addressHelper;
	private final UserAccountService userAccountService;
	
	static {
		/**
		 * Add values to externalLinks
		 */
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_FACEBOOK);
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_TIKTOK);
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_INSTAGRAM);
		externalLinks.add(OrganizationRepository.EXTERNAL_LINK_WEBSITE);
		
		/**
		 * paymentTermsElements
		 */
		paymentTermsElements.add(OrganizationRepository.PAYMENT_TERMS_BILLS);
		paymentTermsElements.add(OrganizationRepository.PAYMENT_TERMS_SALES);
	}
	
	/**
	 * This method will retrieve an organization entity.
	 * If obj is null, return a new Organization object.
	 * If obj is instance of UUID, retrieve the organization object using it's ID.
	 * @param obj - accepts {@linkplain Object} type.
	 * @return - {@linkplain Organization} object type.
	 */
	private Organization getOrganizationObject(Object obj) {
		Optional<Organization> findOrganization = Optional.empty();
		
		if (obj == null) {
			findOrganization = Optional.of(new Organization());
		} else if (obj instanceof UUID id) {
			findOrganization = organizationRepository
					.findById(id);
			
			if (findOrganization.isEmpty()) {
				throw new NoSuchElementException("Organization entity cannot be found.");
			}
		}
		
		return findOrganization.get();
	}
	
	/**
	 * This will persist the organization object to database (the actual creation of the entity
	 * to the database)
	 * @param organization - accepts {@linkplain Organization} object type.
	 * @return - {@linkplain OrganizationDetailsResponse} object type.
	 */
	private OrganizationDetailsResponse save(Organization organization) {
		Organization newOrganization = organizationRepository.save(organization);
		return mapOrganizationDetails(newOrganization);
	}
	
	/**
	 * This method will create new organization object
	 * @param - {@linkplain OrganizationCreateRequest} object type of data
	 * @return - {@linkplain OrganizationDetailsResponse} object type of data
	 */
	@Transactional
	public OrganizationDetailsResponse createNewOrganizationObject(OrganizationCreateRequest data) {
		Organization org = getOrganizationObject(null); // New organization object
		
		// Set organization basic information
		setBasicInformation(data.basicInformation(), org);
		
		// Set organization contact details
		setContactDetails(data.contactDetails(), org);
		
		// Set the creator if the organization as the advisor.
		userAccountService.addAdvisor(org);
		
		// Set organization invited users
		userAccountService.addInvitedUsers(data.inviteOtherUser(), org);
		
		return save(org);
		
	}
	
	/**
	 * The following fields are part of organization's basic informations:
	 * - displayName
	 * - legalName,
	 * - organizationType
	 * - organizationDescription
	 * @param info - accepts {@linkplain OrganizationCreateRequest.BasicInformation} object type.
	 * @param organization - accepts {@linkplain Organization} object type.
	 */
	private void setBasicInformation(
			OrganizationCreateRequest.BasicInformation info,
			Organization organization
	) {
		OrganizationType type = getOrganizationType(info.organizationType());
		organization.setBasicInformation(
				info.displayName(),
				info.legalName(),
				type,
				info.organizationDescription()
		);
	}
	
	/**
	 * This method will set the organization's contact details.
	 * Contact details include:
	 * - countryCode
	 * - addresses
	 * - phoneDetails
	 * - email
	 * - website
	 * - externalLinks
	 * @param contacts - accepts {@linkplain OrganizationCreateRequest.ContactDetails} object type
	 * @param organization - accepts {@linkplain Organization} object type.
	 */
	private void setContactDetails(
			OrganizationCreateRequest.ContactDetails contacts,
			Organization organization
	) {
		// Transform organization address data to fit the internal models
		Set<Map<String, String>> transformedAddress = addressHelper
				.transformOrganizationAddressData(contacts.address());
		
		// Set organization phone details
		String getPhoneDetails = contactNumberHelper.parsePhoneNumbers(contacts.phoneNo());
		
		organization.setContactDetails(
				contacts.countryCode(),
				transformedAddress,
				getPhoneDetails,
				contacts.email(),
				contacts.website(),
				contacts.externalLinks().stream()
				.filter(lkFilter ->
				externalLinks.contains(lkFilter.getLinkType().toLowerCase())
				).collect(Collectors.toSet())
		);
	}
	
	/**
	 * This method will check existing organization entity by {@linkplain java.util.UUID} id
	 * @param id - accepts {@linkplain java.util.UUID} type of ID
	 * @return - returns {@linkplain OrganizationDetailsResponse} object type.
	 */
	public OrganizationDetailsResponse retrieveOrganizationById(UUID id) {
		Optional<Organization> getOrganization = organizationRepository
				.findById(id);
		
		if(getOrganization.isEmpty()) {
			throw new IllegalArgumentException("Organization entity not found.");
		}
		return mapOrganizationDetails(getOrganization.get());
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
	
	/**
	 * This method will map {@linkplain Organization} object
	 * to {@linkplain OrganizationDetailsResponse} object type.
	 * @param org - accepts {@linkplain Organization} object type.
	 * @return - returns {@linkplain OrganizationDetailsResponse} object type.
	 */
	public OrganizationDetailsResponse mapOrganizationDetails(Organization org) {
		/**
		 * Will hold organization's basic information
		 */
		OrganizationDetailsResponse.BasicInformation basicInformation =
				new OrganizationDetailsResponse.BasicInformation(
						org.getDisplayName(),
						org.getLegalName(),
						org.getOrganizationDescription(),
						Map.of(
								"id", org.getOrganizationType().getId().toString(),
								"type", org.getOrganizationType().getName()
						)
				);
		/**
		 * Will hold organization's contact details
		 */
		LinkedHashSet<PhoneDetail> phoneDetails =
				contactNumberHelper.fromJsonStringToObject(org.getAddress());
		Set<Map<String, String>> address =
				addressHelper.jsonAddressStringToSetObj(org.getAddress().get("address"));
		
		OrganizationDetailsResponse.ContactInformation contactInformation =
				new OrganizationDetailsResponse.ContactInformation(
						org.getCountry(),
						address,
						org.getEmail(),
						org.getWebsite(),
						phoneDetails,
						org.getExternalLinks()
				);
		/**
		 * Will retrieve organization users
		 */
		Set<OrganizationDetailsResponse.Users> users =
				org.getOrganizationUsers()
				.stream()
				.map(user -> new OrganizationDetailsResponse.Users(
						Map.of(
								"id", user.getUserId().getId().toString(),
								"firstName", user.getUserId().getFirstName(),
								"lastName", user.getUserId().getLastName(),
								"email", user.getUserId().getEmail()
						),
						user.getUserRole(),
						user.getUserJoined()
				))
				.collect(Collectors.toSet());
		
		return new OrganizationDetailsResponse(
				org.getId(),
				basicInformation,
				contactInformation,
				users
		);
		
	}
}
