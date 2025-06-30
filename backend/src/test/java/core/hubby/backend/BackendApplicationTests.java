package core.hubby.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import core.hubby.backend.auth.controller.AuthController;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BackendApplicationTests {
	
	@Autowired
	private AuthController authController;
	
	@Test
	void contextLoads() {
		assertThat(authController).isNotNull();
	}

}
