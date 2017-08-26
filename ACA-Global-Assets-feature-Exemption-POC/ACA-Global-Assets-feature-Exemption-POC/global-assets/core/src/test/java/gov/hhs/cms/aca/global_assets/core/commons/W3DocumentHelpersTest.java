package gov.hhs.cms.aca.global_assets.core.commons;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * The class <code>W3DocumentHelpersTest</code> contains tests for the class
 * {@link <code>W3DocumentHelpers</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 23/03/16 8:20 AM
 *
 * @author Guillaume
 *
 * @version $Revision$
 */
public class W3DocumentHelpersTest extends TestCase {

	/**
	 * Construct new test instance
	 *
	 * @param name the test name
	 */
	public W3DocumentHelpersTest(String name) {
		super(name);
	}
	
	public static Document sampleDocument;

	/**
	 * Run the Document domFromFile(String) method test
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testDomFromFile() throws SAXException, IOException, ParserConfigurationException {
		String path = "src/test/resources/Batch.xml.preview";
		Document result = W3DocumentHelpers.domFromFile(path);
		assertNotNull(result);
	}
	
	/**
	 * Run the String domToString(Document) method test
	 * @throws TransformerException 
	 */
	public void testDomToString() throws TransformerException {
		String result = W3DocumentHelpers.domToString(sampleDocument);
		assertNotNull(result);
	}

	/**
	 * Run the Document stringToDom(String) method test
	 * @throws TransformerException 
	 */
	public void testStringToDom() throws TransformerException {

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<batchNoticeData xmlns:xsi=\"htt//www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"htt//www.cms.hhs.gov/notices BatchNotice.xsd \">" +
				"  <NoticeDate></NoticeDate>" +
				"  <PolicyId></PolicyId>" +
				"  <applicationSignatureDate></applicationSignatureDate>" +
				"  <SystemDate></SystemDate>" +
				"  <NoticeNumber></NoticeNumber>" +
				"  <AccountId></AccountId>" +
				"  <InsuranceMarketType></InsuranceMarketType>" +
				"  <NoticeTypeCode></NoticeTypeCode>" +
				"  <UniqueNoticeID></UniqueNoticeID>" +
				"  <CoverageYear></CoverageYear>" +
				"  <locale></locale>" +
				"  <ApplicationId></ApplicationId>" +
				"  <Barcode></Barcode>" +
				"<PostalCode></PostalCode>" +
				"  <FileName></FileName>" +
				"  <FileType></FileType>" +
				"  <FormName></FormName>" +
				"  <VersionNumber></VersionNumber>" +
				"  <SubmissionDate></SubmissionDate>" +
				"  <TrackingNumber></TrackingNumber>" +
				"  <NoticeData>" +
				"    <EmployerName></EmployerName>" +
				"	<JobIncomeEmployerName></JobIncomeEmployerName>" +
				"	<EmployeNameMatched></EmployeNameMatched>" +
				"    <StreetName1></StreetName1>" +
				"    <CityName></CityName>" +
				"    <State></State>" +
				"    <ZipCode></ZipCode>" +
				"    <EmployerTelphoneNumber></EmployerTelphoneNumber>" +
				"    <EmployereIN></EmployereIN>" +
				"    <ContactPersonFirstName></ContactPersonFirstName>" +
				"    <ContactPersonLastName></ContactPersonLastName>" +
				"    <ContactPersonTelephoneNumber></ContactPersonTelephoneNumber>" +
				"    <PersonFirstName></PersonFirstName>" +
				"    <PersonLastName></PersonLastName>" +
				"	<newParent>" +
				"		<newNode></newNode>" +
				"	</newParent>" +
				"    <PersonSuffix></PersonSuffix>" +
				"    <SSN></SSN>" +
				"    <BirthDate></BirthDate>" +
				"    <CoveredInsuredMember></CoveredInsuredMember>" +
				"  </NoticeData>" +
				"</batchNoticeData>";
		Document result = W3DocumentHelpers.stringToDom(xml);
		assertNotNull(result);
	}

	/**
	 * Run the Document transformXML(Document, Document) method test
	 * @throws TransformerException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testTransformXML() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String path = "src/test/resources/PrettyPrintFlatXML.xsl";
		Document xslt = W3DocumentHelpers.domFromFile(path);
		Document result = W3DocumentHelpers.transformXML(sampleDocument, xslt);
		assertNotNull(result);
	}

	/**
	 * Run the String getXPathValue(String, Document) method test
	 * @throws XPathExpressionException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testGetXPathValue() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		
		String path = "src/test/resources/Batch.xml.preview";
		Document result = W3DocumentHelpers.domFromFile(path);
		
		String xpath = "/*[name()='batchNoticeData']/*[name()='NoticeData']/*[name()='EmployerName']";
		String value = W3DocumentHelpers.getXPathValue(xpath, result);		
		
		assertEquals("!5TH AVE CHILD CARE LEARNING CENTER", value);
	}

	/**
	 * Run the Document setXPathValue(String, String, Document) method test
	 * @throws XPathExpressionException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testSetXPathValue() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		String path = "src/test/resources/Batch.xml.preview";
		Document dom = W3DocumentHelpers.domFromFile(path);
		
		String value = "newValue";
		String xpath = "/*[name()='batchNoticeData']/*[name()='NoticeData']/*[name()='EmployerName']";
		
		Document result = W3DocumentHelpers.setXPathValue(value, xpath, dom);
		
		String valueMod = W3DocumentHelpers.getXPathValue(xpath, result);
		
		assertEquals(value, valueMod);
	}

	/**
	 * Run the Document insertNode(String, String, String, Document) method
	 * test
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testInsertNode() throws SAXException, IOException, ParserConfigurationException {
		
		String path = "src/test/resources/Batch.xml.preview";
		Document dom = W3DocumentHelpers.domFromFile(path);
		
		String parent = dom.getChildNodes().item(0).getNodeName();
		String tagName = "guid";
		String value = "somekindofinsertedguid";

		Document result = W3DocumentHelpers.insertNode(
			parent,
			tagName,
			value,
			dom);
		assertNotNull(result);
	}

	/**
	 * Run the DocumentBuilder getDomBuilder() method test
	 * @throws ParserConfigurationException 
	 */
	public void testGetDomBuilder() throws ParserConfigurationException {
		DocumentBuilder result = W3DocumentHelpers.getDomBuilder();
		assertNotNull(result);
	}

	/**
	 * Run the Transformer getTransformer(Source) method test
	 * @throws TransformerConfigurationException 
	 */
	public void testGetTransformer() throws TransformerConfigurationException {
		Source source = null;
		Transformer result = W3DocumentHelpers.getTransformer(source);
		assertNotNull(result);
	}

	/**
	 * Run the String serializeXML(InputStream) method test
	 * @throws TransformerException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testSerializeXML() throws SAXException, IOException, ParserConfigurationException, TransformerException {

		String path = "src/test/resources/Batch.xml.preview";
		Document dom = W3DocumentHelpers.domFromFile(path);
		
		InputStream stream = W3DocumentHelpers.toInputStream(dom);
		String result = W3DocumentHelpers.serializeXML(stream);
		assertNotNull(result);
	}

	/**
	 * Run the InputStream toInputStream(org.w3c.dom.Document) method test
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 * @throws TransformerConfigurationException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testToInputStream() throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError, SAXException, IOException, ParserConfigurationException {
		String path = "src/test/resources/Batch.xml.preview";
		Document dom = W3DocumentHelpers.domFromFile(path);
	
		InputStream result = W3DocumentHelpers.toInputStream(dom);
		assertNotNull(result);
	}

	/**
	 * Run the Boolean nodeIsSerializable(Node) method test
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testNodeIsSerializable() throws SAXException, IOException, ParserConfigurationException {
		String path = "src/test/resources/Batch.xml.preview";
		Document dom = W3DocumentHelpers.domFromFile(path);

		Node node = dom.getDocumentElement();
		Boolean result = W3DocumentHelpers.nodeIsSerializable(node);
		assertTrue(result);
	}

	/**
	 * Run the String getInnerXml(Node) method test
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void testGetInnerXml() throws SAXException, IOException, ParserConfigurationException {
		String path = "src/test/resources/Batch.xml.preview";
		Document dom = W3DocumentHelpers.domFromFile(path);

		Node node = dom.getDocumentElement();
		String result = W3DocumentHelpers.getInnerXml(node);
		assertNotNull(result);
	}

	/**
	 * Run the String linarizeXml(String) method test
	 */
	public void testLinarizeXml() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<batchNoticeData xmlns:xsi=\"htt//www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"htt//www.cms.hhs.gov/notices BatchNotice.xsd \">" +
				"  <NoticeDate></NoticeDate>" +
				"</batchNoticeData>";
		
		String flatXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><batchNoticeData xmlns:xsi=\"htt//www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"htt//www.cms.hhs.gov/notices BatchNotice.xsd \">  <NoticeDate></NoticeDate></batchNoticeData>";
		
		String result = W3DocumentHelpers.linarizeXml(xml);

		assertEquals(flatXml, result);
	}

	/**
	 * Run the boolean validateAgainstXSD(Document, Document, ErrorHandler)
	 * method test
	 * @throws IOException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 * @throws SAXException 
	 * @throws TransformerConfigurationException 
	 * @throws ParserConfigurationException 
	 */
	public void testValidateAgainstXSD() throws TransformerConfigurationException, SAXException, TransformerException, TransformerFactoryConfigurationError, IOException, ParserConfigurationException {
		String path = "src/test/resources/Batch.xml.preview";
		Document xml = W3DocumentHelpers.domFromFile(path);
		
		String path2 = "src/test/resources/BatchNotice_Validation.xsd";
		Document xsd = W3DocumentHelpers.domFromFile(path2);
		

		ErrorHandler errorHandler = null;
		boolean result = W3DocumentHelpers.validateAgainstXSD(
			xml,
			xsd,
			errorHandler);
		assertTrue(result);
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
 * testCase = W3DocumentHelpersTest
 * testClassType = gov.hhs.cms.aca.global_assets.core.commons.W3DocumentHelpers
 */