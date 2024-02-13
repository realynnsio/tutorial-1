package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EshopApplicationTests {

	@Autowired
	private EshopApplication eshopApplication;

	@Test
	void contextLoads() {
		// Test to ensure the Spring context loads
		assertNotNull(eshopApplication);
	}

}
