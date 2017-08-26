package gov.hhs.cms.aca.global_assets.it.tests.selenium;

public class SeleniumConstants {
	
		//file path on my local machine
		static String pathToTests="src/resources/testdata";
		static String pathToTestsBackSlashedString="src/resources/testdata";
		
		static String userAEM="admin";
		static String passwordAEM="admin";
		static String userDashboard="admin";		//local: admin		CMS:testuser1
		static String passwordDashboard="admin";	//local: admin		CMS:password
		static String loginPageString="http://localhost:4502/content/batchnoticeprocessing/login.html";
		//test server located at: 	http://cms-test.adobecqms.net/content/batchnoticeprocessing/login.html
		//local server:				http://localhost:4502/content/batchnoticeprocessing/login.html
		
		static String rootXPathButtons = "/html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[1]/div/table/tbody";
		static String rootXPathPDF = "/html/body/div/div[2]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[2]/table/tbody";
		
		//TODO refine auto populated lists and implement
		//need to determine which files apply to which kinds of tests
		static boolean useAutoPopulatedLists = false;
		
		static boolean AEMloginRequired = false;
		
		static boolean addDateToScreenShots = true;
		
		static boolean verboseDebugging = true;
		static int debugLevel = 1;//TODO implement this
		/* Debugging levels
		 * Level 1: write method start/stop
		 * Level 2: write actions
		 * Level 3: write individual steps
		 * */
		static final int BASIC = 1;
		static final int DETAILED = 2;
		static final int OVERT = 3;
		
		//log codes
		static final int ERRORCODE = -1;
		static final int STANDARD = 0;
		static final int CONFIG = 1;
		static final int ROWCOUNT = 3;
		static final int WAITNOTE = 4;
		
		
		static boolean consoleVerboseWriteLog = false;
		
		String testBatchNum="Batch_4";//don't forget to change before testing
		
		static int explicitWaitTimeOut=180;//I prefer to set this to 3 minutes (in seconds)
		static int pageLoadTimeOut=60;//in seconds
		int tempTestNumber;
}
