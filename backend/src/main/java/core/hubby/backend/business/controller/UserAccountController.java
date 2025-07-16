package core.hubby.backend.business.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.hubby.backend.business.controller.dto.UserAccountChangeEmailRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/business/user/account/")
@RequiredArgsConstructor
public class UserAccountController {
	
	/**
	 * TODO - ideal to add email verification
	 * This method controller will update user account email address
	 * @param id - holds the User's ID
	 * @param request - accepts {@linkplain UserAccountChangeEmailRequest} object type
	 * @return
	 */
	@PutMapping("{id}/change/email")
	public ResponseEntity<?> changeUserAccountEmail(
			@PathVariable("id") UUID id,
			@Valid @RequestBody UserAccountChangeEmailRequest request
	){
		return null;
	}
}
