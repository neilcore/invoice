package core.hubby.backend.tax;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import core.hubby.backend.tax.controller.dto.TaxRateRequests;
import core.hubby.backend.tax.entities.TaxRate;
import core.hubby.backend.tax.entities.embedded.TaxTypes;
import core.hubby.backend.tax.repositories.TaxRateRepository;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TaxMapper {
	
	public Set<TaxRateRequests> taxTypeToTaxRateRequests(Set<TaxTypes> collections) {
		Set<TaxRateRequests> taxRates = new HashSet<>();
		for (TaxTypes type: collections) {
			taxRates.add(taxTypeToTaxRateRequest(type));
		}
		
		return taxRates;
	}
	
	public TaxRateRequests taxTypeToTaxRateRequest(TaxTypes type) {
		return new TaxRateRequests(
				type.getName(),
				type.getType(),
				// Set tax components
				List.of(new TaxRateRequests.Component(
						type.getComponent(),
						new BigDecimal(type.getRate()),
						null,
						null
						
				)),
				new BigDecimal(type.getRate()),
				new TaxRateRequests.ApplyToAccounts(null, null, null, null, null),
				// Whether it is system defined or not.
				// It is customizable if it is not system defined
				type.getSystemDefined(),
				// Set all status to active
				TaxRateRepository.TAXRATE_STATUS_ACTIVE
		);
	}
	
	public Set<TaxRateRequests> toTaxRateRequests(List<TaxRate> taxRates) {
		Set<TaxRateRequests> taxRateRequests = taxRates.stream()
				.map(tx -> toTaxRateRequest(tx)).collect(Collectors.toSet());
		
		return taxRateRequests;
	}
	
	public TaxRateRequests toTaxRateRequest(TaxRate tx) {
		List<TaxRateRequests.Component> component = tx.getTaxComponent().stream()
				.map(cm -> new TaxRateRequests.Component(
						cm.getName(), cm.getRate(),
						cm.getIsCompound(),
						cm.getNonRecoverable()
				)).toList();

		TaxRateRequests.ApplyToAccounts accounts = new TaxRateRequests.ApplyToAccounts(
				tx.getApplyToAssetAccount(), tx.getApplyToEquityAccount(),
				tx.getApplyToExpensesAccount(), tx.getApplyToLiabilitiesAccount(),
				tx.getApplyToRevenueAccount()
		);
		
		TaxRateRequests requests = new TaxRateRequests(
				tx.getName(),
				tx.getTaxType(),
				component,
				tx.getEffectiveRate(),
				accounts,
				null,
				tx.getStatus()
		);
		return requests;
	}
}
