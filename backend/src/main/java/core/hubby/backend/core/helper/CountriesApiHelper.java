package core.hubby.backend.core.helper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import core.hubby.backend.core.dto.CountriesApiDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CountriesApiHelper {
	@Value("${spring.app.details.api.website}")
	private String REST_COUNTRIES_BASE_URL;
	private final RestClient restClient;
	private static final Map<String, String> fieldQueryParameter = new HashMap<>();
	private static final Logger logger = LoggerFactory.getLogger(CountriesApiHelper.class);
	
	static {
		fieldQueryParameter.put("COUNTRY_NAME", "name");
		fieldQueryParameter.put("TWO_LETTER_COUNTRY_CODE", "cca2");
	}
	
	/**
	 * @GET request for https://restcountries.com/v3.1/all
	 * query parameters include:
	 * 	name and cca2
	 * @return - a list of {@linkplain CountriesApiDTO} objects
	 */
	public List<CountriesApiDTO> retrieveCountriesAPI() {
		return restClient.get()
				.uri(
						uriBuilder -> uriBuilder
						.path("/all")
						.queryParam(
								"fields",
								fieldQueryParameter.get("COUNTRY_NAME") + "," +
								fieldQueryParameter.get("TWO_LETTER_COUNTRY_CODE")
						)
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
				.body(new ParameterizedTypeReference<List<CountriesApiDTO>>() {});
	}
	
	/**
	 * @GET request for 2 letter country code
	 * @return - Set<Map<String, String>> object of type
	 */
	public Set<Map<String, String>> getTwoLetterCountryCode() {
		return restClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/all")
						.queryParam("fields", fieldQueryParameter.get("TWO_LETTER_COUNTRY_CODE"))
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
				.body(new ParameterizedTypeReference<Set<Map<String, String>>>() {});
	}
}
