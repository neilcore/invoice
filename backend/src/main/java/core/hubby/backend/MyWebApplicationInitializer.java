package core.hubby.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

@Configuration
public class MyWebApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("spring.profiles.active", "dev");
		
	}

}
