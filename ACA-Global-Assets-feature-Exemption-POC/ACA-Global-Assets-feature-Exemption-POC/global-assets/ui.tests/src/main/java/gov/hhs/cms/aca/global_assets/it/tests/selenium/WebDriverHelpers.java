package gov.hhs.cms.aca.global_assets.it.tests.selenium;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverHelpers extends BaseWebdriverTest{

	public WebDriverHelpers(String browser) {
		super(browser);
		// TODO Auto-generated constructor stub
	}
	
	public void logInToAEM(int tempTestNumber, String uuid){
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	Log into AEM
		 * *****************************************/
		System.out.println("logInToAEM");
		writeLog("logInToAEM() START",tempTestNumber,SeleniumConstants.STANDARD, uuid);
		//user name
		if(SeleniumConstants.verboseDebugging){writeLog("logInToAEM() Entering AEM user name",tempTestNumber,SeleniumConstants.STANDARD, uuid);}
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys(SeleniumConstants.userAEM);
		//password
		if(SeleniumConstants.verboseDebugging){writeLog("logInToAEM() Entering AEM password",tempTestNumber,SeleniumConstants.STANDARD, uuid);}
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys(SeleniumConstants.passwordAEM);
		//click submit
		if(SeleniumConstants.verboseDebugging){writeLog("logInToAEM() Submitting user login information",tempTestNumber,SeleniumConstants.STANDARD, uuid);}
		WebElement submit = driver.findElement(By.id("submit-button"));
		submit.click();
		writeLog("logInToAEM() FIN",tempTestNumber,SeleniumConstants.STANDARD, uuid);
		
	}
	public void logInToDashboard(int tempTestNumber, String uuid){
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	Log into test site
		 * *****************************************/
		System.out.println("logInToDashboard");
		writeLog("logInToDashboard() START",tempTestNumber,SeleniumConstants.STANDARD, uuid);
		//user name
		if(SeleniumConstants.verboseDebugging){writeLog("logInToDashboard() Entering dashboard user name",tempTestNumber,SeleniumConstants.STANDARD, uuid);}
		WebElement username = driver.findElement(By.id("login_component_username"));
		username.sendKeys(SeleniumConstants.userDashboard);
		//password
		if(SeleniumConstants.verboseDebugging){writeLog("logInToDashboard() Entering dashboard password",tempTestNumber,SeleniumConstants.STANDARD, uuid);}
		WebElement password = driver.findElement(By.id("login_component_password"));
		password.sendKeys(SeleniumConstants.passwordDashboard);
		if(SeleniumConstants.verboseDebugging){writeLog("logInToDashboard() Submitting user login information",tempTestNumber,SeleniumConstants.STANDARD, uuid);}
		WebElement submit = driver.findElement(By.id("loginSubmit"));
		submit.click();
		writeLog("logInToDashboard() FIN",tempTestNumber,SeleniumConstants.STANDARD, uuid);
	}
	
	void takeScreenshot(int tempTestNumber, String uuid){
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	The robot will take a screenshot of the main monitor
		 * *****************************************/
		String date="";
		System.out.println("takeScreenshot");
		writeLog("takeScreenshot() START",tempTestNumber,2, uuid);
		try {
			if (SeleniumConstants.addDateToScreenShots){
				if(SeleniumConstants.verboseDebugging){writeLog("takeScreenshot() Adding date stamp to screenshot",tempTestNumber,2, uuid);}
				//source: http://stackoverflow.com/questions/23068676/how-to-get-current-timestamp-in-string-format-in-java-yyyy-mm-dd-hh-mm-ss
				date= new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
			}
			System.out.println("Saving screenshot: test_"+tempTestNumber+"_"+uuid+"_"+browser+"_"+date+".bmp");
			writeLog("takeScreenshot() Saving screenshot: test_"+tempTestNumber+"_"+uuid+"_"+browser+"_"+date+".bmp", tempTestNumber, 2, uuid);
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			if(SeleniumConstants.debugLevel>=3){writeLog("takeScreenshot() Instantiating robot",tempTestNumber,2, uuid);}
			BufferedImage capture = new Robot().createScreenCapture(screenRect);
			ImageIO.write(capture, "bmp", new File("test_"+tempTestNumber+"_"+uuid+"_"+browser+"_"+date+".bmp"));
			
		} catch (IOException e) {
			//Auto-generated catch block
			writeLog("takeScreenshot() IO EXCEPTION", tempTestNumber, SeleniumConstants.ERRORCODE, uuid);
			e.printStackTrace();
		} catch (AWTException e) {
			//Auto-generated catch block
			writeLog("takeScreenshot() ROBOT EXCEPTION", tempTestNumber, SeleniumConstants.ERRORCODE, uuid);
			e.printStackTrace();
		}
		writeLog("takeScreenshot() FIN",tempTestNumber,SeleniumConstants.STANDARD, uuid);
	}
	
	void scrollpage(int scrolls, int tempTestNumber, String uuid){
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	The robot will scroll the page with page up and page down
		 * 			positive numbers are page down, negative are page up
		 * *****************************************/
		System.out.println("scrollpage");
		writeLog("scrollpage() START number of scrolls: "+scrolls,tempTestNumber,2, uuid);
		try{
			if(SeleniumConstants.debugLevel>=3){writeLog("scrollpage() Instantiating robot",tempTestNumber,2, uuid);}
			Robot robot = new Robot();// for key press simulation
			if(scrolls>0){
				//scroll down
				for(int i=0; i<scrolls;i++){
					System.out.println("Page Down");
					if(SeleniumConstants.verboseDebugging){writeLog("scrollpage() remaining page down scrolls: "+(scrolls-i),tempTestNumber,2, uuid);}
					robot.keyPress(java.awt.event.KeyEvent.VK_PAGE_DOWN);
					robot.keyRelease(java.awt.event.KeyEvent.VK_PAGE_DOWN);
					robot.delay(100);//lets not go crazy fast
				}
			}
			else if(scrolls<0){
				//scroll up
				for(int i=0; i<scrolls;i++){
					System.out.println("Page Up");
					if(SeleniumConstants.verboseDebugging){writeLog("scrollpage() remaining page up scrolls: "+(scrolls-i),tempTestNumber,2, uuid);}
					robot.keyPress(java.awt.event.KeyEvent.VK_PAGE_UP);
					robot.keyRelease(java.awt.event.KeyEvent.VK_PAGE_UP);
					
					robot.delay(100);//lets not go crazy fast
				}
			}
		}
		catch (AWTException ex)
		{
		    System.err.println("Can't start Robot: " + ex);
		    writeLog("scrollpage() Can't start Robot",tempTestNumber,-1, uuid);
		}
		writeLog("scrollpage() FIN",tempTestNumber,2, uuid);
	}
	
	private void type(String s, int tempTestNumber, String uuid) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		//source http://alvinalexander.com/java/java-robot-class-example-mouse-keystroke
		//
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	The robot will type out words, at human speeds
		 * 			(1)Detects case and applies shift key if necessary
		 * 			(2)Detects special cases: Underscore, Double quote, forward slash
		 * 			(3)Types character into whatever has focus, both key press and key release
		 * 			(4)Releases shift
		 * 			(5)Repeats until end of string
		 * *****************************************/
		System.out.println("type ROBOT_ROBOT_ROBOT");
		writeLog("type() START",tempTestNumber,2, uuid);
		try{
			if(SeleniumConstants.debugLevel>=3){writeLog("type() Instantiating robot",tempTestNumber,2, uuid);}
			Robot robot = new Robot();// for key press simulation
			robot.delay(1500);//wait for dialog pop up, in case of heavy system load increase this
			char[] charArray = s.toCharArray();
	    	for (char c : charArray){

	    		robot.delay(100);
	    		System.out.println("char: "+c);
	    		if(SeleniumConstants.debugLevel>=3){writeLog("type() entering character: "+c,tempTestNumber,2, uuid);}
	    		
	    		@SuppressWarnings("unused")//the following is needed for robot typing
				java.awt.event.KeyEvent keyEvent = null;
	    		
	    		if (Character.isUpperCase(c)) {
	                robot.keyPress(java.awt.event.KeyEvent.VK_SHIFT);
	            }
	    		
	    		if(c == '_'){
	    			robot.keyPress(java.awt.event.KeyEvent.VK_SHIFT);
	    			robot.keyPress(java.awt.event.KeyEvent.VK_MINUS);
	            	robot.keyRelease(java.awt.event.KeyEvent.VK_MINUS);
	            	robot.keyRelease(java.awt.event.KeyEvent.VK_SHIFT);
	    			
	    		}
	    		else if(c == '"'){
	    			robot.keyPress(java.awt.event.KeyEvent.VK_SHIFT);
	    			robot.keyPress(java.awt.event.KeyEvent.VK_QUOTE);
	            	robot.keyRelease(java.awt.event.KeyEvent.VK_QUOTE);
	            	robot.keyRelease(java.awt.event.KeyEvent.VK_SHIFT);
	    			
	    		}
	    		else if(c == '\\'){
	    			//do nothing ignore escape characters
	    		}
	    		else if(c == ':'){
	    			//do nothing
	    		}
	    		else{
	    			//toUppercase returns the keyboard code
	    			robot.keyPress(Character.toUpperCase(c));
	            	robot.keyRelease(Character.toUpperCase(c));
	    		}
	    		//robot.keyPress(KeyPressField.getInt(KeyPressField));
	    		//robot.keyRelease(KeyPressField.getInt(KeyPressField));
	            if (Character.isUpperCase(c)) {
	                robot.keyRelease(java.awt.event.KeyEvent.VK_SHIFT);
	            }

	    	}
		}
		catch (AWTException ex)
		{
		    System.err.println("Can't start Robot: " + ex);
		    writeLog("type() Can't start Robot",tempTestNumber,-1, uuid);
		}
		writeLog("type() FIN",tempTestNumber,2, uuid);
		
	}
	
	void login(int tempTestNumber, String uuid){
		if(SeleniumConstants.debugLevel>=3){writeLog("login() START",tempTestNumber,2, uuid);}
		driver.get(SeleniumConstants.loginPageString);
		
		try {
			Thread.sleep(1000);// Let the user actually see something!
			//(1)
			if(SeleniumConstants.AEMloginRequired){
				logInToAEM(0, uuid);//log in to AEM
			}
			logInToDashboard(0, uuid);// log into dashboard
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Grace split second
		
	}
	
	void uploadFile(String filenameToUpload, int tempTestNumber, String uuid) throws InterruptedException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	The robot will initiate file upload procedure
		 * 			(REFACTORED)Log into AEM and Dashboard
		 * 			(2)Switch to upload tab with hot keys
		 * 			(3)click upload file
		 * 			(4)initiate human-speed typing procedure
		 * 			(5)sends file selection
		 * 			(6)clicks upload file button
		 * 			(7)switches back to batches tab with hot keys
		 * *****************************************/
		String temp = filenameToUpload;
		System.out.println("uploadFile");
		writeLog("uploadFile() START",tempTestNumber,2, uuid);
		try{
			if(SeleniumConstants.debugLevel>=3){writeLog("uploadFile() Instantiating robot",tempTestNumber,2, uuid);}
			Robot robot = new Robot();// for key press simulation
			

			//(2) have the robot type ctrl+alt+u, switches to upload tab
			System.out.println("Switching tabs");
			if(SeleniumConstants.debugLevel>=3){writeLog("uploadFile() Switching to upload screen",tempTestNumber,2, uuid);}
			robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
			robot.keyPress(java.awt.event.KeyEvent.VK_ALT);
			robot.keyPress(java.awt.event.KeyEvent.VK_U);
			
			robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
			robot.keyRelease(java.awt.event.KeyEvent.VK_ALT);
			robot.keyRelease(java.awt.event.KeyEvent.VK_U);
			
			Thread.sleep(500);//Grace split second
			
			//(3) look for a button whose onclick element contains the word chooseFile
			if(SeleniumConstants.debugLevel>=3){writeLog("uploadFile() accessing file selection dialog",tempTestNumber,2, uuid);}
			WebElement uploadFile =driver.findElement(By.cssSelector("button[onclick*=chooseFile]"));
			uploadFile.click();
			
			Thread.sleep(500);//Grace split second
			//(4)
			if(SeleniumConstants.debugLevel>=3){writeLog("uploadFile() Initiating typing procedure",tempTestNumber,2, uuid);}
			writeLog("Uploading file: "+filenameToUpload, tempTestNumber, 2, uuid);
			type(temp, 0, uuid);//file name here
			//(5)
			System.out.println("Pressing Enter");
			if(SeleniumConstants.debugLevel>=3){writeLog("uploadFile() Submitting selection to dialog",tempTestNumber,2, uuid);}
			robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
			robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
			writeLog("File uploaded", tempTestNumber, 2, uuid);
			//Thread.sleep(500);//Grace split second
			//(6)
			System.out.println("fileUpload: Initiating explicit wait");
			writeLog("FileUpload() Initiating explicit wait", tempTestNumber, 2, uuid);
			//explicit wait
			WebDriverWait wait = new WebDriverWait(driver, SeleniumConstants.explicitWaitTimeOut);
			try{
				//set test to wait for the upload button to load
				@SuppressWarnings("unused")
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("uploadConfirm-upload-btn")));
			}catch(TimeoutException te){
				System.out.println("Timed out, test fail");
				writeLog("Wait timeout exception", tempTestNumber, -1, uuid);
				writeLog("UPLOAD FILE ERROR FAIL", tempTestNumber, -1, uuid);
			}
			System.out.println("fileUpload: Explicit wait Success");
			writeLog("Explicit wait success", tempTestNumber, 2, uuid);
			Thread.sleep(500);//Grace split second
			////upload button xpath: /html/body/div/div[2]/div/div[2]/div/div[1]/div[2]/div[1]/div[2]/div[1]/button
			WebElement submitFile = driver.findElement(By.id(("uploadConfirm-upload-btn")));
			submitFile.click();
			
			Thread.sleep(500);//Grace split second
			
			//(7) have the robot hit ctrl+alt+b, switches to batches tab
			System.out.println("Switching tabs");
			if(SeleniumConstants.debugLevel>=3){writeLog("uploadFile() Switching to batches screen",tempTestNumber,2, uuid);}
			robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
			robot.keyPress(java.awt.event.KeyEvent.VK_ALT);
			robot.keyPress(java.awt.event.KeyEvent.VK_B);
			
			robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
			robot.keyRelease(java.awt.event.KeyEvent.VK_ALT);
			robot.keyRelease(java.awt.event.KeyEvent.VK_B);
			}
			catch (AWTException ex)
			{
			    System.err.println("Can't start Robot: " + ex);
			    writeLog("uploadFile() Can't start Robot",tempTestNumber,-1, uuid);
			}
		writeLog("uploadFile() FIN",tempTestNumber,2, uuid);
	}
	
	void writeLog(String logEntry,int testNum,int codeNum, String uuid){
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	write debug info to a log file
		 * source:	http://alvinalexander.com/java/edu/qanda/pjqa00009.shtml
		 * Status:	Heavily modified from original
		 * *****************************************/
		//Captain's log
		/**
		 * Code:	-1 = ERROR
		 * Code:	0 = Test log
		 * Code:	1 = Config
		 * Code:	2 = Method call log
		 * Code:	3 = Row count
		 * Code:	4 = wait logging
		 * */
		if(SeleniumConstants.consoleVerboseWriteLog){
			System.out.println("writeLog");
		}
		BufferedWriter bw = null;
		String date;
		date= new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
				
		try {
			// APPEND MODE SET HERE
			bw = new BufferedWriter(new FileWriter("target/Automated_Tests_"+uuid+".log", true));
			//write to the file 
			if(codeNum==0 || codeNum==3 || codeNum==4){
				bw.write("[INFO] "+date+" "+uuid+" Browser: "+browser+" Test: "+testNum+" "+logEntry+"");
			}
			else if(codeNum==2){
				bw.write("[INFO] "+date+" "+uuid+" Browser: "+browser+" Test: "+testNum+" "+logEntry+"");
			}
			else if(codeNum==-1){
				bw.write("[ERROR] "+date+" "+uuid+" Browser: "+browser+" Test: "+testNum+" "+logEntry+"");
			}
			else if(codeNum==1){
				bw.write("[CONFIG]"+logEntry);
			}
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {                       // always close the file
			if (bw != null) try {
				bw.close();
			} catch (IOException ioe2) {
		   		// just ignore it
			}
		} // end try/catch/finally
	}
	
	void detectTestFiles(int tempTestNumber, String uuid){
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	Scan the test file folder and populate a master list of test files
		 * *****************************************/
		System.out.println("detectTestFiles");
		writeLog("detectTestFiles() START", tempTestNumber, 2, uuid);
		//create list of files in a folder
		//source: http://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
		File folder = new File(SeleniumConstants.pathToTestsBackSlashedString);
		File[] listOfFiles = folder.listFiles();

		
		populateTestFileLists(listOfFiles, 0, uuid);
		writeLog("detectTestFiles() FIN", tempTestNumber, 2, uuid);
	}
	
	String removeFilePathFromString(String absolutePaths, String pathToRemove, int tempTestNumber, String uuid){
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	Remove the absolute path from the input string, aka disconcatenate,
		 * 			discard absolute path
		 * 			and return the string
		 * *****************************************/
		System.out.println("removeFilePathFromString");
		writeLog("removeFilePathFromString() START", tempTestNumber, 2, uuid);
		String disconcatenatedFileName;
		String filePath=pathToRemove;
		disconcatenatedFileName=absolutePaths.replace(filePath, "");
		disconcatenatedFileName=disconcatenatedFileName.replace("[", "\"");
		disconcatenatedFileName=disconcatenatedFileName.replace("]", "\"");
		disconcatenatedFileName=disconcatenatedFileName.replace(", ", "\" \"");
		writeLog("removeFilePathFromString() FIN", tempTestNumber, 2, uuid);
		return disconcatenatedFileName;
		
	}
	
	/**
	 * 
	 * @param listOfFiles
	 * @param tempTestNumber
	 * @param uuid
	 * @unimplemented
	 */
	private void populateTestFileLists(File[] listOfFiles, int tempTestNumber, String uuid){
		/* *****************************************
		 * Author:	William Bowdridge
		 * Purpose:	sort master list of test files into test specific lists
		 * *****************************************/
		
		//standard operation file lists
		List<File> nonErroneousTestFiles;//all standard operation files
		List<File> nonErroneousCSVFiles;//all standard operation CSV files
		List<File> nonErroneousXLSXFiles;//all standard operation XLSX files
		
		//exception file lists
		List<File> erroneousDateFiles;//all date limit test files
		List<File> erroneousFormatFiles;//all wrongly formatted test files, jpegs and such
		List<File> erroneousSpecialCharFiles;//all special character test files
		List<File> erroneousValidationFiles;//all test files that would throw an error for missing or wrong data
		List<File> erroneousInjectionFiles;//all test files with data that is trying to inject code into the system
		
		//error free file lists
		System.out.println("PopulateTestFileLists");
		writeLog("populateTestFileLists() START", tempTestNumber, 2, uuid);
		nonErroneousTestFiles = new ArrayList<>();
		nonErroneousCSVFiles = new ArrayList<>();
		nonErroneousXLSXFiles = new ArrayList<>();
		//error producing file lists
		erroneousDateFiles = new ArrayList<>();//all date limit test files
		erroneousFormatFiles = new ArrayList<>();//all wrongly formatted test files, jpegs and such
		erroneousSpecialCharFiles = new ArrayList<>();//all special character test files
		erroneousValidationFiles = new ArrayList<>();//all test files that would throw an error for missing or wrong data
		erroneousInjectionFiles = new ArrayList<>();//all test files with data that is trying to inject code into the system
		
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				//detect for what test the file belongs.
				if(listOfFiles[i].getName().contains("ACA")||listOfFiles[i].getName().contains("Adobe")){
					nonErroneousTestFiles.add(listOfFiles[i]);//yes it's identical to csv + xlsx lists, I'll remove it later
					if(listOfFiles[i].getName().contains("csv")){
						//add all SeleniumConstants.STANDARD csv files to a list
						nonErroneousCSVFiles.add(listOfFiles[i]);
					}
					else if(listOfFiles[i].getName().contains("xlsx")){
						//add all SeleniumConstants.STANDARD xlsx files to a list
						nonErroneousXLSXFiles.add(listOfFiles[i]);
					}
				}
				//populate error producing lists
				else if(listOfFiles[i].getName().contains("Date")){
					erroneousDateFiles.add(listOfFiles[i]);
				}
				else if(listOfFiles[i].getName().contains("Invalid")){
					erroneousFormatFiles.add(listOfFiles[i]);
				}
				else if(listOfFiles[i].getName().contains("Char")){
					erroneousSpecialCharFiles.add(listOfFiles[i]);
				}
				else if(listOfFiles[i].getName().contains("Validation")){
					erroneousValidationFiles.add(listOfFiles[i]);
				}
				else if(listOfFiles[i].getName().contains("Injection")){
					erroneousInjectionFiles.add(listOfFiles[i]);
				}
				
				//System.out.println("File " + listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
				//System.out.println("Directory " + listOfFiles[i].getName());
			}
		}
		writeLog("populateTestFileLists() FIN", tempTestNumber, 2, uuid);
	}
	/*wait for the button*/
	void waitForStartButton(int numberOfRows, String uuid){
		waitForButton(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[1]", 0, uuid);
	}
	void waitForPrintButton(int numberOfRows, String uuid){
		waitForButton(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[2]", 0, uuid);
	}
	private void waitForArchiveButton(int numberOfRows, String uuid){
		waitForButton(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[3]", 0, uuid);
	}
	void waitForPreviewButton(int numberOfRows, String uuid){
		waitForButton(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[4]", 0, uuid);
	}
	void waitForErrorButton(int numberOfRows, String uuid){
		waitForButton(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[5]", 0, uuid);
	}
	void waitForDeleteButton(int numberOfRows, String uuid){
		waitForButton(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[6]", 0, uuid);
	}
	
	
	/*wait for the preview PDF button*/
	void waitForPreviewPDF(int rowNumber, String uuid){
		waitForButton(SeleniumConstants.rootXPathPDF+"/tr["+rowNumber+"]/td[7]/a/input", 0, uuid);
	}
	/*click the preview PDF button*/
	void clickPreviewPDF(int rowNumber){
		WebElement generatePDF = driver.findElement(By.xpath(SeleniumConstants.rootXPathPDF+"/tr["+rowNumber+"]/td[7]/a/input"));
		generatePDF.click();
	}
	
	/*Click the button*/
	void clickStartButton(int numberOfRows){
		WebElement startButton = driver.findElement(By.xpath(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[1]"));
		startButton.click();
	}
	void clickPrintButton(int numberOfRows){
		WebElement startButton = driver.findElement(By.xpath(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[2]"));
		startButton.click();
	}
	private void clickArchiveButton(int numberOfRows){
		WebElement startButton = driver.findElement(By.xpath(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[3]"));
		startButton.click();
	}
	void clickPreviewButton(int numberOfRows){
		WebElement element = driver.findElement(By.xpath(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[4]"));
		element.click();
	}
	void clickErrorButton(int numberOfRows){
		WebElement element = driver.findElement(By.xpath(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[5]"));
		element.click();
	}
	void clickDeleteButton(int numberOfRows){
		WebElement element = driver.findElement(By.xpath(SeleniumConstants.rootXPathButtons+"/tr["+numberOfRows+"]/td[9]/input[6]"));
		element.click();
	}
	
	
	/*the wait for the button engine*/
	private void waitForButton(String xpathOfButton, int tempTestNumber, String uuid){
		System.out.println("Initiating explicit wait");
		writeLog("Initiating explicit wait", tempTestNumber, 4, uuid);
		//explicit wait
		WebDriverWait wait = new WebDriverWait(driver, SeleniumConstants.explicitWaitTimeOut);
		try{
			//set test to wait for start batch button of the last element(last thing uploaded)
			@SuppressWarnings("unused")//TODO this test failed
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOfButton)));
		}catch(TimeoutException te){
			System.out.println("Timed out, test fail");
			writeLog("Wait timeout exception", tempTestNumber, -1, uuid);
			writeLog("TEST "+tempTestNumber+" ERROR FAIL", tempTestNumber, -1, uuid);
		}
		System.out.println("Explicit wait success");
		writeLog("Explicit wait success", tempTestNumber, 4, uuid);
	}
	
	void closeTab(int tempTestNumber, String uuid){
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		try {
			Thread.sleep(2000);//grace split second
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeLog("Closing tab", tempTestNumber, 0, uuid);
		driver.close();
		driver.switchTo().window(tabs2.get(0));
	}
}
