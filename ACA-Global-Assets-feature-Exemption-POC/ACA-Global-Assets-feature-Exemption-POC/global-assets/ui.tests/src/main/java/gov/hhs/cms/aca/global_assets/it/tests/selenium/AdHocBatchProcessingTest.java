package gov.hhs.cms.aca.global_assets.it.tests.selenium;

import java.awt.Robot;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;




public class AdHocBatchProcessingTest extends BaseWebdriverTest {	

	private WebDriverHelpers webDriverHelpers = null;

	String uuid = UUID.randomUUID().toString();

	public AdHocBatchProcessingTest(String browser) {
		super(browser);

		webDriverHelpers = new WebDriverHelpers(browser);

		webDriverHelpers.writeLog("Auto testing suite initialize",00,SeleniumConstants.CONFIG, uuid);
		webDriverHelpers.writeLog("SeleniumConstants.CONFIG settings dump",00,SeleniumConstants.CONFIG, uuid);
		webDriverHelpers.writeLog("Target site: "+SeleniumConstants.loginPageString,00,SeleniumConstants.CONFIG, uuid);
		webDriverHelpers.writeLog("AEM login required: "+SeleniumConstants.AEMloginRequired,00,SeleniumConstants.CONFIG, uuid);
		webDriverHelpers.writeLog("Use auto detect test files: "+SeleniumConstants.useAutoPopulatedLists,00,SeleniumConstants.CONFIG, uuid);
		if(SeleniumConstants.useAutoPopulatedLists){
			webDriverHelpers.writeLog("Test file location: "+SeleniumConstants.pathToTests,00,SeleniumConstants.CONFIG, uuid);
		}
		webDriverHelpers.writeLog("Verbose debugging: "+SeleniumConstants.verboseDebugging, 00, SeleniumConstants.CONFIG, uuid);
	}




	@Test
	public void test1() throws Exception {
		/* *****************************************
		 * Test:	0001
		 * Author:	William Bowdridge
		 * Purpose:	Verify successful login
		 * *****************************************/
		//enter login details
		//click log in button
		//end test
		int tempTestNumber=1;
		System.out.println("TEST 1");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		driver.get(SeleniumConstants.loginPageString);

		Thread.sleep(1000); // Let the user actually see something!
		if(SeleniumConstants.AEMloginRequired){
			webDriverHelpers.logInToAEM(0, uuid);//log in to AEM
		}
		webDriverHelpers.logInToDashboard(0, uuid);

		webDriverHelpers.takeScreenshot(1, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test4() throws Exception {
		/* *****************************************
		 * Test:	0004
		 * Author:	William Bowdridge
		 * Purpose:	Verify successful logout
		 * *****************************************/
		//enter login details
		//click login button
		//click logout button
		//end test
		int tempTestNumber=4;
		System.out.println("TEST 4");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		driver.get(SeleniumConstants.loginPageString);
		if(SeleniumConstants.AEMloginRequired){
			webDriverHelpers.logInToAEM(0, uuid);//log in to AEM
		}
		webDriverHelpers.logInToDashboard(0, uuid);

		Thread.sleep(1000); // Let the user actually see something!
		WebElement logout = driver.findElement(By.xpath("//a[@href='/bin/logout']"));
		logout.click();

		webDriverHelpers.takeScreenshot(4, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test8() throws Exception {
		/* *****************************************
		 * Test:	0008
		 * Author:	William Bowdridge
		 * Purpose:	Verify successful XLSX file upload via file browsing
		 * *****************************************/
		//login procedure
		//switch to upload tab
		//click upload file button
		//wait for open file dialog
		//type in name of test file, in this test the file is an XLSX type
		//press enter to accept file
		//click submit file button
		//switch to batch tab 
		//check that file uploaded correctly
		int tempTestNumber=8;
		System.out.println("TEST 8");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("Adobe Test Excel 9 Records.xlsx", 0, uuid);//upload file method also logs into AEM and Dashboard
		System.out.println("Initiating implicit wait");
		webDriverHelpers.writeLog("Initiating implicit wait", tempTestNumber, 4, uuid);
		try{
			driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);//wait for a thing, if the thing isn't there wait some more
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", tempTestNumber, -1, uuid);
			webDriverHelpers.writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1, uuid);
		}
		System.out.println("Implicit wait success");
		webDriverHelpers.writeLog("Implicit wait success", tempTestNumber, 4, uuid);
		webDriverHelpers.takeScreenshot(8, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test10() throws Exception {
		/* *****************************************
		 * Test:	0010
		 * Author:	William Bowdridge
		 * Purpose:	Verify successful CSV file upload via file browsing
		 * *****************************************/
		int tempTestNumber=10;
		System.out.println("TEST 10");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("ACA_TEST_0_2.CSV", 0, uuid);
		//scroll page down
		webDriverHelpers.scrollpage(1, 0, uuid);
		webDriverHelpers.takeScreenshot(10, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test12() throws Exception {
		/* *****************************************
		 * Test:	0012
		 * Author:	William Bowdridge
		 * Purpose:	Verify successful upload of several files via file browsing
		 * *****************************************/
		int tempTestNumber=12;
		System.out.println("TEST 12");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);

		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("\"ACA_TEST_0_2.CSV\" \"ACA_TEST_0_5.CSV\" \"ACA_TEST_0_10.CSV\"", 0, uuid);
		
		Thread.sleep(500);
		//take screenshot
		webDriverHelpers.takeScreenshot(12, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}
	@Test
	public void test15() throws Exception {
		/* *****************************************
		 * Test:	0015
		 * Author:	William Bowdridge
		 * Purpose:	Verify deleting a batch after extraction
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=15;
		System.out.println("TEST 15");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		
		//String name="";
		if(SeleniumConstants.debugLevel>=3){webDriverHelpers.writeLog("Instantiating robot",tempTestNumber,0, uuid);}
		Robot robot = new Robot();// for key press simulation

	
			webDriverHelpers.login(0, uuid);
			webDriverHelpers.uploadFile("ACA_TEST_0_2.CSV", 0, uuid);//"\"ACA_TEST_0_2.CSV\" \"ACA_TEST_0_5.CSV\" \"ACA_TEST_0_10.CSV\""
	
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		//wait
		webDriverHelpers.waitForDeleteButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", tempTestNumber, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[6]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
					webDriverHelpers.writeLog("Wait timeout exception", tempTestNumber, -1);
					webDriverHelpers.writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1);
				}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", tempTestNumber, 4);
		 */
		webDriverHelpers.clickDeleteButton(numberOfRows);
		/*
		WebElement deleteButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[6]"));
		deleteButton.click();
		 */
		Thread.sleep(500);
		//robot will hit the accept button on the dialog popup
		robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
		robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);

		//take screenshot
		webDriverHelpers.takeScreenshot(15, uuid);

		//source: http://www.seleniumhq.org/docs/04_webdriver_advanced.jsp#remotewebdriver
		/*
		WebDriver augmentedDriver = new Augmenter().augment(driver);
        File screenshot = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
		 */


		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test16() throws Exception {
		/* *****************************************
		 * Test:	0016
		 * Author:	William Bowdridge
		 * Purpose:	Verify error when uploading invalid file type
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=16;
		System.out.println("TEST 16");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("Invalid File Jpeg.xlsx", 0, uuid);
		//detect stall - the word stall will appear in the row batch row
		//example row: <tr id="batch-160620125225305" data-old="0">
		//<td class="berrors">1</td> will be greater than 0
		//an error button will also appear, class=errors, value="Errors"
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForErrorButton(numberOfRows, uuid);
		/*		
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", tempTestNumber, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)	  
			@SuppressWarnings("unused")//TODO this test failed								  
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[5]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", tempTestNumber, -1);
			webDriverHelpers.writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", tempTestNumber, 4);
		 */
		//find the last entry's error button, the last entry being the most recently added file
		webDriverHelpers.clickErrorButton(numberOfRows);
		/*
		WebElement errorButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[5]"));
		errorButton.click();
		 */
		Thread.sleep(500);//it takes a second(roughly) to load the errors
		//scroll page down
		webDriverHelpers.scrollpage(2, 0, uuid);
		//TODO check error message
		//parse the string from xpath: /html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[3]/div/div[2]/table/tbody/tr/td[4]

		//take screenshot
		webDriverHelpers.takeScreenshot(16, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}	

	@Test
	public void test18() throws Exception {
		/* *****************************************
		 * Test:	0018
		 * Author:	William Bowdridge
		 * Purpose:	Verify error when uploading file with SQL injection
		 * Addendum:Does not produce error after version 1.0.7
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=18;
		System.out.println("TEST 18");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("Contains SQL.CSV", 0, uuid);
		//wait for file to process
		//check if it stalled
		//open preview
		//check if the injection made it in the PDF
		//end test
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", tempTestNumber, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", tempTestNumber, -1);
			webDriverHelpers.writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", tempTestNumber, 4);
		 */
		//find the last entry's preview button, the last entry being the most recently added file
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]"));
		previewButton.click();
		 */
		Thread.sleep(500);//it takes a second(roughly) to load the errors
		//scroll page down
		webDriverHelpers.scrollpage(2, 0, uuid);
		//TODO check error message

		//take screenshot
		webDriverHelpers.takeScreenshot(18, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}	

	@Test
	public void test19() throws Exception {
		/* *****************************************
		 * Test:	0019
		 * Author:	William Bowdridge
		 * Purpose:	Upload file containing javascript
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=19;
		System.out.println("TEST 19");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("Contains Javascript.CSV", 0, uuid);
		//wait for file to process
		//check if it stalled
		//open preview
		//check if the injection made it in the PDF
		//end test
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", tempTestNumber, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", tempTestNumber, -1);
			webDriverHelpers.writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", tempTestNumber, 4);
		 */
		//find the last entry's preview button, the last entry being the most recently added file
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]"));
		previewButton.click();
		 */
		webDriverHelpers.clickPreviewButton(numberOfRows);
		Thread.sleep(500);//it takes a second(roughly) to load the errors
		//scroll page down
		//webDriverHelpers.scrollpage(2);/html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[1]/div/table/tbody/tr[73]/td[9]/input[2]
		webDriverHelpers.waitForPreviewPDF(1, uuid);//wait for the first pdf button
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", tempTestNumber, 4);
		//explicit wait
		try{
			//wait for PDF view button to load
			@SuppressWarnings("unused")
			WebElement pdfButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/div[2]/table/tbody/tr[1]/td[7]/a/input")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", tempTestNumber, -1);
			webDriverHelpers.writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1);
		}
		System.out.println("Explicit wait Success");
		webDriverHelpers.writeLog("Explicit wait success", tempTestNumber, 4);
		 */
		//look at PDF
		webDriverHelpers.clickPreviewPDF(1);//click the first pdf button
		/*
		WebElement generatePDF = driver.findElement(By.xpath(rootXPath+"/div[2]/table/tbody/tr[1]/td[7]/a/input"));
		generatePDF.click();
		 */
		Thread.sleep(2000);//grace split second
		//screenshot
		webDriverHelpers.scrollpage(1, 0, uuid);//scroll to middle of pdf
		webDriverHelpers.takeScreenshot(19, uuid);

		//close tab
		webDriverHelpers.closeTab(0, uuid);
		/*
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		Thread.sleep(2000);//grace split second
		webDriverHelpers.writeLog("Closing tab", tempTestNumber, 0);
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		 */
		//take screenshot
		webDriverHelpers.takeScreenshot(19, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test20() throws Exception {
		/* *****************************************
		 * Test:	0020
		 * Author:	William Bowdridge
		 * Purpose:	Verify preview PDF
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=20;
		System.out.println("TEST 20");
		webDriverHelpers.writeLog("TEST 20 START", 20, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("Adobe Test Excel 9 Records.xlsx", 0, uuid);
		//wait for file to process
		//check if it stalled
		//open preview
		//check if data entered into the PDF

		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//grace split second
		//"//tr[contains(@id, 'batch-')]"
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 20, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 20, -1);
			webDriverHelpers.writeLog("TEST 20 ERROR FAIL", 20, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 20, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]"));
		previewButton.click();
		 */
		webDriverHelpers.waitForPreviewPDF(1, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 20, 4);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement waitElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/div[2]/table/tbody/tr[1]/td[7]/a/input")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 20, -1);
			webDriverHelpers.writeLog("TEST 20 ERROR FAIL", 20, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 20, 4);
		 */
		webDriverHelpers.clickPreviewPDF(1);
		/*
		WebElement generatePDF = driver.findElement(By.xpath(rootXPath+"/div[2]/table/tbody/tr[1]/td[7]/a/input"));
		generatePDF.click();
		 */
		Thread.sleep(5000);//let PDF load

		//take screenshot
		webDriverHelpers.takeScreenshot(20, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test21() throws Exception {
		/* *****************************************
		 * Test:	0021
		 * Author:	William Bowdridge
		 * Purpose:	Verify preview pagination with a file that has 5 records
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=21;
		System.out.println("TEST 21");
		webDriverHelpers.writeLog("TEST 21 START", 21, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("ACA_TEST_0_5.CSV", 0, uuid);
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 21, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 21, -1);
			webDriverHelpers.writeLog("TEST 21 ERROR FAIL", 21, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 21, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4] "));
		previewButton.click();
		 */
		Thread.sleep(500);//let it load
		//check each level of pagination: 25, 50, 100

		//scroll page down
		webDriverHelpers.scrollpage(2, 0, uuid);

		//check each of the pagination values.
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("25");
		Thread.sleep(500);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("50");
		Thread.sleep(500);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("100");
		Thread.sleep(500);
		//take screenshot
		webDriverHelpers.takeScreenshot(21, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test22() throws Exception {
		/* *****************************************
		 * Test:	0022
		 * Author:	William Bowdridge
		 * Purpose:	Verify preview pagination with a file that has 25 records
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=22;
		System.out.println("TEST 22");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("ACA_TEST_0_25.CSV", 0, uuid);
		//check each level of pagination: 25, 50, 100
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", tempTestNumber, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 22, -1);
			webDriverHelpers.writeLog("TEST 22 ERROR FAIL", 22, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 22, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4] "));
		previewButton.click();
		 */
		Thread.sleep(500);//let it load
		//check each level of pagination: 25, 50, 100
		//scroll page down
		webDriverHelpers.scrollpage(2, 0, uuid);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("25");
		Thread.sleep(500);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("50");
		Thread.sleep(500);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("100");
		Thread.sleep(500);
		//take screenshot
		webDriverHelpers.takeScreenshot(22, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test23() throws Exception {
		/* *****************************************
		 * Test:	0023
		 * Author:	William Bowdridge
		 * Purpose:	Verify preview pagination with a file that has 50 records
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=23;
		System.out.println("TEST 23");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("Adobe Test Excel 50 Records.xlsx", 0, uuid);
		//check each level of pagination: 25, 50, 100
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, 23, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 23, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 23, -1);
			webDriverHelpers.writeLog("TEST 23 ERROR FAIL", 23, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 23, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4] "));
		previewButton.click();
		 */
		Thread.sleep(500);//let it load
		//check each level of pagination: 25, 50, 100
		//scroll page down
		webDriverHelpers.scrollpage(2, 0, uuid);
		//probably want to take screenshots for each of these
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("25");
		Thread.sleep(500);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("50");
		Thread.sleep(500);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("100");
		Thread.sleep(500);
		//take screenshot
		webDriverHelpers.takeScreenshot(23, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test24() throws Exception {
		/* *****************************************
		 * Test:	0024
		 * Author:	William Bowdridge
		 * Purpose:	Verify preview pagination with a file that has 100 records
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=24;
		System.out.println("TEST 24");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("Adobe Test Excel 100 Records.xlsx", 0, uuid);
		//check each level of pagination: 25, 50, 100
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);	
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 24, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 24, -1);
			webDriverHelpers.writeLog("TEST 24 ERROR FAIL", 24, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 24, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4] "));
		previewButton.click();
		 */
		Thread.sleep(500);//let it load
		//check each level of pagination: 25, 50, 100
		//scroll page down
		webDriverHelpers.scrollpage(2, 0, uuid);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("25");
		Thread.sleep(500);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("50");
		Thread.sleep(500);
		new Select(driver.findElement(By.name("batchpreview-table_length"))).selectByVisibleText("100");
		Thread.sleep(500);
		//take screenshot
		webDriverHelpers.takeScreenshot(24, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test25() throws Exception {
		/* *****************************************
		 * Test:	0025
		 * Author:	William Bowdridge
		 * Purpose:	Verify preview list search
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=25;
		System.out.println("TEST 25");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		//upload file with 100 records
		webDriverHelpers.uploadFile("Adobe Test Excel 100 Records.xlsx", 0, uuid);
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//grace split second
		//"//tr[contains(@id, 'batch-')]"
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 25, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 25, -1);
			webDriverHelpers.writeLog("TEST 25 ERROR FAIL", 25, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 25, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]"));
		previewButton.click();
		 */
		//enter text into search field
		WebElement searchfield = driver.findElement(By.xpath("//input[@type='search']"));
		Thread.sleep(500);//grace split second
		searchfield.sendKeys("one");

		//take screenshot
		webDriverHelpers.takeScreenshot(25, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test27() throws Exception {
		/* *****************************************
		 * Test:	0027
		 * Author:	William Bowdridge
		 * Purpose:	Verify correct invalid character handling functionality
		 * *****************************************/

		int numberOfRows=0;
		int tempTestNumber=27;
		System.out.println("TEST 27");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		//upload file with invalid characters
		webDriverHelpers.uploadFile("SpecialCharactersTestingInvalidContactName.xlsx", 0, uuid);
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//grace split second
		//"//tr[contains(@id, 'batch-')]"
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 27, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 27, -1);
			webDriverHelpers.writeLog("TEST 27 ERROR FAIL", 27, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 27, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]"));
		previewButton.click();
		 */
		//TODO parse PDF and progromatically check that it worked
		//take screenshot
		webDriverHelpers.takeScreenshot(27, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test28() throws Exception {
		/* *****************************************
		 * Test:	0028
		 * Author:	William Bowdridge
		 * Purpose:	verify print order with valid invalid characters
		 * *****************************************/

		//print file
		//send email with the batch number
		//wait for email with the results
		int numberOfRows=0;
		int tempTestNumber=28;
		System.out.println("TEST 28");
		webDriverHelpers.writeLog("TEST 28 START", 28, 0, uuid);
		webDriverHelpers.login(0, uuid);
		//upload file with invalid characters
		webDriverHelpers.uploadFile("SpecialCharactersTestingValidContactName.xlsx", 0, uuid);
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//grace split second
		//"//tr[contains(@id, 'batch-')]"
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 28, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 28, -1);
			webDriverHelpers.writeLog("TEST 28 ERROR FAIL", 28, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 28, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]"));
		previewButton.click();
		 */
		//TODO check PDF
		//switch tabs and close PDF view
		//hit print button
		//output printed batch number
		//take screenshot
		webDriverHelpers.takeScreenshot(28, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test29() throws Exception {
		/* *****************************************
		 * Test:	0029
		 * Author:	William Bowdridge
		 * Purpose:	Verify XLSX files with dates preceding 1927 do not produce errors
		 * *****************************************/



		int numberOfRows=0;
		int tempTestNumber=29;
		System.out.println("TEST 29");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		//upload file with invalid characters
		webDriverHelpers.uploadFile("4 Records_Invalid_DateTesting.xlsx", 0, uuid);
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//grace split second
		//"//tr[contains(@id, 'batch-')]"
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 29, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 29, -1);
			webDriverHelpers.writeLog("TEST 29 ERROR FAIL", 29, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 29, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]"));
		previewButton.click();
		 */
		//TODO check PDF
		Thread.sleep(500);//grace split second
		webDriverHelpers.scrollpage(1, 0, uuid);
		//take screenshot
		webDriverHelpers.takeScreenshot(29, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test30() throws Exception {
		/* *****************************************
		 * Test:	0030
		 * Author:	William Bowdridge
		 * Purpose:	Verify PDF footer contains a valid web address and the link is clickable
		 * *****************************************/

		int numberOfRows=0;
		int tempTestNumber=30;
		System.out.println("TEST 30");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		//upload file with invalid characters
		webDriverHelpers.uploadFile("ACA_TEST_0_10.CSV", 0, uuid);
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//grace split second
		//"//tr[contains(@id, 'batch-')]"
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 30, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 30, -1);
			webDriverHelpers.writeLog("TEST 30 ERROR FAIL", 30, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 30, 4);
		 */
		//click preview
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]"));
		previewButton.click();
		 */
		webDriverHelpers.waitForPreviewPDF(1, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 30, 4);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement waitElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/div[2]/table/tbody/tr[1]/td[7]/a/input")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 30, -1);
			webDriverHelpers.writeLog("TEST 30 ERROR FAIL", 30, -1);
		}
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 30, 4);
		 */
		webDriverHelpers.clickPreviewPDF(1);
		/*
		WebElement generatePDF = driver.findElement(By.xpath(rootXPath+"/div[2]/table/tbody/tr[1]/td[7]/a/input"));
		generatePDF.click();
		 */
		Thread.sleep(5000);//let PDF load
		webDriverHelpers.scrollpage(2, 0, uuid);//scroll to footer
		Thread.sleep(500);//grace split second
		//take screenshot
		webDriverHelpers.takeScreenshot(30, uuid);
		//TODO check PDF
		//verify link
		//robot press ctrl + A
		//robot press ctrl + C
		//java write clipboard to String
		//java parse String
		//save things that look like web addresses ie contains(".gov")
		//web driver driver.get(address)

		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}
	//@Test
	public void test31() throws Exception {
		/* *****************************************
		 * Test:	0031
		 * Author:	William Bowdridge
		 * Purpose:	Verify Site Links
		 * Status:	currently unimplemented
		 * *****************************************/
		int tempTestNumber=31;
		System.out.println("TEST 31");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("", 0, uuid);
		//TODO verify that this is a required test
		//take screenshot
		webDriverHelpers.takeScreenshot(31, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test407() throws Exception {
		/* *****************************************
		 * Test:	0407
		 * Author:	William Bowdridge
		 * Purpose:	Verify appropriate error message when a record is uploaded with an invalid column length
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=407;
		System.out.println("TEST 407");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("Invalid First Name Exceeds Character Limit.CSV", 0, uuid);
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//grace split second
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForErrorButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 407, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for error button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[5]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 407, -1);
			webDriverHelpers.writeLog("TEST 407 ERROR FAIL", 407, -1);
		}
		Thread.sleep(2000);//grace split second
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 407, 4);
		 */
		webDriverHelpers.clickErrorButton(numberOfRows);
		/*
		WebElement errorButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[5]"));//error button = input[5]
		errorButton.click();
		 */
		Thread.sleep(2000);//grace split second
		webDriverHelpers.scrollpage(1, 0, uuid);//scroll to error message
		Thread.sleep(2000);//grace split second
		//take screenshot
		webDriverHelpers.takeScreenshot(407, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		//TODO check error message progromatically
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test408() throws Exception {
		/* *****************************************
		 * Test:	0408
		 * Author:	William Bowdridge
		 * Purpose:	Verify appropriate error message when uploaded file contains missing mandatory column
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=408;
		System.out.println("TEST 408");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		/**required columns:
		 * ApplicationId
		 * EmployerName
		 * StreetName1
		 * CityName
		 * State
		 * ZipCode
		 * PersonFirstName
		 * PersonLastName
		 * */
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("missing madatory fields 13 Records.xlsx", 0, uuid);
		//xpath to error button /html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[1]/div/table/tbody/tr[numberOfRows]/td[9]/input[5]
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//grace split second
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForStartButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", tempTestNumber, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for start batch button of the last element(last thing uploaded)
			@SuppressWarnings("unused")//TODO this test failed
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[1]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", tempTestNumber, -1);
			webDriverHelpers.writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1);
		}
		Thread.sleep(2000);//grace split second
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", tempTestNumber, 4);
		 */
		webDriverHelpers.clickStartButton(numberOfRows);
		/*
		WebElement startButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[1]"));
		startButton.click();
		 */
		Thread.sleep(2000);//grace split second
		webDriverHelpers.waitForErrorButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 408, 4);
		//explicit wait
		try{
			//set test to wait for error button of the last element(last thing uploaded)
			@SuppressWarnings("unused")//TODO this test failed
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[5]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 408, -1);
			webDriverHelpers.writeLog("TEST 408 ERROR FAIL", 408, -1);
		}
		Thread.sleep(2000);//grace split second
		System.out.println("Explicit wait success");
		webDriverHelpers.writeLog("Explicit wait success", 408, 4);
		 */
		webDriverHelpers.clickErrorButton(numberOfRows);
		/*
		WebElement errorButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[5]"));
		errorButton.click();
		 */
		Thread.sleep(1000);//grace split second
		webDriverHelpers.scrollpage(1, 0, uuid);//scroll to error message
		Thread.sleep(1000);//grace split second
		//take screenshot
		webDriverHelpers.takeScreenshot(408, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		//TODO check error message progromatically
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test501() throws Exception {
		/* *****************************************
		 * Test:	0501
		 * Author:	William Bowdridge
		 * Purpose:	Verify file upload, preview, and can be processed
		 * *****************************************/
		//Keep an eye on this, there is a tendancy towards false negatives
		//TODO fix up and verify
		//upload
		//wait
		//click preview
		//wait
		//check for errors
		//check that batches processed
		//click preview batch
		//screenshot
		//close tab
		//start batch
		//wait
		//screenshot
		//compare pdf values with extracted values
		String appId;
		String firstName;
		String lastName;
		String city;
		String state;
		String zipCode;

		int numberOfRows=0;
		int tempTestNumber=501;
		System.out.println("TEST 501");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("ACA_TEST_1_5.CSV", 0, uuid);
		//start batch xpath: /html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[1]/div/table/tbody/tr["+numberOfRows+"]/td[9]/input[1]
		//xpath to error button /html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[1]/div/table/tbody/tr[numberOfRows]/td[9]/input[5]

		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//was producing errors at 500 ms
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are, note not zero delimited
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		webDriverHelpers.waitForPreviewButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 501, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 501, -1);
			webDriverHelpers.writeLog("TEST 501 ERROR FAIL", 501, -1);
		}
		Thread.sleep(2000);//grace split second
		System.out.println("Explicit wait Success");
		webDriverHelpers.writeLog("Explicit wait success", 501, 4);
		 */
		webDriverHelpers.clickPreviewButton(numberOfRows);
		/*
		WebElement previewButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[4]"));
		previewButton.click();
		 */
		webDriverHelpers.waitForPreviewPDF(1, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 501, 4);
		try{
			//look at PDF
			@SuppressWarnings("unused")
			WebElement pdfButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/div[2]/table/tbody/tr[1]/td[7]/a/input")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", 501, -1);
			webDriverHelpers.writeLog("TEST 501 ERROR FAIL", 501, -1);
		}
		System.out.println("Explicit wait Success");
		webDriverHelpers.writeLog("Explicit wait success", 501, 4);
		 */

		//read the ApplicationId, FirstName, LastName, City, State, ZipCode
		//store fields
		//WebElement appIdElement = driver.findElement(By.xpath(rootXPath+"/div[2]/table/tbody/tr[1]/td[1]"));
		//appId=appIdElement.getAttribute("value");
		//System.out.println(appId);
		//firstName;/html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[2]/table/tbody/tr[1]/td[2]
		//lastName;/html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[2]/table/tbody/tr[1]/td[3]
		//city;/html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[2]/table/tbody/tr[1]/td[4]
		//state;/html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[2]/table/tbody/tr[1]/td[5]
		//zipCode;/html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[2]/table/tbody/tr[1]/td[6]



		//then compare to values in the PDF
		webDriverHelpers.clickPreviewPDF(1);
		/*
		WebElement generatePDF = driver.findElement(By.xpath(rootXPath+"/div[2]/table/tbody/tr[1]/td[7]/a/input"));
		generatePDF.click();
		 */
		Thread.sleep(2000);//grace split second
		//screenshot
		webDriverHelpers.scrollpage(1, 0, uuid);//scroll to middle of pdf
		webDriverHelpers.takeScreenshot(501, uuid);

		//close tab
		webDriverHelpers.closeTab(0, uuid);
		/*
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		Thread.sleep(2000);//grace split second
		webDriverHelpers.writeLog("Closing tab", 501, 0);
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		 */
		Thread.sleep(2000);//grace split second
		//@SuppressWarnings("unused")
		//WebElement startbatchElementButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[1]")));
		webDriverHelpers.clickStartButton(numberOfRows);
		/*
		WebElement startBatchButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[1]"));
		startBatchButton.click();
		 */
		//wait for batch to finish TODO
		webDriverHelpers.waitForPrintButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", 501, 4);
		System.out.println("Explicit wait Success");
		webDriverHelpers.writeLog("Explicit wait success", 501, 4);
		@SuppressWarnings("unused")
		WebElement printButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[2]"));
		 */
		Thread.sleep(2000);//grace split second

		//take screenshot
		webDriverHelpers.takeScreenshot(501, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

	@Test
	public void test601() throws Exception {
		/* *****************************************
		 * Test:	0601
		 * Author:	William Bowdridge
		 * Purpose:	Verify printing a batch
		 * *****************************************/
		int numberOfRows=0;
		int tempTestNumber=601;
		System.out.println("TEST "+tempTestNumber);
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" START", tempTestNumber, 0, uuid);
		webDriverHelpers.login(0, uuid);
		webDriverHelpers.uploadFile("ACA_TEST_3_2.CSV", 0, uuid);
		//wait for extraction
		//click start batch - class= btn-success, value="Start Batch"
		//wait for processing
		//click print

		//start batch xpath: /html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[1]/div/table/tbody/tr["+numberOfRows+"]/td[9]/input[1]
		//xpath to error button /html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[1]/div/table/tbody/tr[numberOfRows]/td[9]/input[5]
		driver.manage().timeouts().pageLoadTimeout(SeleniumConstants.pageLoadTimeOut, TimeUnit.SECONDS);
		Thread.sleep(5000);//grace split second
		numberOfRows = driver.findElements(By.xpath("//tr[contains(@id,'batch-')]")).size();//determine how many rows there are
		System.out.println("Initial row count: "+numberOfRows);
		webDriverHelpers.writeLog("Initial row count: "+numberOfRows, tempTestNumber, 3, uuid);
		//numberOfRows--;//adjust to match zero delimited lists
		//System.out.println("zero delimited row count: "+numberOfRows);
		webDriverHelpers.waitForStartButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", tempTestNumber, 4);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeOut);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[1]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", tempTestNumber, -1);
			webDriverHelpers.writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1);
		}
		Thread.sleep(2000);//grace split second
		System.out.println("Explicit wait Success");
		webDriverHelpers.writeLog("Explicit wait success", tempTestNumber, 4);
		 */
		webDriverHelpers.clickStartButton(numberOfRows);
		/*
		WebElement startBatchButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[1]"));
		startBatchButton.click();
		 */
		Thread.sleep(2000);//grace split second
		webDriverHelpers.waitForPrintButton(numberOfRows, uuid);
		/*
		System.out.println("Initiating explicit wait");
		webDriverHelpers.writeLog("Initiating explicit wait", tempTestNumber, 4);
		try{
			//set test to wait for preview button of the last element(last thing uploaded)
			@SuppressWarnings("unused")
			WebElement printButtonWait = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[2]")));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			webDriverHelpers.writeLog("Wait timeout exception", tempTestNumber, -1);
			webDriverHelpers.writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1);
		}
		System.out.println("Explicit wait Success");
		webDriverHelpers.writeLog("Explicit wait success", tempTestNumber, 4);
		 */
		webDriverHelpers.clickPrintButton(numberOfRows);
		/*
		WebElement printButton = driver.findElement(By.xpath(rootXPath+"/table/tbody/tr["+numberOfRows+"]/td[9]/input[2]"));
		printButton.click();
		 */
		//TODO find a way to output the batch number
		webDriverHelpers.scrollpage(1, 0, uuid);//scroll to error message

		//take screenshot
		webDriverHelpers.takeScreenshot(tempTestNumber, uuid);
		Thread.sleep(5000); // Let the user actually see something!
		System.out.println("Test Passed");
		webDriverHelpers.writeLog("TEST "+tempTestNumber+" FIN PASS", tempTestNumber, 0, uuid);
	}

}
