package core.hubby.backend.business.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.hubby.backend.business.dto.param.OrganizationCreateRequest;
import core.hubby.backend.business.dto.param.OrganizationUserInvitationUpdateRequest;
import core.hubby.backend.business.dto.vo.OrganizationElementVO;
import core.hubby.backend.business.dto.vo.OrganizationDetailsResponse;
import core.hubby.backend.business.services.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * x-organization-id header is required for
 * accessing existing organization entity.
 */
@RestController
@RequestMapping("api/v1/business/organization/")
@RequiredArgsConstructor
public class OrganizationController {
	private final OrganizationService organizationService;
	
	@PostMapping("create")
	public ResponseEntity<OrganizationDetailsResponse> createOrganization(@RequestBody @Valid OrganizationCreateRequest data) {
		return ResponseEntity.ok(organizationService.createNewOrganizationObject(data));
	}
	
	/**
	 * TODO finish updateOrganization method controller for updating
	 * organization entity
	 * @param id
	 * @param data
	 * @return
	 */
	@PutMapping("update/{id}")
	public ResponseEntity<OrganizationDetailsResponse> updateOrganization(
			@RequestHeader("x-organization-id") UUID xOrganizationHeaderId,
			@PathVariable(name = "id") UUID id,
			@Valid @RequestBody OrganizationCreateRequest data
	) {
		return null;
	}
	
	@GetMapping("{id}")
	public ResponseEntity<OrganizationDetailsResponse> getOrganization(@PathVariable(required = true) UUID id) {
		return ResponseEntity.ok(organizationService.retrieveOrganizationById(id));
	}
	
	// Get Organization Elements
	@GetMapping("elements")
	public ResponseEntity<OrganizationElementVO> getOrganizationElements() {
		return null;
	}
	
	/**
	 * Method controller for handling user organization invitation
	 * @param id
	 * @param data
	 * @return
	 */
	@PutMapping("{id}/invite/users/update")
	public ResponseEntity<?> updatedUserOrganizationInvitation(
			@PathVariable("id") UUID id,
			@Valid @RequestBody OrganizationUserInvitationUpdateRequest data
	) {
		return null;
	}
	
	/**
	 * TODO work on delete method controller for organization
	 * @param id
	 * @return
	 */
	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteOrganizationAccount(@PathVariable("id") UUID id) {
		return null;
	}
}
