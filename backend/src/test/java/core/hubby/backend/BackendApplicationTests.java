package core.hubby.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import core.hubby.backend.business.entities.embedded.DefaultCurrency;
import core.hubby.backend.core.service.CountryService;

/**
 * It tells Spring to include the beans defined in
 * TestcontainersConfiguration (specifically your PostgreSQLContainer bean)
 * when building the application context.
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BackendApplicationTests {
	
	@Autowired
	CountryService countryService;
	
	@Test
	void contextLoads() {
	}
	
	// CountryService - validate if country code
	// exists or not
	@Test
	void validateCountry() {
		boolean response = countryService
				.validateCountry("QX");
		
		assertThat(response)
		.isNotEqualTo(true)
		.isEqualTo(false);
	}
	
	@Test
	void returnCurrency() {
		DefaultCurrency response = 
				countryService.returnCurrency("PH");
		
		assertThat(response.getName())
		.doesNotContain("euro")
		.contains("Philippine", "peso");
		
		assertThat(response.getSymbol())
		.doesNotContain("$")
		.isEqualTo("₱");
	}

}
