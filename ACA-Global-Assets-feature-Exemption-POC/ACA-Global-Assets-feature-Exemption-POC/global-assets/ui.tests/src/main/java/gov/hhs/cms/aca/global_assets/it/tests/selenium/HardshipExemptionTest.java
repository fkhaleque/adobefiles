package gov.hhs.cms.aca.global_assets.it.tests.selenium;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;



public class HardshipExemptionTest extends BaseWebdriverTest{

	public HardshipExemptionTest(String browser) {
		super(browser);
	}

	@Test
	public void test1() throws Exception {
		driver.get("http://cms-test.adobecqms.net/content/forms/af/cms-exemptions/Hardship.html");

		WebElement privacy_modal_header = driver.findElement(By.id("modalLabel_pii"));
		assertEquals(privacy_modal_header.getText(), "Note on privacy and information security");

		Thread.sleep(1000); // Let the user actually see something!

		WebElement privacy_modal_button = driver.findElement(By.id("piiok"));
		privacy_modal_button.click();

		Thread.sleep(1000); // Let the user actually see something!

		WebElement continue_button = driver.findElement(By.id("guideContainer-rootPanel-toolbar-continue___widget"));
		continue_button.click();

		Thread.sleep(1000); // Let the user actually see something!
	}

}
