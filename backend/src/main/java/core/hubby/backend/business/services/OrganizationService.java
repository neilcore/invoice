package core.hubby.backend.business.services;
import org.springframework.stereotype.Service;

import core.hubby.backend.business.dto.param.OrganizationCreateRequest;
import core.hubby.backend.business.dto.vo.OrganizationDetailsResponse;
import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.entities.OrganizationType;
import core.hubby.backend.business.entities.embedded.OrganizationUserInvites;
import core.hubby.backend.business.entities.embedded.OrganizationUsers;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.business.repositories.OrganizationTypeRepository;
import core.hubby.backend.business.repositories.UserAccountRepository;
import core.hubby.backend.core.dto.PhoneDetail;
import core.hubby.backend.core.helper.AddressHelper;
import core.hubby.backend.core.helper.ContactNumberHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
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
	private final UserAccountRepository userRepository;
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
	 * This method will create new organization entity
	 * @param - {@linkplain OrganizationCreateRequest} object type of data
	 * @return - {@linkplain OrganizationDetailsResponse} object type of data
	 */
	@Transactional
	public OrganizationDetailsResponse create(OrganizationCreateRequest data) {
		/**
		 * Set the subscriber as the first organization user
		 */
		Set<OrganizationUsers> organizationUsers = Set.of(userAccountService.addSubscriber());
		
		/**
		 * Check if there are invites
		 */
		Set<OrganizationUserInvites> userInvites = userAccountService.addInvitedUsers(data.inviteOtherUser());
		
		/**
		 * Transform organization address data to fit the internal models
		 */
		Set<Map<String, String>> transformedAddress = addressHelper
				.transformOrganizationAddressData(data.contactDetails().address());
		
		String getPhoneDetails = 
				contactNumberHelper.parsePhoneNumbers(data.contactDetails().phoneNo());
		
		Organization newOrganization = Organization.builder()
				.organizationUsers(organizationUsers)
				.organizationUserInvites(userInvites)
				.displayName(data.basicInformation().displayName())
				.legalName(data.basicInformation().legalName())
				.organizationDescription(data.basicInformation().organizationDescription())
				.organizationType(getOrganizationType(data.basicInformation().organizationType()))
				.country(data.contactDetails().countryCode())
				.phoneNo(Map.of("phones", getPhoneDetails))
				.email(data.contactDetails().email())
				.website(data.contactDetails().website())
				.address(Map.of("address", transformedAddress.toString()))
				.externalLinks(
						data.contactDetails().externalLinks().stream()
						.filter(lkFilter ->
						externalLinks.contains(lkFilter.getLinkType().toLowerCase())
						).collect(Collectors.toSet())
				)
				.build();
		
		Organization org = organizationRepository.save(newOrganization);
		return mapOrganizationDetails(org);
		
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
