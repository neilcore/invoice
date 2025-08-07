package core.hubby.backend.tax;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import core.hubby.backend.tax.entities.TaxType;
import core.hubby.backend.tax.entities.embedded.TaxTypes;
import core.hubby.backend.tax.repositories.TaxComponentRepository;
import core.hubby.backend.tax.repositories.TaxTypeRepository;
import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class TaxRunner implements CommandLineRunner {
	private static final String COMPONENT_GST = TaxComponentRepository.COMPONENT_GST;
	private static final String COMPONENT_VAT = TaxComponentRepository.COMPONENT_VAT;
	
	private final TaxTypeRepository taxTypeRepository;
	
	@Override
	public void run(String... args) throws Exception {
		if (taxTypeRepository.findAll().isEmpty()) {
			TaxType au = new TaxType();
			
			au.setLabel("AU");
			Set<TaxTypes> AUTypes = Set.of(
					new TaxTypes("OUTPUT", 10.00, "GST on Income", COMPONENT_GST, true),
					new TaxTypes("INPUT", 10.00, "GST on Expenses", COMPONENT_GST, true),
					new TaxTypes("BASEEXCLUDED", 0.00, "BAS Excluded", COMPONENT_GST, true),
					new TaxTypes("EXEMPTEXPENSES", 0.00, "GST Free Expenses", COMPONENT_GST, true),
					new TaxTypes("EXEMPTOUTPUT", 0.00, "GST Free Income", COMPONENT_GST, true),
					new TaxTypes("BASEXCLUDED", 0.00, "BAS Excluded", COMPONENT_GST, true),
					new TaxTypes("GSTONIMPORTS", 0.00, "GST Free Expenses", COMPONENT_GST, true)
			);
			au.setTypeCollections(AUTypes);
			
			taxTypeRepository.save(au);
			
			TaxType ph = new TaxType();
			
			ph.setLabel("PH");
			Set<TaxTypes> PHTypes = Set.of(
					new TaxTypes("OUTPUT", 12.00, "VAT on Income", COMPONENT_VAT, true),
					new TaxTypes("INPUT", 12.00, "VAT on Expenses", COMPONENT_VAT, true),
					new TaxTypes("BASEEXCLUDED", 0.00, "BAS Excluded", null, true)
			);
			ph.setTypeCollections(PHTypes);
			
			taxTypeRepository.save(ph);
			
			TaxType us = new TaxType();
			
			us.setLabel("US");
			Set<TaxTypes> USTypes = Set.of(
					new TaxTypes("OUTPUT", 0.00, "Tax on Sales", COMPONENT_GST, true),
					new TaxTypes("INPUT", 0.00, "Tax on Purchases", COMPONENT_GST, true),
					new TaxTypes("NONE", 0.00, "Tax Exempt", null, true),
					new TaxTypes("GSTONIMPORTS", 0.00, "Sales Tax on Imports", null, true)
			);
			us.setTypeCollections(USTypes);
			
			taxTypeRepository.save(us);
			
			TaxType sg = new TaxType(); // Singapore
			
			sg.setLabel("SG");
			Set<TaxTypes> SGTypes = Set.of(
					new TaxTypes("BADDEBTRECOVERY", 7.00, "2022 Bad Debt Recovery", null, true),
					new TaxTypes("BADDEBTRELIEF", 7.00, "2022 Bad Debt Relief", null, true),
					new TaxTypes("DSOUTPUT", 7.00, "	2022 Deemed Supplies", null, true),
					new TaxTypes("BLINPUT2", 0.00, "2022 Disallowed Expenses", null, true),
					new TaxTypes("BLINPUT3", 7.00, "2022 Disallowed Expenses", null, true),
					new TaxTypes("IMINPUT2", 0.00, "2022 Imports", null, true),
					new TaxTypes("IGDSINPUT2", 0.00, "2022 Imports under IGDS (input tax claim)", null, true),
					new TaxTypes("IMINPUT2", 0.00, "2022 Imports", null, true),
					new TaxTypes("IGDSINPUT3", 0.00, "2022 Imports under IGDS (no input tax claim)", null, true),
					new TaxTypes("IMN33", 0.00, "2022 Imports: non-regulation 33 exempt supplies", null, true),
					new TaxTypes("IMESS", 0.00, "2022 Imports: regulation 33 exempt supplies", null, true),
					new TaxTypes("IMRE", 0.00, "2022 Imports: taxable & exempt supplies", null, true),
					new TaxTypes("IM", 0.00, "	2022 Imports: taxable supplies", null, true)
			);
			sg.setTypeCollections(SGTypes);
			
			taxTypeRepository.save(sg);
		}
	}

}
