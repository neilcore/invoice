package core.hubby.backend.tax.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import core.hubby.backend.tax.controller.dto.TaxResponse;
import core.hubby.backend.tax.entities.TaxDetails;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TaxDetailMapper {

	public TaxResponse taxToTaxDetailsVO(TaxDetails details) {
		TaxResponse.Organization organization = 
				new TaxResponse.Organization(details.getOrganization().getId());
		
		TaxResponse response = new TaxResponse(
				details.getId(),
				organization,
				details.getPaysTax(),
				details.getTaxNumber(),
				details.getDefaultSalesTax(),
				details.getSalesTaxBasis(),
				details.getTaxPeriod(),
				details.getDefaultTaxPurchases()
		);
		
		return response;
	}
}
