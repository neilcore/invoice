package core.hubby.backend.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class UtilConfiguration {
	
	/**
	 * A configuration bean for RestClient
	 * @param baseUrl - contains the base URL
	 * @return
	 */
	@Bean
	public RestClient restClient(
			@Value("${spring.app.details.api.website}") String baseUrl
	) {
		return RestClient.builder()
				.baseUrl(baseUrl)
				.build();
	}
}
