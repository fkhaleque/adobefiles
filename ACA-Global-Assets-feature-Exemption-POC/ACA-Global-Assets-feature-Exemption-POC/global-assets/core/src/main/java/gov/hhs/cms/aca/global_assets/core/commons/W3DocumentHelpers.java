package gov.hhs.cms.aca.global_assets.core.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import gov.hhs.cms.aca.global_assets.core.models.exceptions.CombinedMessageErrorHandler;
/**
 * W3DocumentHelpers is a class which contains an array of helper methods related to parsing, transformation, and creation
 * of W3C Document objects
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 *
 */
/**
 * @author Guillaume
 *
 */
public class W3DocumentHelpers {

	public static final String XML_LINERIZATION_REGEX = "(>|&gt;){1,1}(\\t)*(\\n|\\r)+(\\s)*(<|&lt;){1,1}";
	public static final String XML_LINERIZATION_REPLACEMENT = "$1$5";

	/**
	 * Method available to serialize a W3C Document into a string.
	 * 
	 * @param doc
	 * @return string
	 * @throws IOException
	 * @throws TransformerException
	 * @see {@link #getTransformer()}
	 */
	public static String domToString(Document doc) throws TransformerException {

		final Transformer transformer = getTransformer(null);

		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);

		transformer.transform(new DOMSource(doc), result);

		return writer.toString();
	}

	/**
	 * Method available to parse a valid XML string into a W3C Document.
	 * 
	 * @param xml
	 * @return document
	 * @throws TransformerException 
	 * @see {@link #getTransformer()}
	 */
	public static Document stringToDom(String xml) throws TransformerException{

		final Transformer transformer = getTransformer(null);

		final StringReader reader = new StringReader(xml);
		final Source source = new StreamSource(reader);
		final DOMResult result = new DOMResult();

		transformer.transform(source, result);

		return (Document)result.getNode();
	}

	/**
	 * Method used to transform XML document using XSLT. Both W3C document nodes are used as inputs and are handled in memory.
	 * The documents need to be created using a namespace aware DOM.
	 * 
	 * @param xml
	 * @param xslt
	 * @return transformedDoc
	 * @throws TransformerException
	 * @see {@link #getTransformer()}
	 */
	public static Document transformXML(Document xml, Document xslt) throws TransformerException{

		final DOMResult result = new DOMResult();      

		// the factory pattern supports different XSLT processors
		final Transformer transformer = getTransformer(new DOMSource(xslt));

		transformer.transform(new DOMSource(xml), result);

		final Document resultDoc = (Document)result.getNode();

		return resultDoc;
	}

	/**
	 * Conversion method used to transform an W3C Document object to a AEM Document.
	 * 
	 * @param w3cDoc
	 * @return aemDoc
	 * @throws TransformerException
	 * @see {@link #getTransformer()}
	 */
	public static com.adobe.aemfd.docmanager.Document w3cDocToAemDoc(org.w3c.dom.Document w3cDoc) throws TransformerException{

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final Source xmlSource = new DOMSource(w3cDoc);
		final Result outputTarget = new StreamResult(outputStream);

		final Transformer transformer = getTransformer(null);
		transformer.transform(xmlSource, outputTarget);

		final InputStream is = new ByteArrayInputStream(outputStream.toByteArray());

		final com.adobe.aemfd.docmanager.Document aemDoc = new com.adobe.aemfd.docmanager.Document(is);
		return aemDoc;
	}

	/**
	 * Conversion method used to transform an AEM Document object to a W3C Document. This only applies to W3C compatible
	 * documents.
	 * 
	 * @param aemDoc
	 * @return w3Doc
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @see {@link #getDomBuilder()}
	 */
	public static org.w3c.dom.Document aemDocToW3cDoc(com.adobe.aemfd.docmanager.Document aemDoc) throws SAXException, IOException, ParserConfigurationException{

		final DocumentBuilder dBuilder = getDomBuilder();
		org.w3c.dom.Document w3Doc;
		InputStream docStream = null;
		InputSource source = null;

		try {
			docStream = aemDoc.getInputStream();
			source = new InputSource(aemDoc.getInputStream());

			w3Doc = dBuilder.parse(source);
		} finally{
			if(!Primitives.isNull(docStream)){
				try {
					docStream.close();
				} catch (IOException e) {
					System.out.println("Failed to close streams");
				}
			}
			if(!Primitives.isNull(source)){
				try {
					source.getByteStream().close();
				} catch (IOException e) {
					System.out.println("Failed to close streams");
				}
			}
		}

		return w3Doc;

	}

	/**
	 * Method used to read a W3C Document from disk
	 * 
	 * @param path
	 * @return document
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @see {@link #getDomBuilder()}
	 */
	public static Document domFromFile(String path) throws SAXException, IOException, ParserConfigurationException{

		final File xml = new File(path);

		final DocumentBuilder dBuilder = getDomBuilder();
		Document doc = dBuilder.parse(xml);

		return doc;
	}

	/**
	 * Method use to retrieve data node value using XPATH traversal.
	 * 
	 * @param xpath
	 * @param dom
	 * @return xpathValue
	 * @throws XPathExpressionException
	 */
	public static String getXPathValue(String xpath, Document dom) throws XPathExpressionException{

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();

		String value = (String) xPath.evaluate(
				xpath, dom,
				XPathConstants.STRING);

		return value;
	}
	
	public static NodeList getXPathNodes(String xpath, Document dom) throws XPathExpressionException{

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();

		return (NodeList) xPath.evaluate(
				xpath, dom,
				XPathConstants.NODESET);

	}

	/**
	 * Method use to set data node value using XPATH traversal.
	 * 
	 * @param value
	 * @param xpath
	 * @param dom
	 * @return dom
	 * @throws XPathExpressionException
	 * @see {@link <a href="http://stackoverflow.com/questions/5239685/xml-namespace-breaking-my-xpath">Namespace Breaking</a>}
	 */
	public static Document setXPathValue(String value, String xpath, Document dom) throws XPathExpressionException{

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();

		NodeList nodeList = (NodeList) xPath.compile(xpath)
				.evaluate(dom, XPathConstants.NODESET);

		nodeList.item(0).setTextContent(value);

		return dom;
	}

	/**
	 * Method used to insert data node in DOM.
	 * 
	 * @param parent
	 * @param tagName
	 * @param value
	 * @param dom
	 * @return dom
	 */
	public static Document insertNode(String parent, String tagName, String value, Document dom){

		NodeList nodeList = dom.getElementsByTagName(parent);

		Text text = dom.createTextNode(value);
		org.w3c.dom.Element element = dom.createElement(tagName);

		element.appendChild(text);

		nodeList.item(0).appendChild(element);

		return dom;
	}

	/**
	 * Private method to return a pre-configured DocumentBuilder used with all of the other public methods.
	 * 
	 * @return documentBuilder
	 * @throws ParserConfigurationException
	 * @see <a href="http://stackoverflow.com/questions/33044212/transform-xml-with-xslt-in-java-using-dom/33052366#33052366">Transform XML with XSLT in Java using DOM</a>
	 */
	public static DocumentBuilder getDomBuilder() throws ParserConfigurationException{

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);

		//No DTD Expansion supported
		dbFactory.setExpandEntityReferences(false);

		//Disallow DOCTYPE declaration
		dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

		//Enable validation
		dbFactory.setValidating(true);
		dbFactory.setFeature("http://xml.org/sax/features/validation", true);

		//XML External Entity Injection Prevention
		dbFactory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		return dBuilder;
	}

	/**
	 * Private method to return a pre-configured Transformer used with all of the other public methods.
	 * 
	 * @param source (null is not required)
	 * @return transformer
	 * @throws TransformerConfigurationException
	 */
	public static Transformer getTransformer(Source source) throws TransformerConfigurationException{

		TransformerFactory tf = TransformerFactory.newInstance();

		//XML External Entity Injection Prevention
		tf.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);

		/*
		 * Disabled 
		 * 
		 * @see http://stackoverflow.com/questions/27128578/set-feature-accessexternaldtd-in-transformerfactory
		 * tf.setFeature(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, false);
		 */

		Transformer transformer = null;

		if(source != null){
			transformer = tf.newTransformer(source);
		}
		else{
			transformer = tf.newTransformer();
		}

		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		return transformer;
	}

	public static String serializeXML(InputStream stream) throws SAXException, IOException, ParserConfigurationException, TransformerException{

		DocumentBuilder builder = getDomBuilder();
		org.w3c.dom.Document doc = builder.parse(stream);
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		Transformer transformer = getTransformer(null);
		transformer.transform(domSource, result);

		return writer.toString();
	}

	public static InputStream toInputStream(org.w3c.dom.Document doc) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError{

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Source xmlSource = new DOMSource(doc);
		Result outputTarget = new StreamResult(outputStream);
		getTransformer(null).transform(xmlSource, outputTarget);
		InputStream is = new ByteArrayInputStream(outputStream.toByteArray());

		return is;
	}

	public static Boolean nodeIsSerializable(Node node){

		try{
			getInnerXml(node);
			return true;
		} catch(Exception e){
			return false;
		}
	}

	public static String getInnerXml(Node node) {

		DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument()
				.getImplementation().getFeature("LS", "3.0");

		LSSerializer lsSerializer = lsImpl.createLSSerializer();
		lsSerializer.getDomConfig().setParameter("xml-declaration", false);

		NodeList childNodes = node.getChildNodes();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < childNodes.getLength(); i++) {
			sb.append(lsSerializer.writeToString(childNodes.item(i)));
		}

		return sb.toString();
	}

	public static String linarizeXml(String xml) {
		return (xml!= null) ? xml
				.trim()
				.replaceAll(XML_LINERIZATION_REGEX, XML_LINERIZATION_REPLACEMENT) : null;
	}

	/**
	 * Validates an XML document against an XSD schema
	 * @param xml
	 * @param xsd
	 * @return valid(boolean)
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 * @throws SAXException 
	 * @throws TransformerConfigurationException 
	 * @throws IOException 
	 */
	public static boolean validateAgainstXSD(Document xml, Document xsd, ErrorHandler errorHandler) throws TransformerConfigurationException, SAXException, TransformerException, TransformerFactoryConfigurationError, IOException{   	

		SchemaFactory factory = 
				SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		factory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
		
		Schema schema = 
				factory.newSchema(
						new StreamSource(
								toInputStream(xsd)));
		

		Validator validator = schema.newValidator();
		

		if (null != errorHandler) {
			validator.setErrorHandler(errorHandler);
		}
		
		validator.validate(new StreamSource(
				toInputStream(xml)));
		
		if (null != errorHandler && errorHandler instanceof CombinedMessageErrorHandler) {
			CombinedMessageErrorHandler combinedMessageErrorHandler = (CombinedMessageErrorHandler) errorHandler;
			combinedMessageErrorHandler.throwCombinedErrors();
		}
		
		return true;
	}
	
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child !=null && child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

	public static void main(String args[]) throws FileNotFoundException, MalformedURLException, SAXException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError, XPathExpressionException{

		boolean result = validateAgainstXSD(domFromFile("src/test/resources/Batch.xml.preview"), 
				domFromFile("src/test/resources/BatchNotice_Validation.xsd"), null);

		System.out.println(result);
		
		/*
		Document sampleXmlDocument = domFromFile("src/test/resources/BatchNoticeData.xml");
		
		Document newDocument = W3DocumentHelpers.setXPathValue("1001", "/*[name()='batchNoticeData']/*[name()='NoticeNumber']", sampleXmlDocument);
		
		System.out.println(W3DocumentHelpers.domToString(newDocument));*/
	}
}
