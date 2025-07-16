package core.hubby.backend.tax.service;
import java.util.UUID;

import org.springframework.stereotype.Service;

import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.tax.controller.dto.CreateTaxDetailsRequests;
import core.hubby.backend.tax.controller.dto.TaxDetailsResponse;
import core.hubby.backend.tax.entities.SalesTaxBasis;
import core.hubby.backend.tax.entities.TaxDetails;
import core.hubby.backend.tax.mapper.TaxDetailMapper;
import core.hubby.backend.tax.repositories.SalesTaxBasisRepository;
import core.hubby.backend.tax.repositories.TaxDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxService {
	private final TaxDetailsRepository taxDetailsRepository;
	private final SalesTaxBasisRepository salesTaxBasisRepository;
	private final OrganizationRepository organizationRepository;
	private final TaxDetailMapper taxDetailMapper;
	
	public void createNewTaxDetails(CreateTaxDetailsRequests requests) {
		// New TaxDetails object
		TaxDetails taxDetails = new TaxDetails();
		
		// Get and set organization object
		Organization organization = 
				organizationRepository.findById(requests.organizationID())
				.orElseThrow(() -> new EntityNotFoundException(
						"Organization object with ID " + requests.organizationID()
						+ " not found."
		));
		taxDetails.setOrganization(organization);
		
		// Set tax number
		taxDetails.setTaxNumber(requests.taxNumber());
		
		// Set sales tax basis
		SalesTaxBasis salesTaxBasis = salesTaxBasisRepository.findById(requests.salesTaxBasis())
				.orElseThrow(() -> new EntityNotFoundException(
						"SalesTaxBasis object with ID " + requests.salesTaxBasis()
						+ " not found."
		));
		taxDetails.setSalesTaxBasis(salesTaxBasis);
		
		// Set tax period
		taxDetails.setTaxPeriod(requests.taxPeriod());
		
		// Set default sales tax
		taxDetails.setDefaultSalesTax(requests.defaultSalesTax());
		
		// Set default tax purchases
		taxDetails.setDefaultTaxPurchases(requests.defaultTaxPurchases());
		
		taxDetailsRepository.save(taxDetails);
	}
	
	public TaxDetailsResponse getTaxDetails(UUID organization) {
		TaxDetails getTaxDetails = taxDetailsRepository.findTaxDetailsByOrganizationID(organization)
				.orElseThrow(() -> new EntityNotFoundException("TaxDetails object cannot be found."));
		return taxDetailMapper.taxToTaxDetailsVO(getTaxDetails);
	}
}
