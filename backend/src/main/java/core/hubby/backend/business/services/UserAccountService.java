package core.hubby.backend.business.services;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import core.hubby.backend.business.dto.param.OrganizationCreateRequest;
import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.entities.UserAccount;
import core.hubby.backend.business.entities.UserAccountSettings;
import core.hubby.backend.business.entities.embedded.OrganizationUserInvites;
import core.hubby.backend.business.entities.embedded.OrganizationUsers;
import core.hubby.backend.business.enums.Roles;
import core.hubby.backend.business.repositories.UserAccountRepository;
import core.hubby.backend.business.repositories.UserAccountSettingsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAccountService {
	private final UserAccountRepository userRepository;
	private final UserAccountSettingsRepository accountSettingsRepository;
	
	// TODO - createUserAccountSetting() must test this method
	public void createUserAccountSetting(UserAccount account) {
        UserAccountSettings accountSettings = UserAccountSettings.builder()
        		.userAccount(account)
        		.build();
		try {
			accountSettingsRepository.save(accountSettings);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will retrieve UserAccount entity by ID.
	 * @param id  - accepts {@linkplain java.util.UUID} type 
	 * @return - {@linkplain UserAccount} object type.
	 */
	public UserAccount retrieveById(UUID id) {
		Optional<UserAccount> findById = userRepository.findById(id);
		
		if (findById.isEmpty()) {
			throw new NoSuchElementException("User entity " + id + " not found.");
		}
		return findById.get();
	}
	
	/**
	 * This will retrieve current authenticated user entity
	 * @return {@linkplain UserAccount} object
	 * @exception - throw {@linkplain java.util.NoSuchElementException}
	 * if it can't find one.
	 */
	public UserAccount getAuthenticatedUser() {
		Optional<UserAccount> findAuthUser = userRepository.findAuthenticatedUser();
		
		if (findAuthUser.isEmpty()) {
			throw new NoSuchElementException("Authenticated user not found in the database.");
		}
		
		return findAuthUser.get();
	}
	
	/**
	 * This method will set the organization's advisor.
	 * @return - return {@linkplain OrganizationUsers} object type
	 */
	public OrganizationUsers addOrganizationAdvisor() {
		return OrganizationUsers
				.builder()
				.userId(getAuthenticatedUser())
				.userRole(Roles.ADVISOR.toString())
				.build();
	}
	
	/**
	 * Add advisor to organization object
	 * @param organization - accepts {@linkplain Organization} object type.
	 */
	public void addAdvisor(Organization organization) {
		OrganizationUsers advisor = addOrganizationAdvisor();
		organization.setOrganizationUsers(Set.of(advisor));
	}
	
    public void addInvitedUsers(
    		Set<OrganizationCreateRequest.InviteUser> invitations,
    		Organization organization
    ) {
    	UserAccount authenticatedUser = getAuthenticatedUser();
    	UserAccount invitedUserAccount = null;
    	UserAccountSettings accountSettings = null;
    	Set<OrganizationUsers> organizationUsers = organization.getOrganizationUsers();
    	Set<OrganizationUserInvites> organizationUserInvites = new HashSet<>();
    	
    	for (OrganizationCreateRequest.InviteUser user: invitations) {
    		invitedUserAccount = retrieveById(user.userId()); // Get the UserAccount object
    		accountSettings = invitedUserAccount.getAccountSettings(); // Get User's account settings
    		
    		/**
    		 * If user account's @autoAcceptInvitation (from UserAccountSettings)
    		 * is set to @true (By default is set to @true).
    		 * automatically accept the invitation else
    		 * add the invited user to organization's @OrganizationUserInvites
    		 */
    		if (accountSettings.getAutoAcceptInvitation()) {
    			OrganizationUsers newOrganizationUser = OrganizationUsers
    					.builder()
    					.userId(invitedUserAccount)
    					.userRole(retrieveRole(user.role()))
    					.build();
    			organizationUsers.add(newOrganizationUser);
    		} else {
    			OrganizationUserInvites addInvites = OrganizationUserInvites.builder()
    					.invitationFor(invitedUserAccount)
    					.invitationBy(authenticatedUser)
    					.invitationRole(retrieveRole(user.role()))
    					.build();
    			organizationUserInvites.add(addInvites);
    		}
    		
    		invitedUserAccount = null;
    		accountSettings = null;
    	}
    	
    	organization.setOrganizationUsers(organizationUsers); // Set new organization users
    	organization.setOrganizationUserInvites(organizationUserInvites); // Set new organization user invites
    }
    
	private String retrieveRole(String role) {
		if (role == Roles.SUBSCRIBER.toString()) {
			return Roles.SUBSCRIBER.toString();
		} else if (role == Roles.READ_ONLY.toString()) {
			return Roles.READ_ONLY.toString();
		} else if (role == Roles.INVOICE_ONLY.toString()) {
			return Roles.READ_ONLY.toString();
		} else {
			return "n/a";
		}
	}
}
