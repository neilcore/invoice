package core.hubby.backend.auth.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import core.hubby.backend.business.entities.User;
import core.hubby.backend.business.enums.Roles;
import core.hubby.backend.business.repositories.UserRepository;

public class DefaultUserInitializer implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public DefaultUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Value("${spring.app.details.default_credentials.default_first_name}")
	private String defaultFirstName;
	@Value("${spring.app.details.default_credentials.default_last_name}")
	private String defaultLastName;
	@Value("${spring.app.details.default_credentials.default_email}")
	private String defaultEmail;
	@Value("${spring.app.details.default_credentials.default_password}")
	private String defaultPassword;
	@Value("${spring.app.details.default_credentials.default_phone_no}")
	private String defaultPhoneNumber;
	
	@Override
	public void run(String... args) throws Exception {
		/* This will create the default user */
		Optional<User> existingEmailOptional = userRepository.findUserByEmailIgnoreCase(defaultEmail);
		if (!existingEmailOptional.isPresent()) {
			User defaultUser = new User();
			defaultUser.setEmail(defaultEmail);
			defaultUser.setFirstName(defaultFirstName);
			defaultUser.setLastName(defaultLastName);
			defaultUser.setPassword(passwordEncoder.encode(defaultPassword));
			defaultUser.setPhoneNumber(defaultPhoneNumber);
			defaultUser.setRoles(Roles.NONE);
			
			userRepository.save(defaultUser);
		}
		
	}

}
