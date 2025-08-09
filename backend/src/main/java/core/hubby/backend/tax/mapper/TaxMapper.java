package core.hubby.backend.tax.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueMappingStrategy;

import core.hubby.backend.tax.controller.dto.TaxRateRequests;
import core.hubby.backend.tax.entities.TaxRate;
import core.hubby.backend.tax.entities.embedded.TaxTypes;
import core.hubby.backend.tax.repositories.TaxRateRepository;

@Mapper(
		imports = TaxRateRepository.class,
		nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
		componentModel = "spring"
)
public interface TaxMapper {
	
	Set<TaxRateRequests> taxTypeToTaxRateRequests(Set<TaxTypes> types);
	
	@Mappings({
		@Mapping(source = "type", target = "taxType"),
		@Mapping(source = "rate", target = "effectiveRate"),
		@Mapping(target = "status", expression = "java(TaxRateRepository.TAXRATE_STATUS_ACTIVE)"),
		@Mapping(target = "taxComponents", expression = "java(taxTypeToComponents(type))"),
		@Mapping(target = "applyToAccounts", ignore = true)
	})
	TaxRateRequests taxTypeToTaxRateRequest(TaxTypes type);
	
	/**
	 * This is to map for DTO's components objects
	 * @param types - {@linkplain TaxTypes} object type.
	 * @return - {@linkplain java.util.Link} that holds {@linkplain TaxRateRequests.Component} objects.
	 */
	default List<TaxRateRequests.Component> taxTypeToComponents(TaxTypes types) {
		List<TaxRateRequests.Component> components = List.of(new TaxRateRequests.Component(
				types.getComponent(),
				new BigDecimal(types.getRate()),
				null,
				null
		));
		return components;
	}
	
	Set<TaxRateRequests> toTaxRateRequests(List<TaxRate> taxRates);
	
	@Mappings({
		@Mapping(target = "taxComponents", source = "tx"),
		@Mapping(target = "effectiveRate", source = "effectiveRate"),
		@Mapping(target = "status", expression = "java(TaxRateRepository.TAXRATE_STATUS_ACTIVE)"),
		@Mapping(target = "applyToAccounts", source = "tx"),
		@Mapping(target = "systemDefined", ignore = true)
	})
	TaxRateRequests toTaxRateRequest(TaxRate tx);
	
	default List<TaxRateRequests.Component> taxRateToComponent(TaxRate rate) {
		List<TaxRateRequests.Component> component = rate.getTaxComponent().stream()
				.map(cm -> new TaxRateRequests.Component(
						cm.getName(), cm.getRate(),
						cm.getIsCompound(),
						cm.getNonRecoverable()
				)).toList();
		
		return component;
	}
	
	default TaxRateRequests.ApplyToAccounts taxRateToApplyAccounts(TaxRate tx) {
		TaxRateRequests.ApplyToAccounts accounts = new TaxRateRequests.ApplyToAccounts(
				tx.getApplyToAssetAccount(), tx.getApplyToEquityAccount(),
				tx.getApplyToExpensesAccount(), tx.getApplyToLiabilitiesAccount(),
				tx.getApplyToRevenueAccount()
		);
		
		return accounts;
	}

}
