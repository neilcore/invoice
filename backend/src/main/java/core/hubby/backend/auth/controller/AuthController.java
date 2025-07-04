package core.hubby.backend.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.hubby.backend.auth.dto.param.LoginRequest;
import core.hubby.backend.auth.dto.param.SignupRequest;
import core.hubby.backend.auth.jwt.JwtUtils;
import core.hubby.backend.auth.vo.AuthResponse;
import core.hubby.backend.business.entities.UserAccount;
import core.hubby.backend.business.entities.UserAccountSettings;
import core.hubby.backend.business.enums.Roles;
import core.hubby.backend.business.repositories.UserAccountRepository;
import core.hubby.backend.business.repositories.UserAccountSettingsRepository;
import core.hubby.backend.business.services.UserAccountService;
import core.hubby.backend.core.api.error.ApiError;
import core.hubby.backend.core.helper.ContactNumberHelper;
import lombok.RequiredArgsConstructor;

@RequestMapping("api/auth/")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userRepository;
    private final UserAccountService userAccountService;
    private final ContactNumberHelper phoneService;
    
    @PostMapping("signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        } catch (AuthenticationException e) {
        	ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid credentials", List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(apiError);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount getUser = userRepository.findUserByEmailIgnoreCase(loginRequest.email())
        		.orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.email()));
        
        String jwtToken = jwtUtils.generateToken(getUser);
        List<String> roles = getUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        AuthResponse response = new AuthResponse(jwtToken, getUser.getUsername(), roles);

        return ResponseEntity.ok(response);
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        Optional<UserAccount> findUserEmail = userRepository.findUserByEmailIgnoreCase(signupRequest.email());

        if (findUserEmail.isPresent()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Email already exists");
            errorResponse.put("status", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        UserAccount userBuilder = UserAccount.builder()
        		.firstName(signupRequest.firstName())
        		.lastName(signupRequest.lastName())
        		.email(signupRequest.email())
        		.password(passwordEncoder.encode(signupRequest.password()))
        		.roles(Roles.NONE) // By default user account don't have a role yet
        		.build();

        UserAccount newUser = userRepository.save(userBuilder);
        
        /**
         * Once the new user is created,
         * create it's account settings
         */
        userAccountService.createUserAccountSetting(newUser);
        
        String jwtToken = jwtUtils.generateToken(newUser);
        List<String> roles = newUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        AuthResponse response = new AuthResponse(jwtToken, newUser.getUsername(), roles);

        return ResponseEntity.ok(response);
    }
}
