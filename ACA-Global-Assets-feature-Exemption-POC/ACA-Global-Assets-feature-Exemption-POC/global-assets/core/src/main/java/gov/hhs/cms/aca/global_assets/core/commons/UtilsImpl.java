package gov.hhs.cms.aca.global_assets.core.commons;

import gov.hhs.cms.aca.global_assets.core.models.workflow.WorkflowHelpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.jcr.LoginException;
import javax.jcr.Session;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.adobe.granite.resourceresolverhelper.ResourceResolverHelper;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;

/**
 * Implementation class to bundle {@link gov.hhs.cms.aca.global_assets.core.commons} methods in a single externally available interface.
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 * @see {@link gov.hhs.cms.aca.global_assets.core.commons.Utils}
 *
 */
@Component(metatype = true, immediate = true, label = "ACA Global Assets - Common Utils Implementation", description = "ACA Global Assets UtilsImpl class")
@Service
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "ACA Global Assets - Common Utils Implementation"),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "package", value = "ACA Global Assets", propertyPrivate = true)})

public class UtilsImpl implements Utils {
	
	/* Class Statics */
	
	private static final Logger log = LoggerFactory.getLogger(UtilsImpl.class);
	
	/* Service References */
	
	@Reference private static ResourceResolverHelper resourceResolverHelper;
	
	/* Class Methods */

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#toInputStream(java.lang.String)
	 */
	@Override
	public InputStream toInputStream(String string) throws IOException{		
		return Primitives.toInputStream(string);		
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#persistData(com.adobe.granite.workflow.exec.WorkItem, com.adobe.granite.workflow.WorkflowSession, java.lang.String, T)
	 */
	@Override
	public <T> boolean persistData(WorkItem workItem, WorkflowSession workflowSession, String key, T val) {
		return WorkflowHelpers.persistData(workItem, workflowSession, key, val);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#getPersistedData(com.adobe.granite.workflow.exec.WorkItem, java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getPersistedData(WorkItem workItem, String key, Class<T> type) {
		return WorkflowHelpers.getPersistedData(workItem, key, type);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#getPersistedData(com.adobe.granite.workflow.exec.WorkItem, java.lang.String, T)
	 */
	@Override
	public <T> T getPersistedData(WorkItem workItem, String key, T defaultValue) {
		return WorkflowHelpers.getPersistedData(workItem, key, defaultValue);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#parseWorkflowArgs(com.adobe.granite.workflow.metadata.MetaDataMap, java.lang.String)
	 */
	@Override
	public Map<String,String> parseWorkflowArgs(MetaDataMap args, String argsEmptyValue){
		return WorkflowHelpers.parseWorkflowArgs(args, argsEmptyValue);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#getResourceResolver(javax.jcr.Session)
	 */
	@Override
	public ResourceResolver getResourceResolver(Session session) throws org.apache.sling.api.resource.LoginException {
		return SlingHelpersImpl.getResourceResolver(session);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#getResourceResolver(java.lang.String)
	 */
	@Override
	public ResourceResolver getResourceResolver(String sysUser) throws org.apache.sling.api.resource.LoginException {
		return SlingHelpersImpl.getResourceResolver(sysUser);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#getAmbiguousService(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Object getAmbiguousService(Object caller, @SuppressWarnings("rawtypes") Class service){
		return SlingHelpersImpl.getAmbiguousService(caller, service);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#getServiceProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public String getServiceProperty(Object caller, String property){
		return SlingHelpersImpl.getServiceProperty(caller, property);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#getConfig(java.lang.String)
	 */
	@Override
	public Dictionary<?, ?> getConfig(String pid) throws IOException {
		return SlingHelpersImpl.getConfig(pid);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#getConfig(java.lang.String, java.lang.String)
	 */
	@Override
	public String getConfig(String pid, String key) throws IOException{
		return SlingHelpersImpl.getConfig(pid, key);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#domToString(org.w3c.dom.Document)
	 */
	@Override
	public String domToString(Document doc) throws TransformerException {
		return W3DocumentHelpers.domToString(doc);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#stringToDom(java.lang.String)
	 */
	@Override
	public Document stringToDom(String xml) throws TransformerException{
		return W3DocumentHelpers.stringToDom(xml);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#transformXML(org.w3c.dom.Document, org.w3c.dom.Document)
	 */
	@Override
	public Document transformXML(Document xml, Document xslt) throws TransformerException{
		return W3DocumentHelpers.transformXML(xml, xslt);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#w3cDocToAemDoc(org.w3c.dom.Document)
	 */
	@Override
	public com.adobe.aemfd.docmanager.Document w3cDocToAemDoc(org.w3c.dom.Document w3cDoc) throws TransformerException {
		return W3DocumentHelpers.w3cDocToAemDoc(w3cDoc);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#aemDocToW3cDoc(com.adobe.aemfd.docmanager.Document)
	 */
	@Override
	public org.w3c.dom.Document aemDocToW3cDoc(com.adobe.aemfd.docmanager.Document aemDoc) throws SAXException, IOException, ParserConfigurationException{
		return W3DocumentHelpers.aemDocToW3cDoc(aemDoc);

	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#domFromFile(java.lang.String)
	 */
	@Override
	public Document domFromFile(String path) throws SAXException, IOException, ParserConfigurationException {
		return W3DocumentHelpers.domFromFile(path);
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.commons.Utils#getXPathValue(java.lang.String, org.w3c.dom.Document)
	 */
	@Override
	public String getXPathValue(String xpath, Document dom) throws XPathExpressionException{
		return W3DocumentHelpers.getXPathValue(xpath, dom);
	}

	/**
	 * Method used to invoke APIs within a thread specific ResourceResolver allowing these APIs 
	 * to be called within a certain Session context.
	 * 
	 * @param resolver
	 * @param callable
	 * @return
	 * @throws Exception
	 */
	public static <T> T executeAs(ResourceResolver resolver, Callable<T> callable) throws Exception{
		return resourceResolverHelper.callWith(resolver, callable);
	}

	/*
	 * Binding Methods
	 */	

	@Activate
	protected void activate() {
		log.info("[Service Activated]");
	}

	@Deactivate
	protected void deactivate() {
		log.info("[Service Deactivated]");
	}

	protected void bindResourceResolverHelper(ResourceResolverHelper injected){
		log.info("[Binding ResourceResolverHelper]");
		resourceResolverHelper = injected;
		log.info("[Bound ResourceResolverHelper]");
	}
}
