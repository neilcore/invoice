package core.hubby.backend.tax;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import core.hubby.backend.tax.entities.TaxType;
import core.hubby.backend.tax.entities.embedded.TaxTypes;
import core.hubby.backend.tax.repositories.TaxTypeRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaxRunner implements CommandLineRunner {
	
	private final TaxTypeRepository taxTypeRepository;
	
	@Override
	public void run(String... args) throws Exception {
		TaxType au = new TaxType();
		
		au.setLabel("AU");
		Set<TaxTypes> AUTypes = Set.of(
				new TaxTypes("OUTPUT", 10.00, "GST on Income", true),
				new TaxTypes("INPUT", 10.00, "GST on Expenses", true),
				new TaxTypes("BASEEXCLUDED", 0.00, "BAS Excluded", true)
		);
		au.setTypeCollections(AUTypes);
		
		taxTypeRepository.save(au);
		
		TaxType us = new TaxType();
		
		us.setLabel("US");
		Set<TaxTypes> USTypes = Set.of(
				new TaxTypes("OUTPUT", 0.00, "Tax on Sales", true),
				new TaxTypes("INPUT", 0.00, "Tax on Purchases", true),
				new TaxTypes("NONE", 0.00, "Tax Exempt", true),
				new TaxTypes("GSTONIMPORTS", 0.00, "Sales Tax on Imports", true)
		);
		us.setTypeCollections(USTypes);
		
		taxTypeRepository.save(us);
		
		TaxType sg = new TaxType(); // Singapore
		
		sg.setLabel("US");
		Set<TaxTypes> SGTypes = Set.of(
				new TaxTypes("BADDEBTRECOVERY", 7.00, "2022 Bad Debt Recovery", true),
				new TaxTypes("BADDEBTRELIEF", 7.00, "2022 Bad Debt Relief", true),
				new TaxTypes("DSOUTPUT", 7.00, "	2022 Deemed Supplies", true),
				new TaxTypes("BLINPUT2", 0.00, "2022 Disallowed Expenses", true),
				new TaxTypes("BLINPUT3", 7.00, "2022 Disallowed Expenses", true),
				new TaxTypes("IMINPUT2", 0.00, "2022 Imports", true),
				new TaxTypes("IGDSINPUT2", 0.00, "2022 Imports under IGDS (input tax claim)", true),
				new TaxTypes("IMINPUT2", 0.00, "2022 Imports", true),
				new TaxTypes("IGDSINPUT3", 0.00, "2022 Imports under IGDS (no input tax claim)", true),
				new TaxTypes("IMN33", 0.00, "2022 Imports: non-regulation 33 exempt supplies", true),
				new TaxTypes("IMESS", 0.00, "2022 Imports: regulation 33 exempt supplies", true),
				new TaxTypes("IMRE", 0.00, "2022 Imports: taxable & exempt supplies", true),
				new TaxTypes("IM", 0.00, "	2022 Imports: taxable supplies", true)
		);
		sg.setTypeCollections(SGTypes);
		
		taxTypeRepository.save(sg);
	}

}
