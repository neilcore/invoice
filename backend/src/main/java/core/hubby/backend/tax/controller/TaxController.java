package core.hubby.backend.tax.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.hubby.backend.core.api.ApiResponse;
import core.hubby.backend.tax.controller.dto.TaxRateRequests;
import core.hubby.backend.tax.service.TaxRateService;
import core.hubby.backend.tax.service.TaxService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/taxes/")
@RequiredArgsConstructor
public class TaxController {
	private final TaxService taxService;
	private final TaxRateService taxRateService;
	
	@GetMapping(path = "{organizationID}")
	public ResponseEntity<ApiResponse> taxes(@PathVariable("organizationID") UUID organizationID) {
		ApiResponse response = new ApiResponse();
		
		response.setData(taxService.taxDetailResponse(organizationID));
		response.setMessage("Tax details found successfully.");
		response.setStatus(HttpStatus.OK);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(path = "rates/{organizationID}/generate")
	public ResponseEntity<ApiResponse> generateTaxRate(@PathVariable UUID organizationID) {
		Map<String, Set<TaxRateRequests>> taxRates = new HashMap<>();
		ApiResponse response = new ApiResponse();
		
		taxRates.put("TaxRates", taxRateService.generateTaxRate(organizationID));
		response.setData(taxRates);
		response.setMessage("Tax rates generated successfully.");
		return ResponseEntity.ok(response);
	}
}
