package core.hubby.backend.tax.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.hubby.backend.core.api.ApiResponse;
import core.hubby.backend.tax.service.TaxService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/tax/")
@RequiredArgsConstructor
public class TaxController {
	private final TaxService taxService;
	
	@GetMapping
	public ResponseEntity<ApiResponse> taxes() {
		ApiResponse response = new ApiResponse();
		
		response.setData(taxService.taxDetailResponse());
		response.setMessage("Tax details found successfully.");
		response.setStatus(HttpStatus.OK);
		
		return ResponseEntity.ok(response);
	}
}
