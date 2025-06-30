package core.hubby.backend;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import core.hubby.backend.business.controller.OrganizationController;
import core.hubby.backend.business.dto.param.CreateOrganizationDTO;
import core.hubby.backend.business.dto.vo.OrganizationVO;
import core.hubby.backend.business.services.OrganizationService;

@WebMvcTest(OrganizationController.class)
public class HttpRequestTest {
	
	@MockitoBean
	private OrganizationService organizationService;
	
	@Test
	void greetingShouldReturnTheDefaultMessage() throws Exception {
		Map<String, String> address = new HashMap<>();
		address.put("PhysicalAddressCity", "philippines");
		address.put("PhysicalAddressStreet", "Negros Oriental");
		
		Map<String, String> taxDetails = new HashMap<>();
		taxDetails.put("taxIdNo", "TIN");
		taxDetails.put("taxBasis", "");
		taxDetails.put("taxtPeriod", "");
		
		Map<String, String> financialSetting = new HashMap<>();
		financialSetting.put("defaultCurrency", "PH");
		financialSetting.put("timeZone", "PST");
		
//		CreateOrganizationDTO data = new CreateOrganizationDTO("name", "legal_name", "trading_name", "country", null, "phone_number", "sample123@sample.com", address, taxDetails, financialSetting);
//		when(organizationService.createOrganization(data))
//		.thenReturn(new OrganizationVO(null, null, null, null, null, null, null));
	}
}
