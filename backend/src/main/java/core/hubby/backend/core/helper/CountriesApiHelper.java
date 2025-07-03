package core.hubby.backend.core.helper;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CountriesApiHelper {
	@Value("${spring.app.details.api.website}")
	private String REST_COUNTRIES_BASE_URL;
	private static final RestClient restClient = RestClient.create();
	
	/**
	 * @GET request for https://restcountries.com/v3.1/all
	 * query parameters include:
	 * 	name and cca2
	 * @return
	 */
	public List<CountriesApiHelper> formatCountries() {
		List<CountriesApiHelper> countries = restClient.get()
				.uri(
						uriBuilder -> uriBuilder
						.path(REST_COUNTRIES_BASE_URL + "/all")
						.queryParam("fields", List.of("name,cca2"))
						.build()
				)
				.header("Content-Type", "application/json")
				.retrieve()
				.body(new ParameterizedTypeReference<List<CountriesApiHelper>>() {});
				
		System.out.println("Countries: " + countries);
		return countries;
	}
	
	/**
	 * @GET request for 2 letter country code
	 * @return
	 */
	public Set<Map<String, String>> getTwoLetterCountryCode() {
		return restClient.get()
				.uri(REST_COUNTRIES_BASE_URL + "/all?fields=cca2")
				.retrieve()
				.body(new ParameterizedTypeReference<Set<Map<String, String>>>() {});
	}
}
