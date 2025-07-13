package core.hubby.backend.business.services;
import org.springframework.stereotype.Service;

import core.hubby.backend.business.dto.param.OrganizationCreateRequest;
import core.hubby.backend.business.dto.vo.OrganizationDetailsResponse;
import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.entities.OrganizationType;
import core.hubby.backend.business.entities.embedded.DefaultCurrency;
import core.hubby.backend.business.entities.embedded.PhoneDetails;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.business.repositories.OrganizationTypeRepository;
import core.hubby.backend.core.exception.CountryNotFoundException;
import core.hubby.backend.core.helper.CountryService;
import core.hubby.backend.core.helper.PhoneNumberService;
import jakarta.transaction.Transactional;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrganizationService {
	private final OrganizationRepository organizationRepository;
	private final OrganizationTypeRepository organizationTypeRepository;
	private final UserAccountService userAccountService;
	private final CountryService countryService;
	private final PhoneNumberService phoneNumberService;
	
	public OrganizationService(
			OrganizationRepository organizationRepository,
			OrganizationTypeRepository organizationTypeRepository,
			UserAccountService userAccountService,
			CountryService countryService,
			PhoneNumberService phoneNumberService
	) {
		this.organizationRepository = organizationRepository;
		this.organizationTypeRepository = organizationTypeRepository;
		this.userAccountService = userAccountService;
		this.countryService = countryService;
		this.phoneNumberService = phoneNumberService;
	}
	
	/**
	 * This method will retrieve an organization entity.
	 * If obj is null, return a new Organization object.
	 * If obj is instance of UUID, retrieve the organization object using it's ID.
	 * @param obj - accepts {@linkplain Object} type.
	 * @return - {@linkplain Organization} object type.
	 */
	protected Organization getOrganizationObject(Object obj) {
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
		// For now set this as organization's profile picture
		org.setProfileImage("sample_image_url");
		
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
		/**
		 * Validate country code and set default currency
		 */
		DefaultCurrency setDefaultCurrency = new DefaultCurrency();
		if(!countryService.validateCountryCode(contacts.countryCode())) {
			throw new CountryNotFoundException(contacts.countryCode());
		}
		setDefaultCurrency = countryService.returnCurrency(contacts.countryCode());
		
		// Validate phone numbers
		LinkedHashSet<PhoneDetails> validatePhoneDetails =
				phoneNumberService.validatePhoneDetails(contacts.phoneNo(), organization.getCountry());
		
		organization.setContactDetails(
				contacts.countryCode(),
				setDefaultCurrency,
				contacts.address(),
				validatePhoneDetails,
				contacts.email(),
				contacts.website(),
				contacts.externalLinks()
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
						org.getProfileImage(),
						org.getDisplayName(),
						org.getLegalName(),
						org.getOrganizationDescription(),
						Map.of(
								"id", org.getOrganizationType().getId().toString(),
								"type", org.getOrganizationType().getName()
						)
				);
		
		OrganizationDetailsResponse.ContactInformation contactInformation =
				new OrganizationDetailsResponse.ContactInformation(
						org.getCountry(),
						org.getAddress(),
						org.getEmail(),
						org.getWebsite(),
						org.getPhoneNo(),
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
								"id", user.getUserId().getUserId().toString(),
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
