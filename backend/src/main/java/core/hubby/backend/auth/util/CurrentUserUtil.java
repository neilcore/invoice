package core.hubby.backend.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import core.hubby.backend.business.entities.User;

public class CurrentUserUtil {
	private static final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
    public static String getCurrentAuthenticatedUserEmail() {
        
        if (isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User getUser) {
                return getUser.getEmail();
            } else {
                return principal.toString();
            }
        }
        return null; // No user is authenticated
    }

    public static UserDetails getCurrentUserDetails() {

        if (isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails getUserDetails) {
                return getUserDetails;
            }
        }
        return null; // No UserDetails available or user not authenticated
    }
    
    private static boolean isAuthenticated() {
    	if (authentication != null && authentication.isAuthenticated()) return true;
    	return false;
    }
}
