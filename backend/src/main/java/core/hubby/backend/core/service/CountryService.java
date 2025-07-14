package core.hubby.backend.core.service;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import core.hubby.backend.business.entities.embedded.DefaultCurrency;
import core.hubby.backend.core.dto.CountriesDTO;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class CountryService {

	private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
	
	@Value("${spring.app.details.api.website}")
	private String REST_COUNTRIES_BASE_URL;
	
	private final RestClient restClient;
	private Set<CountriesDTO> countries;
	private Set<String> twoLetterCountryCodes;
	private Set<String> commonCountryNames;
	private Set<String> countryCodes;
	
	@PostConstruct
	private void init() {
		String queryParams = "name,cca2,idd,currencies";
		countries = restClient.get()
				.uri(
						uriBuilder -> uriBuilder
						.path("/all")
						.queryParam("fields", queryParams)
						.build()
				)
				.header("Content-Type", "application/json")
				.retrieve()
				.onStatus(HttpStatus.NOT_FOUND::equals, (request, response) -> {
					logger.error("Countries API endpoint not found: {}", response.getStatusCode());
					throw new RuntimeException("Countries API endpoint not found");
				})
				.onStatus(HttpStatus.BAD_REQUEST::equals, (request, response) -> {
					logger.error("Countries API endpoint can't process client request: {}", response.getStatusCode());
					throw new RuntimeException(
							"Countries API endpoint unable to process client's request" +
							" due to a client-side error."
					);
				})
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, (request, response) -> {
                    logger.error("Countries API is unavailable: {}", response.getStatusCode());
                    throw new RuntimeException("Countries API is temporarily unavailable");
                })
                .body(new ParameterizedTypeReference<Set<CountriesDTO>>() {});
		
		/**
		 * Collects all two letter country codes
		 */
		twoLetterCountryCodes = countries.stream()
				.map(data -> data.cca2())
				.collect(Collectors.toSet());
		/**
		 * Collects all common country names
		 */
		commonCountryNames = countries.stream()
				.map(data -> data.name().common())
				.collect(Collectors.toSet());
		
		/**
		 * Collects all country codes
		 */
		countryCodes = countries.stream()
				.map(data -> {
					String root = data.idd().root();
					String suffices = data.idd().suffixes().size() > 0 ? (String) data.idd().suffixes().toArray()[0] : null;
					
					return root + suffices;
				})
				.collect(Collectors.toSet());
	}
	
	/**
	 * Validate country codes (two letter country name)
	 * @param countryCode - accepts {@linkplain java.util.String} object type
	 * @return - @boolean
	 */
	public boolean validateCountry(String tlcountry) {
		if (!twoLetterCountryCodes.contains(tlcountry)) {
			return false;
		}
		return true;
	}
	
	public DefaultCurrency returnCurrency(String countryCode) {
		DefaultCurrency returnedCurrency = new DefaultCurrency();
		for(CountriesDTO country: countries) {
			if (Objects.equals(countryCode, country.cca2())) {
				country.currencies().forEach(
						(currencyCode, currencyDetail) -> {
							returnedCurrency.setName(currencyDetail.get("name"));
							returnedCurrency.setSymbol(currencyDetail.get("symbol"));
							returnedCurrency.setCurrencyCode(currencyCode);
						});
				break;
			}
		}
		return returnedCurrency;
	}
}
