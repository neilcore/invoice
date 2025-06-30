package core.hubby.backend.business.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.hubby.backend.business.dto.param.CreateOrganizationDTO;
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
	public ResponseEntity<OrganizationVO> createOrganization(@RequestBody @Valid CreateOrganizationDTO data) {
		return ResponseEntity.ok(organizationService.createOrganization(data));
	}
	
	@GetMapping("{id}")
	public ResponseEntity<OrganizationVO> getOrganization(@PathVariable(required = true) UUID id) {
		return ResponseEntity.ok(organizationService.getOrganization(id));
	}
}
