package gov.hhs.cms.aca.global_assets.core.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.Session;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.sling.api.resource.ResourceResolver;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;

/**
 * Interface used to bundle {@link gov.hhs.cms.aca.global_assets.core.commons} methods in a single combined implementation.
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 * @see {@link gov.hhs.cms.aca.global_assets.core.commons.UtilsImpl}
 *
 */
public interface Utils {

	/**
	 * Method to convert a string into a ByteArrayInputStream
	 * 
	 * @param string
	 * @return ByteArrayInputStream
	 * @throws IOException
	 */
	public abstract InputStream toInputStream(String string) throws IOException;

	/**
	 * Helper method to persist workflow data key pair
	 * 
	 * @param workItem
	 * @param workflowSession
	 * @param key
	 * @param val
	 * @return boolean
	 */
	public abstract <T> boolean persistData(WorkItem workItem,
			WorkflowSession workflowSession, String key, T val);

	/**
	 * Helper method to retrieve persisted data from workflow based on key pair.
	 * 
	 * @param workItem
	 * @param key
	 * @param type
	 * @return map
	 */
	public abstract <T> T getPersistedData(WorkItem workItem, String key,
			Class<T> type);

	/**
	 * Helper method to retrieve persisted data from workflow based on default value.
	 * 
	 * @param workItem
	 * @param key
	 * @param defaultValue
	 * @return map
	 */
	public abstract <T> T getPersistedData(WorkItem workItem, String key,
			T defaultValue);

	/**
	 * Helper method to parse standard workflow step arguments provided in the following fashion:
	 * argument1=value1,argument2=value2
	 * 
	 * @param args
	 * @param argsEmptyValue
	 * @return values
	 */
	public abstract Map<String, String> parseWorkflowArgs(MetaDataMap args,
			String argsEmptyValue);
	
	/**
	 * Helper method to retrieve ResourceResolver within current session
	 * 
	 * @param resolver (null to use default)
	 * @param session
	 * @return ResourceResolver
	 * @throws org.apache.sling.api.resource.LoginException 
	 */
	public abstract ResourceResolver getResourceResolver(Session session) throws org.apache.sling.api.resource.LoginException;
			
	
	/**
	 * Helper method to retrieve ResourceResolver using a System User
	 * 
	 * @param sysUser
	 * @return ResourceResolver
	 * @throws org.apache.sling.api.resource.LoginException
	 */
	public abstract ResourceResolver getResourceResolver(String sysUser) throws org.apache.sling.api.resource.LoginException;

	/**
	 * Helper method to retrieve OSGI service dynamically where SCR annotations may not be able to inject a service definition.
	 * 
	 * @param caller
	 * @param service
	 * @return (castable) Object
	 * @throws Exception 
	 * @sample OutputService outputService = (OutputService) Helpers.getAmbiguousService(this, OutputService.class);
	 */
	public abstract Object getAmbiguousService(Object caller, @SuppressWarnings("rawtypes") Class service);

	/**
	 * Helper method to retrieve a service property
	 * 
	 * @param caller
	 * @param propertyName
	 * @return value
	 * @throws Exception 
	 */
	public abstract String getServiceProperty(Object caller, String property);

	/**
	 * Helper method to get configuration based on PID
	 * 
	 * @param pid
	 * @return Dictionary<?, ?>
	 * @throws IOException 
	 * @see {@link org.apache.sling.commons.osgi.PropertiesUtis}
	 */
	public abstract Dictionary<?, ?> getConfig(String pid) throws IOException;

	/**
	 * Helper method to get specific configuration value based on PID
	 * @param pid
	 * @param key
	 * @return Dictionary<?, ?>
	 * @throws IOException 
	 * @see {@link org.apache.sling.commons.osgi.PropertiesUtis}
	 */
	public abstract String getConfig(String pid, String key) throws IOException;

	/**
	 * Method available to serialize a W3C Document into a string.
	 * 
	 * @param doc
	 * @return string
	 * @throws TransformerException
	 * @see {@link #getTransformer()}
	 */
	public abstract String domToString(Document doc) throws TransformerException;

	/**
	 * Method available to parse a valid XML string into a W3C Document.
	 * 
	 * @param xml
	 * @return document
	 * @throws TransformerException
	 * @see {@link #getTransformer()}
	 */
	public abstract Document stringToDom(String xml) throws TransformerException;

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
	public abstract Document transformXML(Document xml, Document xslt) throws TransformerException;

	/**
	 * Conversion method used to transform an W3C Document object to a AEM Document.
	 * 
	 * @param w3cDoc
	 * @return aemDoc
	 * @throws TransformerException
	 * @see {@link #getTransformer()}
	 */
	public abstract com.adobe.aemfd.docmanager.Document w3cDocToAemDoc(
			org.w3c.dom.Document w3cDoc) throws TransformerException;

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
	public abstract org.w3c.dom.Document aemDocToW3cDoc(
			com.adobe.aemfd.docmanager.Document aemDoc) throws SAXException, IOException, ParserConfigurationException;

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
	public abstract Document domFromFile(String path) throws SAXException, IOException, ParserConfigurationException;

	/**
	 * Method use to retrieve data node value using XPATH traversal.
	 * 
	 * @param xpath
	 * @param dom
	 * @return xpathValue
	 * @throws XPathExpressionException
	 */
	String getXPathValue(String xpath, Document dom)
			throws XPathExpressionException;
}