package core.hubby.backend.auth.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import core.hubby.backend.business.entities.User;
import core.hubby.backend.business.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository; 
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findUserByEmail = userRepository.findUserByEmailIgnoreCase(username);
        if (findUserByEmail.isPresent()) {
            return findUserByEmail.get();
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
	}

}
