package core.hubby.backend.business.services;
import org.springframework.stereotype.Service;

import core.hubby.backend.business.controller.dto.CreateOrganizationRequest;
import core.hubby.backend.business.controller.dto.OrganizationDetailsResponse;
import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.entities.OrganizationType;
import core.hubby.backend.business.entities.embedded.DefaultCurrency;
import core.hubby.backend.business.mapper.OrganizationMapper;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.business.repositories.OrganizationSettingsRepository;
import core.hubby.backend.business.repositories.OrganizationTypeRepository;
import core.hubby.backend.core.embedded.PhoneDetails;
import core.hubby.backend.core.exception.CountryNotFoundException;
import core.hubby.backend.core.service.CountryService;
import core.hubby.backend.core.service.phone.PhoneService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {
	private final OrganizationRepository organizationRepository;
	private final OrganizationSettingsRepository organizationSettingsRepo;
	private final OrganizationTypeRepository organizationTypeRepository;
	private final UserAccountService userAccountService;
	private final CountryService countryService;
	private final PhoneService phoneService;
	private final OrganizationMapper organizationMapper;
	
	public OrganizationService(
			OrganizationRepository organizationRepository,
			OrganizationSettingsRepository organizationSettingsRepository,
			OrganizationTypeRepository organizationTypeRepository,
			UserAccountService userAccountService,
			CountryService countryService,
			PhoneService phoneService,
			OrganizationMapper organizationMapper
	) {
		this.organizationRepository = organizationRepository;
		this.organizationSettingsRepo = organizationSettingsRepository;
		this.organizationTypeRepository = organizationTypeRepository;
		this.userAccountService = userAccountService;
		this.countryService = countryService;
		this.phoneService = phoneService;
		this.organizationMapper = organizationMapper;
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
		return organizationMapper.toOrganizationResponse(newOrganization);
	}
	
	/**
	 * This method will create new organization object
	 * @param - {@linkplain CreateOrganizationRequest} object type of data
	 * @return - {@linkplain OrganizationDetailsResponse} object type of data
	 */
	@Transactional
	public OrganizationDetailsResponse createNewOrganizationObject(CreateOrganizationRequest data) {
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
	 * @param info - accepts {@linkplain CreateOrganizationRequest.BasicInformation} object type.
	 * @param organization - accepts {@linkplain Organization} object type.
	 */
	private void setBasicInformation(
			CreateOrganizationRequest.BasicInformation info,
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
	 * - phone details
	 * - email
	 * - web-site
	 * - externalLinks
	 * @param contacts - accepts {@linkplain CreateOrganizationRequest.ContactDetails} object type
	 * @param organization - accepts {@linkplain Organization} object type.
	 */
	private void setContactDetails(
			CreateOrganizationRequest.ContactDetails contacts,
			Organization organization
	) {
		/**
		 * Validate country code and set default currency
		 */
		DefaultCurrency setDefaultCurrency = new DefaultCurrency();
		if(!countryService.validateCountry(contacts.countryCode())) {
			throw new CountryNotFoundException(contacts.countryCode());
		}
		setDefaultCurrency = countryService.returnCurrency(contacts.countryCode());
		
		// Validate phone numbers
		LinkedHashSet<PhoneDetails> validatePhoneDetails =
				phoneService.validatePhones(contacts.phoneNo(), Optional.of(organization.getCountry()));
		
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
		Organization getOrganization = organizationRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Organization object not found."));
		return organizationMapper.toOrganizationResponse(getOrganization);
	}
	
	/**
	 * This method will retrieve the industry type
	 * @param type - the id of type java.util.UUID
	 * @return OrganizationType object
	 */
	private OrganizationType getOrganizationType(UUID type) {
		OrganizationType getOrganizationType = organizationTypeRepository.findById(type)
				.orElseThrow(() -> new EntityNotFoundException("Organization type not found"));
		
		return getOrganizationType;
	}
}
