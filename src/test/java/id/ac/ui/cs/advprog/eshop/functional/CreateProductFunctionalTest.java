package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {
    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}.
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d/product/list", testBaseUrl, serverPort);
    }

    @Test
    void createProductPage_isAccessible(ChromeDriver driver) throws Exception {
        // Exercise
        driver.get(baseUrl);
        WebElement createButton = driver.findElement(By.linkText("Create Product"));
        createButton.click();

        String createProductTitle = driver.getTitle();

        // Verify
        assertEquals("Create New Product", createProductTitle);
    }

    @Test
    void createProduct_isCorrect(ChromeDriver driver) throws Exception {
        // Exercise
        driver.get(baseUrl);
        WebElement createButton = driver.findElement(By.linkText("Create Product"));
        createButton.click();

        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        WebElement submitButton = driver.findElement(By.className("btn"));

        String name = "Test";
        int quantity = 5;

        nameInput.sendKeys(name);
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));
        submitButton.click();

        // To check
        String productListTitle = driver.getTitle();
        List<WebElement> productTable = driver.findElements(By.tagName("td"));
        String firstProductName = productTable.get(0).getText();
        String firstProductQuantity = productTable.get(1).getText();

        // Verify
        assertEquals("Product List", productListTitle);
        assertEquals(firstProductName, name);
        assertEquals(firstProductQuantity, String.valueOf(quantity));
    }
}
