package gov.hhs.cms.aca.global_assets.it.tests.selenium;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

@RunWith(Parameterized.class)
public class BaseWebdriverTest {

	protected WebDriver driver;
	protected String browser;

	public BaseWebdriverTest(String browser) {
		this.browser = browser;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("webdriver.chrome.driver", "C:/Software/selenium/chromedriver.exe");
		System.setProperty("webdriver.ie.driver", "C:/Software/selenium/IEDriverServer.exe");
		System.setProperty("webdriver.gecko.driver", "C:/Software/selenium/geckodriver.exe");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		switch (browser) {
		case "Chrome":
			driver = new ChromeDriver();
			break;
		case "Firefox":
			driver = new MarionetteDriver();
			break;
		case "InternetExplorer":
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();    
		    caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);    
			driver = new InternetExplorerDriver(caps);
			break;
		}
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
	
	@Parameters
    public static Collection<Object[]> data() {
    	
    	Object[][] dataStrings =  new Object[][] {
                {"Chrome" }/*, 
                {"Firefox" },
                {"InternetExplorer"}*/
          };
    	
        return Arrays.asList(dataStrings);
    }

}
