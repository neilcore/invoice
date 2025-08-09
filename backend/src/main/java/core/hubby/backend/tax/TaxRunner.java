package core.hubby.backend.tax;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import core.hubby.backend.tax.entities.TaxType;
import core.hubby.backend.tax.entities.embedded.TaxTypes;
import core.hubby.backend.tax.repositories.TaxComponentRepository;
import core.hubby.backend.tax.repositories.TaxTypeRepository;
import lombok.RequiredArgsConstructor;

/**
 * This default tax types will give you base start.
 */
@Component
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
			
			/**
			 * | **Code**     | **Used For**                                                              |
				| ------------ | ------------------------------------------------------------------------- |
				| `VAT12`      | Standard VAT on sales of goods/services.                                  |
				| `VATZERO`    | Sales to PEZA-registered or export-oriented businesses.                   |
				| `EXEMPT`     | Sale of agricultural products, education, etc. (not subject to VAT).      |
				| `INPUTVAT`   | VAT you can claim back from purchases/expenses.                           |
				| `VATIMPORT`  | VAT applied when importing goods into the Philippines.                    |
				| `WHTEXP`     | For professional services and contractors.                                |
				| `WHTCOMP`    | Applied on employee salaries (typically deducted by employer).            |
				| `PERCENTTAX` | Applied to small businesses not VAT-registered (gross receipts tax).      |
				| `NONE`       | For fully non-taxable transactions (e.g., donations, internal transfers). |

			 */
			TaxType ph = new TaxType();
			
			ph.setLabel("PH");
			Set<TaxTypes> PHTypes = Set.of(
					new TaxTypes("VAT12", 12.00, "Value-Added Tax (12%)", COMPONENT_VAT, true),
					new TaxTypes("VATZERO", 0.00, "	VAT Zero-Rated Sales", COMPONENT_VAT, true),
					new TaxTypes("EXEMPT", 0.00, "VAT Exempt Sales", COMPONENT_VAT, true),
					new TaxTypes("INPUTVAT", 12.00, "Input VAT on Purchases", COMPONENT_VAT, true),
					new TaxTypes("VATIMPORT", 0.00, "	VAT on Importation", COMPONENT_VAT, true),
					new TaxTypes("WHTEXP", 1.00, "Expanded Withholding Tax", COMPONENT_VAT, true),
					new TaxTypes("WHTCOMP", 2.00, "	Withholding Tax on Compensation", COMPONENT_VAT, true),
					new TaxTypes("PERCENTTAX", 3.00, "Percentage Tax (Non-VAT)", COMPONENT_VAT, true),
					new TaxTypes("NONE", 0.00, "No Tax", null, true)
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
