package core.hubby.backend.tax.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import core.hubby.backend.tax.controller.dto.TaxResponse;
import core.hubby.backend.tax.entities.TaxDetails;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL, componentModel = "spring")
public interface TaxDetailMapper {
	@Mapping(target = "taxDetailsID", source = "id")
	@Mapping(target = "organization.organizationID", expression = "java(organization.getId())")
	TaxResponse taxToTaxDetailsVO(TaxDetails details);
}
