package core.hubby.backend.core.helper;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;

// TODO work and improve this countries API and TEST
@Component
public class CountriesApiHelper {
	@Value("${spring.app.details.api.website}")
	private String REST_COUNTRIES_BASE_URL;
	private final RestClient restClient;
	private String field;
	private String uri;
	
	
	public CountriesApiHelper(RestClient.Builder clientBuilder, String field, String uri) {
		this.field = field;
		this.uri = uri;
		this.restClient = clientBuilder
				.baseUrl(URI.create(REST_COUNTRIES_BASE_URL))
				.defaultUriVariables(Map.of("fields", this.field))
				.build();
	}
	
	// Format countries
	public List<JsonNode> formatCountries() {
		List<JsonNode> countries = restClient.get()
				.uri(URI.create(this.uri))
				.retrieve()
				.body(new ParameterizedTypeReference<List<JsonNode>>() {});
				
		System.out.println("Countries: " + countries);
		return countries;
	}
	
	// Get the two letter country code
	public Set<String> getTwoLetterCountryCode() {
		return restClient.get()
				.uri(URI.create(this.uri))
				.retrieve()
				.body(new ParameterizedTypeReference<Set<String>>() {});
	}
}
