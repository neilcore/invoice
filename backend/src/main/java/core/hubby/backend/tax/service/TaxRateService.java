package core.hubby.backend.tax.service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.tax.TaxMapper;
import core.hubby.backend.tax.controller.dto.TaxRateRequests;
import core.hubby.backend.tax.entities.TaxComponent;
import core.hubby.backend.tax.entities.TaxRate;
import core.hubby.backend.tax.entities.TaxType;
import core.hubby.backend.tax.entities.embedded.TaxTypes;
import core.hubby.backend.tax.repositories.TaxRateRepository;
import core.hubby.backend.tax.repositories.TaxTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxRateService {
	private final TaxMapper taxMapper;
	private final TaxRateRepository taxRateRepository;
	private final TaxTypeRepository taxTypeRepository;
	private final OrganizationRepository organizationRepository;
	
	/**
	 * When a user creates organization's tax rate (e.g. clicks the create button),
	 * this will be returned in the response
	 * @param organizationID - accepts {@linkplain java.util.UUID} object type
	 * @return - {@linkplain TaxRateRequests} object.
	 */
	public Set<TaxRateRequests> generateTaxRate(@NotNull UUID organizationID) {
		// Get the country code of the organization
		Optional<String> countryCode = organizationRepository.findCountryUsingOrganizationId(organizationID);
		
		// Get the tax type using the organization's country code
		TaxType countryTaxType = taxTypeRepository.findByLabelIgnoreCase(countryCode.get()).get();
		// Get the tax type collections
		Set<TaxTypes> collections = countryTaxType.getTypeCollections();
		
		return taxMapper.taxTypeToTaxRateRequests(collections);
		
	}
	
	public Set<TaxRateRequests> createTaxRates(
			@NotNull UUID organizationID,
			Set<TaxRateRequests> requests
	) {
		Set<TaxRate> taxrates = requests.stream()
				.map(request -> {
					TaxRate newTaxRate = new TaxRate();
					TaxComponent taxComponent = new TaxComponent();
					
					// Set organization object
					Organization organizationObj = organizationRepository.findById(organizationID)
							.orElseThrow(() -> new EntityNotFoundException("Organization object not found."));
					newTaxRate.setOrganization(organizationObj);
					
					newTaxRate.setName(request.name());
					
					// Set tax component
					request.taxComponents().stream()
					.forEach(component -> {
						taxComponent.setName(component.name());
						taxComponent.setRate(component.rate());
						taxComponent.setIsCompound(component.isCompound());
						taxComponent.setNonRecoverable(component.nonRecoverable());
					});
					newTaxRate.setTaxComponent(taxComponent);
					
					// Set tax type
					newTaxRate.setTaxType(request.taxType());
					
					// Set status
					newTaxRate.setStatus(request.status());
					
					return newTaxRate;
				})
				.collect(Collectors.toSet());
		
		List<TaxRate> savedTaxRates = taxRateRepository.saveAll(taxrates);
		return taxMapper.toTaxRateRequests(savedTaxRates);
	}
}
