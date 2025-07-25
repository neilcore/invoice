package core.hubby.backend.tax.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import core.hubby.backend.tax.controller.dto.TaxResponse;
import core.hubby.backend.tax.entities.TaxDetails;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaxDetailMapper {
	@Mappings({
		@Mapping(target = "taxDetailsID", source = "id"),
		@Mapping(target = "organization", expression = "java(new TaxResponse.Organization(taxdetails.getOrganization().getId()))")
	})
	TaxResponse taxToTaxDetailsVO(TaxDetails taxdetails);
}
