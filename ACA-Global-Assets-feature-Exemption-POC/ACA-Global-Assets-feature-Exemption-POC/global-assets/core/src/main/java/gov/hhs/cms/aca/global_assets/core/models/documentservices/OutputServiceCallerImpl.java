package gov.hhs.cms.aca.global_assets.core.models.documentservices;

import gov.hhs.cms.aca.global_assets.core.commons.Primitives;
import gov.hhs.cms.aca.global_assets.core.commons.W3DocumentHelpers;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.output.api.BatchOptions;
import com.adobe.fd.output.api.BatchResult;
import com.adobe.fd.output.api.OutputService;
import com.adobe.fd.output.api.OutputServiceException;
import com.adobe.fd.output.api.PDFOutputOptions;

/**
 * This class is used to wrap the AEM Forms Document Services Output Service calls. It binds a new configurable service (ACA Global Assets- Output Service Caller) to the AEM 
 * server which can be configured via the /configMgr portion of the console.
 *  
 * @see {@link <a href="http://felix.apache.org/documentation/subprojects/apache-felix-maven-scr-plugin/scr-annotations.html">SCR Annotations</a>}
 * @see {@link <a href="https://helpx.adobe.com/aem-forms/6-1/javadocs/com/adobe/fd/output/api/OutputService.html">Output Service</a>}
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 *
 */

@Component(immediate = true, metatype = true,
label = "ACA Global Assets - Output Service Caller", description = "Custom implementation of the Document Services Output Service")
@Service
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "ACA Global Assets - Custom implementation of the Document Services Output Service"),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "package", value = "ACA Global Assets", propertyPrivate = true)})

public class OutputServiceCallerImpl implements OutputServiceCaller{

	/* Class Statics */

	private static final Logger log = LoggerFactory.getLogger(OutputServiceCallerImpl.class);

	private static Map<String, Object> cachedConfig;

	/* Configurable Properties */

	@Property(label = "Tagged PDF", description = "Require OutputServiceCallerImpl to produce a Section 508 compliant document (True or False)", cardinality = 0, boolValue = true)
	private static final String TAGGED_PDF = "gov.hhs.cms.aca.global_assets.core.models.documentservices.OutputServiceCallerImpl.TAGGED_PDF";

	@Property(label = "Linearized PDF", description = "Require OutputServiceCallerImpl to produce a linearized document (True or False)", cardinality = 0, boolValue = true)
	private static final String LINEARIZED_PDF = "gov.hhs.cms.aca.global_assets.core.models.documentservices.OutputServiceCallerImpl.LINEARIZED_PDF";

	@Property(label = "Acrobat Version", description = "Acrobat Version compliance of the documents created by OutputServiceCallerImpl (Acrobat_10, Acrobat_10_1, or Acrobat_11)", cardinality = 0, value  = "Acrobat_10")
	private static final String ACROBAT_VERSION = "gov.hhs.cms.aca.global_assets.core.models.documentservices.OutputServiceCallerImpl.ACROBAT_VERSION";

	@Property(label = "Document Locale", description = "Set the default locale for documents created by OutputServiceCallerImpl (en_US)", cardinality = 0, value = "en_US")
	private static final String LOCALE = "gov.hhs.cms.aca.global_assets.core.models.documentservices.OutputServiceCallerImpl.LOCALE";

	/* Service References */

	@Reference
	private static OutputService outputService;

	/* Class construct */

	public OutputServiceCallerImpl() {
		super();
		//Empty Construct
	}

	/* Class Methods */

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.models.documentservices.OutputService#callOutputService(com.adobe.aemfd.docmanager.Document, com.adobe.aemfd.docmanager.Document, com.adobe.fd.output.api.PDFOutputOptions)
	 */
	@Override
	public Document generatePDFOutputDocument(Document template, Document data, PDFOutputOptions outputOptions) throws OutputServiceException{

		//Create new PDFOutputOptions bean
		if(Primitives.isNull(outputOptions)){
			outputOptions = setUpPDFOutputOptions();
		}

		//Log settings
		log.info("Calling Output Service with: {Operation: generatePDFOutput(), Tagged PDF: " + outputOptions.getTaggedPDF() + ", Linearized PDF: " + outputOptions.getLinearizedPDF() + ", Acrobat Version: " + outputOptions.getAcrobatVersion() + ", Locale: " + outputOptions.getLocale() + "}");

		//Call OutputService with provided elements
		final Document doc = outputService.generatePDFOutput(template, data, outputOptions); 

		log.debug("OutputServiceCallerImpl.callOutputService() successfully invoked.");

		return doc;

	}

	@Override
	public BatchResult generatePDFOutputBatch(Map<String, String> templates, Map<String, Document> dataDocs, PDFOutputOptions outputOptions, BatchOptions batchOptions) throws OutputServiceException{

		//Create new PDFOutputOptions bean
		if(Primitives.isNull(outputOptions)){
			outputOptions = setUpPDFOutputOptions();
		}
		//Create new BatchOptions bean
		if(Primitives.isNull(batchOptions)){
			batchOptions = new BatchOptions();
		}

		//Log settings
		log.info("Calling Output Service with: {Operation: generatePDFOutputBatch(), Tagged PDF: " + outputOptions.getTaggedPDF() + ", Linearized PDF: " + outputOptions.getLinearizedPDF() + ", Acrobat Version: " + outputOptions.getAcrobatVersion() + ", Locale: " + outputOptions.getLocale() + "}");

		//Call OutputService with provided elements
		final BatchResult batchResult = outputService.generatePDFOutputBatch(templates, dataDocs, outputOptions, batchOptions);

		log.debug("OutputServiceCallerImpl.callOutputService() successfully invoked.");

		return batchResult;
	}

	@Override
	public int getPDFPageCount(Document metaDataDocument){

		try {
			//Transform AEM document to XML
			org.w3c.dom.Document xmlMetaDocument = W3DocumentHelpers.aemDocToW3cDoc(metaDataDocument);

			//Get XPATH value of first metadata record
			String pageCountString = W3DocumentHelpers.getXPathValue("/printBatchMetaData/recordMetaData/pages", xmlMetaDocument);

			//Parse XPATH value
			return Integer.parseInt(pageCountString);

		} catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
			log.error("Exception thrown while getting page count. Defaulting to [0].", e);
		}

		return 0;
	}

	private static PDFOutputOptions setUpPDFOutputOptions(){

		PDFOutputOptions outputOptions = new PDFOutputOptions();

		//Set outputOptions based on bundle configuration
		//Set ACROBAT_VERSION
		final String version = cachedConfig.get(ACROBAT_VERSION).toString();

		if(version.equals("Acrobat_10")){ 
			outputOptions.setAcrobatVersion(com.adobe.fd.output.api.AcrobatVersion.Acrobat_10);
		} 
		else if(version.equals("Acrobat_10_1")) {
			outputOptions.setAcrobatVersion(com.adobe.fd.output.api.AcrobatVersion.Acrobat_10_1);
		} 
		else if(version.equals("Acrobat_11")) {             
			outputOptions.setAcrobatVersion(com.adobe.fd.output.api.AcrobatVersion.Acrobat_11);
		}
		else{
			log.warn("Acrobat version set to [" + version + "] did not match any directives. Falling back to [Acrobat_10].");
			outputOptions.setAcrobatVersion(com.adobe.fd.output.api.AcrobatVersion.Acrobat_10);
		}

		//Set TAGGED_PDF
		if (cachedConfig.get(TAGGED_PDF).toString().equalsIgnoreCase("true") ) {
			outputOptions.setTaggedPDF(true );
		}

		//Set LINEARIZED_PDF
		if (cachedConfig.get(LINEARIZED_PDF).toString().equalsIgnoreCase("true") ) {
			outputOptions.setLinearizedPDF(true);
		}

		//Set LOCALE
		if(cachedConfig.get(LOCALE)!=null){
			outputOptions.setLocale(cachedConfig.get(LOCALE).toString());
		}

		return outputOptions;		
	}


	/* Binding Methods */	

	@Activate
	protected void activate(final Map<String, Object> config) {
		cachedConfig = config;
		log.info("[Service Activated]");
	}

	@Deactivate
	protected void deactivate() {
		log.info("[Service Deactivated]");
	}

	protected void bindOutputService(OutputService injected){
		log.info("[Binding OutputService]");
		outputService = injected;
		log.info("[Bound OutputService]");
	}

}
