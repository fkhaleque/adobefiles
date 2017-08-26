package gov.hhs.cms.aca.global_assets.core.models.documentservices;

import gov.hhs.cms.aca.global_assets.core.commons.Primitives;

import java.util.Map;

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

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.forms.api.FormsService;
import com.adobe.fd.forms.api.FormsServiceException;
import com.adobe.fd.forms.api.PDFFormRenderOptions;

/**
 * This class is used to wrap the AEM Forms Document Services Forms Service calls. It binds a new configurable service (ACA Global Assets- Forms Service Caller) to the AEM 
 * server which can be configured via the /configMgr portion of the console.
 *  
 * @see {@link <a href="http://felix.apache.org/documentation/subprojects/apache-felix-maven-scr-plugin/scr-annotations.html">SCR Annotations</a>}
 * @see {@link <a href="https://helpx.adobe.com/aem-forms/6-1/javadocs/com/adobe/fd/forms/api/FormsService.html">Forms Service</a>}
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 *
 */

@Component(immediate = true, metatype = true,
label = "ACA Global Assets - Forms Service Caller", description = "Custom implementation of the Document Services Forms Service")
@Service
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "ACA Global Assets - Custom implementation of the Document Services Forms Services"),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "package", value = "ACA Global Assets", propertyPrivate = true)})

public class FormsServiceCallerImpl implements FormsServiceCaller{

	/* Class Statics */
	
	private static final Logger log = LoggerFactory.getLogger(FormsServiceCallerImpl.class);
	
	private static Map<String, Object> cachedConfig;
	
	/* Configurable Properties */
	
	@Property(label = "Tagged PDF", description = "Require FormsServiceCallerImpl to produce a Section 508 compliant document (True or False)", cardinality = 0, boolValue = true)
	private static final String TAGGED_PDF = "gov.hhs.cms.aca.global_assets.core.models.documentservices.FormsServiceCallerImpl.TAGGED_PDF";
	
	@Property(label = "Acrobat Version", description = "Acrobat Version compliance of the documents created by FormsServiceCallerImpl (Acrobat_10, Acrobat_10_1, or Acrobat_11)", cardinality = 0, value  = "Acrobat_10")
	private static final String ACROBAT_VERSION = "gov.hhs.cms.aca.global_assets.core.models.documentservices.FormsServiceCallerImpl.ACROBAT_VERSION";
	
	@Property(label = "Document Locale", description = "Set the default locale for documents created by FormsServiceCallerImpl (en_US)", cardinality = 0, value = "en_US")
	private static final String LOCALE = "gov.hhs.cms.aca.global_assets.core.models.documentservices.FormsServiceCallerImpl.LOCALE";
	
	/* Service References */
	
	@Reference
	private static FormsService formsService;
	
	/* Class construct */
	
	public FormsServiceCallerImpl() {
		super();
		//Empty Construct
	}
	
	/* Class Methods */

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.models.documentservices.FormsService#callFormsService(java.lang.String, com.adobe.aemfd.docmanager.Document, com.adobe.fd.forms.api.PDFFormRenderOptions)
	 */
	@Override
	public Document callFormsService(String templateLoc, Document data, PDFFormRenderOptions formsOptions) throws FormsServiceException {
		
		//Create new PDFFormRenderOptions bean
		if(Primitives.isNull(formsOptions)){
			formsOptions = new PDFFormRenderOptions();
						
			//Set formsOptions based on bundle configuration
			//Set ACROBAT_VERSION
			final String version = cachedConfig.get(ACROBAT_VERSION).toString();
			
			if(version.equals("Acrobat_10")){ 
				formsOptions.setAcrobatVersion(com.adobe.fd.forms.api.AcrobatVersion.Acrobat_10);
			} 
			else if(version.equals("Acrobat_10_1")) {
				formsOptions.setAcrobatVersion(com.adobe.fd.forms.api.AcrobatVersion.Acrobat_10_1);
			} 
			else if(version.equals("Acrobat_11")) {             
				formsOptions.setAcrobatVersion(com.adobe.fd.forms.api.AcrobatVersion.Acrobat_11);
			}
			else{
				log.warn("Acrobat version set to [" + version + "] did not match any directives. Falling back to [Acrobat_10].");
				formsOptions.setAcrobatVersion(com.adobe.fd.forms.api.AcrobatVersion.Acrobat_10);
			}

			//Set TAGGED_PDF
			if (cachedConfig.get(TAGGED_PDF).toString().equalsIgnoreCase("true") ) {
				formsOptions.setTaggedPDF(true );
			}

			//Set LOCALE
			if(cachedConfig.get(LOCALE)!=null){
				formsOptions.setLocale(cachedConfig.get(LOCALE).toString());
			}
		}

		//Log settings
		log.info("Calling Forms Service with: {Operation: renderPDFForm(), Tagged PDF: " + formsOptions.getTaggedPDF() + ", Acrobat Version: " + formsOptions.getAcrobatVersion() + ", Locale: " + formsOptions.getLocale() + "}");
		
		//Call FormsService with provided elements
		final Document doc = formsService.renderPDFForm(templateLoc, data, formsOptions);
		
		log.debug("FormsServiceCallerImpl.callFormsService() successfully invoked.");

		return doc;

	}
	
	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.models.documentservices.FormsService#callFormsImportData(com.adobe.aemfd.docmanager.Document, com.adobe.aemfd.docmanager.Document)
	 */
	@Override
	public Document callFormsImportData(Document pdf, Document data) throws FormsServiceException{
		
		//Log settings
		log.info("Calling Forms Service with: {Operation: importData()}");	

		final Document doc = formsService.importData(pdf, data);
		
		log.debug("FormsServiceCallerImpl.callFormsImportData() successfully invoked.");

		return doc;
		
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

	protected void bindFormsService(FormsService injected){
		log.info("[Binding FormsService]");
		formsService = injected;
		log.info("[Bound FormsService]");
	}

}
