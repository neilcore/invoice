package core.hubby.backend.business.services;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import core.hubby.backend.business.dto.param.OrganizationCreateRequest;
import core.hubby.backend.business.entities.UserAccount;
import core.hubby.backend.business.entities.embedded.OrganizationUserInvites;
import core.hubby.backend.business.entities.embedded.OrganizationUsers;
import core.hubby.backend.business.enums.Roles;
import core.hubby.backend.business.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAccountService {
	private final UserAccountRepository userRepository;
	
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
	 * This method will set the organization's subscriber.
	 * @return - return {@linkplain OrganizationUsers} object type
	 */
	public OrganizationUsers addSubscriber() {
		return OrganizationUsers
				.builder()
				.userId(getAuthenticatedUser())
				.userRole(Roles.SUBSCRIBER.toString())
				.build();
	}
	
	private boolean allUserIDExists(Set<UUID> ids) {
		if (userRepository.checkIfUsersExists(ids)) return true;
		return false;
	}
	
    /**
     * Adds invited users to an organization by creating OrganizationUserInvites objects.
     *
     * @param users A set of InviteUser objects containing user IDs and roles.
     * @return A set of OrganizationUserInvites objects, or an empty set if not all user IDs exist.
     * @throws IllegalStateException if an invited user account is not found in the database
     * after `allUserIDExists` returned true, indicating a data inconsistency.
     */
    public Set<OrganizationUserInvites> addInvitedUsers(
            Set<OrganizationCreateRequest.InviteUser> users
    ) {
        // 1. Retrieve the authenticated user once.
        // This method should handle its own "not found" scenario (e.g., by throwing an exception).
        UserAccount authenticatedUser = getAuthenticatedUser();

        // 2. Extract invited user IDs for the existence check.
        Set<UUID> invitedUserIds = users
                .stream()
                .map(OrganizationCreateRequest.InviteUser::userId)
                .collect(Collectors.toSet());

        // 3. Check if all invited user IDs exist in the database.
        if (allUserIDExists(invitedUserIds)) {
            // 4. If all exist, proceed to map and build invitation objects.
            return users.stream()
                    .map(inviteUser -> {
                        // Retrieve the invited user's account from the repository.
                        // Use orElseThrow to handle the case where the user might not be found,
                        // although `allUserIDExists` should prevent this if implemented correctly.
                        UserAccount invitedUserAccount = retrieveById(inviteUser.userId());

                        // Retrieve the role string.
                        String userRole = retrieveRole(inviteUser.role());

                        // Explicitly declare the type of the variable to help the compiler infer the map's return type.
                        OrganizationUserInvites builtInvite = OrganizationUserInvites.builder()
                                .invitationFor(invitedUserAccount)
                                .invitationBy(authenticatedUser)
                                .invitationRole(userRole)
                                .build();
                        return builtInvite;
                    }).collect(Collectors.toSet());
        } else {
            // 5. If not all user IDs exist, return an empty set.
            // You might also consider throwing a more specific exception here,
            // e.g., `InvalidInviteUsersException` if this is an error condition.
            System.out.println("DEBUG: Not all invited user IDs exist. Returning empty set.");
            return Set.of(); // Return an empty set instead of null
        }
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
