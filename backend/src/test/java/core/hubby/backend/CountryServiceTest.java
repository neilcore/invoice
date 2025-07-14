package core.hubby.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import core.hubby.backend.business.entities.embedded.DefaultCurrency;
import core.hubby.backend.core.service.CountryService;

@SpringBootTest
public class CountryServiceTest {
	
	@Autowired
	private CountryService countryService;
	
	@Test
	public void itShouldReturnCountryCurrency() {
		DefaultCurrency currency = countryService
				.returnCurrency("PH");
		
		// validate currency
		assertThat(currency.getCurrencyCode())
		.isNotNull()
		.isEqualTo("PHP")
		.isNotEqualTo("AU");
		
		// validate currency symbol
		assertThat(currency.getSymbol())
		.isNotBlank()
		.isNotEqualTo("$")
		.isEqualTo("₱");
	}
	
	@Test
	public void itShouldValidateCountryCode() {
		boolean shouldBeTrue = countryService.validateCountryCode("PH");
		assertThat(shouldBeTrue)
		.isNotNull()
		.isEqualTo(true)
		.isNotEqualTo(false);
		
		boolean shouldBeFalse = countryService.validateCountryCode("XXX");
		assertThat(shouldBeFalse)
		.isNotNull()
		.isNotEqualTo(true)
		.isEqualTo(false);
	}
}
