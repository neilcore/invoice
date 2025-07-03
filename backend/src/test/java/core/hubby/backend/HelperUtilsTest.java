package core.hubby.backend;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import core.hubby.backend.core.dto.CountriesApiDTO;
import core.hubby.backend.core.helper.CountriesApiHelper;

@SpringBootTest
public class HelperUtilsTest {
	
	@Autowired
	private CountriesApiHelper countriesApiHelper;
	
	@Test
	public void itShouldQueryCountriesAPI() {
		/**
		 * This should retrieve a list of countries in
		 * @return - List<CountriesApiDTO> object of type
		 */
		List<CountriesApiDTO> willQueryListOfCountries = 
				countriesApiHelper.retrieveCountriesAPI();
		
		System.out.println(willQueryListOfCountries);
		
		/**
		 * This should retrieve a list of two letter country code and 
		 * @return - Set<Map<String, String>> object of type
		 */
		Set<Map<String, String>> retrieveSetOfTwoLetterCountryCode =
				countriesApiHelper.getTwoLetterCountryCode();
		
		System.out.println(retrieveSetOfTwoLetterCountryCode);
	}
}
