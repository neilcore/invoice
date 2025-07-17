package core.hubby.backend.tax.service;

import org.springframework.stereotype.Service;

import core.hubby.backend.tax.repositories.TaxRateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxRateService {
	private final TaxRateRepository taxRateRepository;
}
