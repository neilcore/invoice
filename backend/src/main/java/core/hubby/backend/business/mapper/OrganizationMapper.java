package core.hubby.backend.business.mapper;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import core.hubby.backend.business.controller.dto.OrganizationDetailsResponse;
import core.hubby.backend.business.entities.Organization;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class OrganizationMapper {
	
	public OrganizationDetailsResponse toOrganizationResponse(Organization organization) {
		// Setup basic information
		OrganizationDetailsResponse.BasicInformation basicInformation =
				new OrganizationDetailsResponse.BasicInformation(
						organization.getProfileImage(), organization.getDisplayName(),
						organization.getLegalName(), organization.getOrganizationDescription(),
						Map.of(
								"id", organization.getOrganizationType().getId().toString(),
								"name", organization.getOrganizationType().getName()
						)
				);
		
		// Setup contact information
		OrganizationDetailsResponse.ContactInformation contactInfo = 
				new OrganizationDetailsResponse.ContactInformation(
						organization.getCountry(),
						organization.getAddress(),
						organization.getEmail(),
						organization.getWebsite(),
						organization.getPhoneNo(),
						organization.getExternalLinks()
				);
		
		// Setup organization user information
		Set<OrganizationDetailsResponse.Users> users = 
				organization.getOrganizationUsers().stream()
				.map(user -> new OrganizationDetailsResponse.Users(
						Map.of(
								"userID", user.getUserId().getUserId().toString(),
								"name", user.getUserId().getFirstName() + " " + user.getUserId().getLastName()
						),
						user.getUserRole(),
						user.getUserJoined()
				))
				.collect(Collectors.toSet());
		
		OrganizationDetailsResponse response = new OrganizationDetailsResponse(
				organization.getId(),
				basicInformation,
				contactInfo,
				users
		);
		
		return response;
	}
}
