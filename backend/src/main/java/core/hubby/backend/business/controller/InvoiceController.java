package core.hubby.backend.business.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.hubby.backend.business.controller.dto.CreateInvoiceRequest;
import core.hubby.backend.business.services.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/business/organization/")
@RequiredArgsConstructor
public class InvoiceController {
	private final InvoiceService invoicesService;
	/**
	 * This will handle invoice create requests.
	 * @param organizationId - accepts {@linkplain java.util.UUID} object type
	 * @param request - accepts {@linkplain CreateInvoiceRequest} object type
	 * @return
	 */
	@PostMapping(path = "{organizationId}/invoice/create")
	public ResponseEntity<?> create(
			@PathVariable("organizationId") UUID organizationId,
			@Valid @RequestBody CreateInvoiceRequest request
	){
		return null;
	}
}
