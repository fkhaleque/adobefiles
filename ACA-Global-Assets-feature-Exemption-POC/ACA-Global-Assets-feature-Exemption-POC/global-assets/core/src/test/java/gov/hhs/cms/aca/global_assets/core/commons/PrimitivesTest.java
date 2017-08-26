package gov.hhs.cms.aca.global_assets.core.commons;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import junit.framework.TestCase;

/**
 * The class <code>PrimitivesTest</code> contains tests for the class {@link
 * <code>Primitives</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 21/03/16 9:19 AM
 *
 * @author Guillaume
 *
 * @version $Revision$
 */
public class PrimitivesTest extends TestCase {

	/**
	 * Construct new test instance
	 *
	 * @param name the test name
	 */
	public PrimitivesTest(String name) {
		super(name);
	}

	/**
	 * Run the InputStream toInputStream(String) method test
	 * @throws IOException 
	 */
	public void testToInputStreamAndToString() throws IOException {
		
		String string = "NEW STRING!!!";
		InputStream result = Primitives.toInputStream(string);
		
		assertNotNull("", result);
		
		String streamString = Primitives.inputStreamToString(result);
		
		assertEquals(string, streamString);
		
	}

	/**
	 * Run the String readFile(String, Charset) method test
	 * @throws IOException 
	 */
	public void testReadFile() throws IOException {
		String path = "src/test/resources/Batch.xml.preview";
		Charset encoding = Charset.defaultCharset();
		String result = Primitives.readFile(path, encoding);
		assertNotNull(result);
	}

	/**
	 * Run the T ifNull(T, T) method test
	 */
	public void testIfNull() {
		String toCheck = null;
		Object result = Primitives.ifNull(toCheck, true);
		assertTrue(Boolean.valueOf(result.toString()));
	}

	/**
	 * Run the boolean isNull(T) method test
	 */
	public void testIsNull() {
		String toCheck = null;
		boolean result = Primitives.isNull(toCheck);
		assertTrue(result);
	}

	/**
	 * Run the long stream(InputStream, OutputStream) method test
	 * @throws IOException 
	 */
	public void testStream() throws IOException {
		InputStream input = Primitives.toInputStream("NEW INPUT STREAM!!!");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		long result = Primitives.stream(input, oos);
		assertNotNull(result);
	}
	
	/**
	 * Run the createMissingFolder(String) method test
	 */
	public void testCreateMissingFolder(){
		String path = "src/main/target/test/testfolder";
		
		Primitives.createMissingFolder(path);
		
		boolean folderCheck = new File(path).exists();
		
		assertTrue("Checking the folder exists", folderCheck);
		
	}
}

/*$CPS$ This comment was generated by CodePro. Do not edit it.
 * patternId = com.instantiations.assist.eclipse.pattern.testCasePattern
 * strategyId = com.instantiations.assist.eclipse.pattern.testCasePattern.junitTestCase
 * additionalTestNames = 
 * assertTrue = false
 * callTestMethod = true
 * createMain = false
 * createSetUp = false
 * createTearDown = false
 * createTestFixture = false
 * createTestStubs = true
 * methods = 
 * package = gov.hhs.cms.aca.global_assets.core.commons
 * package.sourceFolder = global-assets.core/src/test/java
 * superclassType = junit.framework.TestCase
 * testCase = PrimitivesTest
 * testClassType = gov.hhs.cms.aca.global_assets.core.commons.Primitives
 */