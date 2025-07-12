package core.hubby.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import core.hubby.backend.business.entities.embedded.DefaultCurrency;
import core.hubby.backend.core.helper.CountryService;

@SpringBootTest
public class HelperUtilsTest {
	
	@Autowired
	private CountryService countriesApiHelper;
	
	@Test
	public void itShouldReturnCountryCurrency() {
		DefaultCurrency mz = countriesApiHelper
				.returnCurrency("PH");
		
		System.out.println(mz);
	}
}
