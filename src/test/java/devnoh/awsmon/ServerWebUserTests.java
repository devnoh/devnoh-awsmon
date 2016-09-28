package devnoh.awsmon;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by devnoh on 9/22/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ServerWebUserTests {

    private static ChromeDriver browser;

    @LocalServerPort
    private int port;

    @BeforeClass
    public static void openBrowser() {
        //browser = new FirefoxDriver();
        browser = new ChromeDriver();
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

    @Test
    public void testLogin() throws Exception {
        String baseUrl = "http://localhost:" + port;
        browser.get(baseUrl);

        assertEquals(baseUrl + "/login", browser.getCurrentUrl());
        assertEquals("Log In", browser.findElementByClassName("title").getText());
        assertEquals(baseUrl + "/login", browser.getCurrentUrl());

        // Login
        browser.findElementById("username").sendKeys("user");
        browser.findElementById("password").sendKeys("user");
        browser.findElementByTagName("button").submit();

        //System.out.println(browser.findElementById("brand").getText());
        assertTrue(browser.findElementById("brand").getText().contains("AWS Monitor"));
        assertTrue(browser.findElementById("account").getText().contains("Log Out"));

        //browser.get(baseUrl + "/ec2");
    }
}
