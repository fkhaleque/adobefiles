package com.adobe.cms.exemptions.core.models.adaptiveforms;

import gov.hhs.cms.aca.global_assets.core.commons.Primitives;
import gov.hhs.cms.aca.global_assets.core.commons.SlingHelpersImpl;
import gov.hhs.cms.aca.global_assets.core.commons.W3DocumentHelpers;
import gov.hhs.cms.aca.global_assets.core.models.documentservices.DocAssuranceServiceCallerImpl;
import gov.hhs.cms.aca.global_assets.core.models.documentservices.FormsServiceCallerImpl;
import gov.hhs.cms.aca.global_assets.core.models.documentservices.OutputServiceCallerImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.cms.exemptions.core.models.exceptions.GeneratePDFException;
import com.adobe.fd.docassurance.client.api.DocAssuranceException;
import com.adobe.fd.forms.api.FormsServiceException;
import com.adobe.fd.output.api.OutputServiceException;

/**
 * GeneratePDF Implementation class. This class contains the methods required to handle a SlingHttpServletRequest forwarded by the submission
 * of an adaptive form in order to generate an interactive or non-interactive PDF document that is returned to the client via a handling servlet.
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 * @see {@link com.adobe.cms.exemptions.core.servlets.adaptiveforms.GeneratePDFServlet}
 *
 */
@Component(immediate = true, metatype = true, label = "ACA CMS Exemptions - GeneratePDFServlet Implementation", description = "ACA CMS Exemptions - GeneratePDFServlet Implementation class")
@Service
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "ACA CMS Exemptions - GeneratePDFServlet Implementation class"),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "package", value = "ACA CMS Exemptions", propertyPrivate = true)})

public class GeneratePDFImpl implements GeneratePDF{

	/* Class Statics */

	private static final Logger log = LoggerFactory.getLogger(GeneratePDFImpl.class);

	private static org.w3c.dom.Document xsltDoc = null;

	/* Class Methods */

	/* (non-Javadoc)
	 * @see com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDF#createDOR(SlingHttpServletRequest request, GeneratePDFOptionsSpec options)
	 */
	@Override
	public Document createDOR(String jcrData, GeneratePDFOptionsSpec options) throws LoginException, FormsServiceException, OutputServiceException, DocAssuranceException, IOException, GeneratePDFException{

		//Performance Metrics
		long totalTime = System.currentTimeMillis();

		//Set working variables
		Document data = null;
		Document dor = null;

		//Performance Metrics
		long xslTime = System.currentTimeMillis();
		
		//Check if an XSLT document was configured
		try {
			if(!Primitives.isNull(xsltDoc)){
				data = transformData(jcrData);
			}
			else{
				//Create XML document from string to use XPATH
				org.w3c.dom.Document xml = W3DocumentHelpers.stringToDom(jcrData);

				//Inject UUID in data using XPATH
				xml = setUUID(xml);

				//Convert the transformed data back into an AEM document
				data = W3DocumentHelpers.w3cDocToAemDoc(xml);
				data.passivate();
			}
		} catch (FactoryConfigurationError | TransformerException e) {
			throw new GeneratePDFException("Failed to tansform XML data using XSLT", e);
		} 
		
		//Performance metrics
		log.info("GeneratePDFImpl Response time (XSLT Transform): [" + (System.currentTimeMillis()-xslTime) +"]");
	

		//Handle renderAction
		if(options.getRenderAction().equals("INTERACTIVE")){
			//Create new FormsService bean
			final FormsServiceCallerImpl fsc = new FormsServiceCallerImpl();

			//Set crx:// protocol for Forms Service resource resolver
			final String templatePath = "crx://" + options.getTemplatePath();

			//Performance Metrics
			long formsTime = System.currentTimeMillis();
			
			//Render interactive document
			dor = fsc.callFormsService(templatePath, data, null);
			
			//Performance metrics
			log.info("GeneratePDFImpl Response time (PDF Render): [" + (System.currentTimeMillis()-formsTime) +"]");
		}
		else if(options.getRenderAction().equals("NON_INTERACTIVE")){
			//Create new OutputService bean
			final OutputServiceCallerImpl osc = new OutputServiceCallerImpl();

			//Get rep:SysUser resource resolver
			final ResourceResolver resolver = SlingHelpersImpl.getResourceResolver("aca-service");

			//Performance Metrics
			long templateTime = System.currentTimeMillis();
			
			//Get template document
			final Document template = new Document(options.getTemplatePath(), resolver);
			template.passivate();

			//Performance metrics
			log.info("GeneratePDFImpl Response time (Template Fetch): [" + (System.currentTimeMillis()-templateTime) +"]");
					
			//Performance Metrics
			long outputTime = System.currentTimeMillis();
			
			//Render non-interactive document
			dor = osc.generatePDFOutputDocument(template, data, null);
			
			//Performance metrics
			log.info("GeneratePDFImpl Response time (PDF Render): [" + (System.currentTimeMillis()-outputTime) +"]");
		}
		else if(options.getRenderAction().equals("DATA_IMPORT")){
			//Create new FormsService bean
			final FormsServiceCallerImpl fsc = new FormsServiceCallerImpl();

			//Get rep:SysUser resource resolver
			final ResourceResolver resolver = SlingHelpersImpl.getResourceResolver("aca-service");

			//Performance Metrics
			long templateTime = System.currentTimeMillis();
			
			//Get template document
			final Document template = new Document(options.getTemplatePath(), resolver);
			template.passivate();
			
			//Performance metrics
			log.info("GeneratePDFImpl Response time (Template Fetch): [" + (System.currentTimeMillis()-templateTime) +"]");
		
			//Performance Metrics
			long formsTime = System.currentTimeMillis();
			
			//Render non-interactive document
			dor = fsc.callFormsImportData(template, data);
			
			//Performance metrics
			log.info("GeneratePDFImpl Response time (PDF Render): [" + (System.currentTimeMillis()-formsTime) +"]");
		}
		else{
			//Fallback to non-interactive document and log warning
			log.warn("Render Action parameters did not match any directives, falling back to non-interactive render action.");

			//Create new OutputService bean
			final OutputServiceCallerImpl osc = new OutputServiceCallerImpl();

			//Get rep:SysUser resource resolver
			final ResourceResolver resolver = SlingHelpersImpl.getResourceResolver("aca-service");

			//Performance Metrics
			long templateTime = System.currentTimeMillis();
			
			//Get template document
			final Document template = new Document(options.getTemplatePath(), resolver);
			
			//Performance metrics
			log.info("GeneratePDFImpl Response time (Template Fetch): [" + (System.currentTimeMillis()-templateTime) +"]");
		
			//Performance Metrics
			long outputTime = System.currentTimeMillis();
			
			//Render non-interactive document
			dor = osc.generatePDFOutputDocument(template, data, null);
			
			//Performance metrics
			log.info("GeneratePDFImpl Response time (PDF Render): [" + (System.currentTimeMillis()-outputTime) +"]");
		}

		//Handle Reader Extensions boolean flag
		if(options.isReaderExtend()){
			final DocAssuranceServiceCallerImpl dasc = new DocAssuranceServiceCallerImpl();
			//Reader Extend form
			dor = dasc.applyUsageRights(dor, null);
		}						

		dor.passivate();

		//Performance metrics
		log.info("GeneratePDFImpl Response time (End-End): [" + (System.currentTimeMillis()-totalTime) +"]");

		//Return generate document
		return dor;
	}

	/**
	 * Method which allows XML data to be persed by the XSLT transformation as a string.
	 * This XSLT is used to unwrap Adaptive form data from it's wrapper.
	 * 
	 * @param dataString
	 * @return dataDocument
	 * @throws UnsupportedEncodingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws TransformerException
	 * @throws XPathExpressionException 
	 */
	public static Document transformData(String dataString) throws GeneratePDFException{

		Document data = null;

		try {
			//Create XML document from string to use XPATH
			org.w3c.dom.Document xml = W3DocumentHelpers.stringToDom(dataString);

			//Transform the data by passing it in with the XSLT
			xml = W3DocumentHelpers.transformXML(xml, xsltDoc);

			//Inject UUID in data using XPATH
			xml = setUUID(xml);

			//Convert the transformed data back into an AEM document
			data = W3DocumentHelpers.w3cDocToAemDoc(xml);
			data.passivate();

			return data;
		} catch (FactoryConfigurationError | TransformerException | IOException e) {
			throw new GeneratePDFException("Failed to tansform XML data using XSLT", e);
		}
	}

	/**
	 * Method used to dynamically insert GUID node under root document node
	 * 
	 * @param dom
	 * @return dom
	 * @throws RuntimeException
	 */
	private static org.w3c.dom.Document setUUID(org.w3c.dom.Document dom) throws RuntimeException{

		final String uuid = UUID.randomUUID().toString();

		dom = W3DocumentHelpers.insertNode(dom.getChildNodes().item(0).getNodeName(), "guid", uuid, dom);

		return dom;
	}

	/**
	 * Method called automatically by the event listener which detects when the service is started, 
	 * registered, or updated manually via the configuration console.
	 * 
	 * @param conf
	 * @throws org.apache.sling.api.resource.LoginException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void loadXSLT(String xsltLocation){

		//If a location is set, proceed
		if(!xsltLocation.isEmpty()){

			try {
				final ResourceResolver resolver = SlingHelpersImpl.getResourceResolver("aca-service");

				//Get XSLT with resourceResolver
				final Document xsltRes = new Document(xsltLocation, resolver);

				//Convert XSLT into a W3C document and set
				xsltDoc = W3DocumentHelpers.aemDocToW3cDoc(xsltRes);

				log.info("Service Updated - XSLT Loading Completed");

			} catch (LoginException e) {
				log.error("LoginException thrown while trying to aquire ResourceResolver", e);
			} catch (SAXException e) {
				log.error("SAXException thrown while parsing XSLT", e);
			} catch (IOException e) {
				log.error("IOException thrown while parsing XSLT", e);
			} catch (ParserConfigurationException e) {
				log.error("ParserConfigurationException thrown while parsing XSLT", e);
			} catch (NullPointerException e){
				log.warn("NullPointerException thrown while parsing XSLT. Standy for event dispatch.");
			}
		}
		else{
			//Invalidate the XSLT document if a blank location is set
			xsltDoc = null;
			log.info("Service Updated - XSLT Invalidated");
		}
	}
}
