package core.hubby.backend.tax;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import core.hubby.backend.tax.controller.dto.TaxRateRequests;
import core.hubby.backend.tax.entities.embedded.TaxTypes;
import core.hubby.backend.tax.repositories.TaxRateRepository;

@Mapper
public abstract class TaxMapper {
	public Set<TaxRateRequests> toTaxRateRequests(Set<TaxTypes> collections) {
		Set<TaxRateRequests> taxRates = new HashSet<>();
		for (TaxTypes type: collections) {
			taxRates.add(toTaxRateRequest(type));
		}
		
		return taxRates;
	}
	
	public TaxRateRequests toTaxRateRequest(TaxTypes type) {
		return new TaxRateRequests(
				type.getName(),
				type.getType(),
				// Set tax components
				List.of(new TaxRateRequests.Component(type.getComponent(), type.getRate(), null, null)),
				type.getRate(),
				new TaxRateRequests.ApplyToAccounts(null, null, null, null, null),
				// Whether it is system defined or not.
				// It is customizable if it is not system defined
				type.getSystemDefined(),
				// Set all status to active
				TaxRateRepository.TAXRATE_STATUS_ACTIVE
		);
	}
}
