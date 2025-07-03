package core.hubby.backend.business.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.hubby.backend.business.dto.param.OrganizationDetailsDTO;
import core.hubby.backend.business.dto.param.UpdateUserOrganizationInvitation;
import core.hubby.backend.business.dto.vo.OrganizationElementVO;
import core.hubby.backend.business.dto.vo.OrganizationVO;
import core.hubby.backend.business.services.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/organization/")
@RequiredArgsConstructor
public class OrganizationController {
	private final OrganizationService organizationService;
	
	@PostMapping("create")
	public ResponseEntity<OrganizationVO> createOrganization(@RequestBody @Valid OrganizationDetailsDTO data) {
		return ResponseEntity.ok(organizationService.create(data));
	}
	
	/**
	 * TODO finish updateOrganization method controller for updating
	 * organization entity
	 * @param id
	 * @param data
	 * @return
	 */
	@PutMapping("update/{id}")
	public ResponseEntity<OrganizationVO> updateOrganization(
			@PathVariable("id") UUID id,
			@Valid @RequestBody OrganizationDetailsDTO data
	) {
		return null;
	}
	
	@GetMapping("{id}")
	public ResponseEntity<OrganizationVO> getOrganization(@PathVariable(required = true) UUID id) {
		return ResponseEntity.ok(organizationService.getOrganization(id));
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
			@Valid @RequestBody UpdateUserOrganizationInvitation data
	) {
		organizationService.updateUserOrganizationInvitation(id, data);
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
