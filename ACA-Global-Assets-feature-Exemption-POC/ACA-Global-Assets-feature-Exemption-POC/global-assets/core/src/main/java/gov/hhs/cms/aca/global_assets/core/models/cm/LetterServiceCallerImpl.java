package gov.hhs.cms.aca.global_assets.core.models.cm;

import gov.hhs.cms.aca.global_assets.core.commons.Primitives;
import gov.hhs.cms.aca.global_assets.core.models.exceptions.LetterGenerationException;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.icc.dbforms.obj.Letter;
import com.adobe.icc.dbforms.obj.LetterRenderOptionsSpec;
import com.adobe.icc.ddg.api.LetterRenderService;
import com.adobe.icc.render.obj.PDFResponseType;
import com.adobe.icc.services.api.LetterService;

/**
 * This class is used to wrap the AEM Forms Correspondence Management Letter Service calls. It binds a new configurable service (ACA Global Assets- CM Letter Service Caller) to the AEM 
 * server which can be configured via the /configMgr portion of the console.
 *  
 * @see {@link <a href="https://helpx.adobe.com/aem-forms/6-1/javadocs/com/adobe/icc/ddg/api/LetterRenderService.html">Letter Render Service</a>}
 * @see {@link <a href="https://helpx.adobe.com/aem-forms/6-1/javadocs/com/adobe/icc/ddg/api/LetterService.html">Letter Service</a>}
 * @see {@link <a href="https://helpx.adobe.com/aem-forms/6-1/javadocs/com/adobe/icc/dbforms/obj/LetterRenderOptionsSpec.html">Letter Render Options Spec</a>}
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 *
 */

@Component(metatype = true, immediate = true, label = "ACA Global Assets - CM Letter Service Caller", description = "Custom implementation of the Correspondence Management LetterService and LetterRenderService")
@Service
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "ACA Global Assets - Custom implementation of the CM Letter and LetterRender Services"),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "package", value = "ACA Global Assets", propertyPrivate = true)})

public class LetterServiceCallerImpl implements LetterServiceCaller {

	/* Class Statics */
	
	private static final Logger log = LoggerFactory.getLogger(LetterServiceCallerImpl.class);
	
	private static Map<String, Object> cachedConfig;
	
	/* Configurable Properties */
	
	@Property(label = "Merge Data On Server", description = "Sets flag indicating whether data should be merged on server or not. (True or False)", cardinality = 0, boolValue = true)
	private static final String SERVER_MERGE = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.SERVER_MERGE";
	
	@Property(label = "Use Test Data", description = "Sets flag indicating whether to use test data or not. (True or False)", cardinality = 0, boolValue = true)
	private static final String TEST_DATA = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.TEST_DATA";
	
	@Property(label = "Render Interactive", description = "Sets flag indicating whether to render interactive or non interactive PDF. (True or False)", cardinality = 0, boolValue = false)
	private static final String RENDER_INTERACTIVE = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.RENDER_INTERACTIVE";
	
	@Property(label = "Save To File", description = "Sets flag indicating whether to save PDF into temporary file or not. (True or False)", cardinality = 0, boolValue = false)
	private static final String FILE_SAVE = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.FILE_SAVE";
	
	@Property(label = "Use XFA Bullets", description = "Sets flag indicating whether to use XFA Bullets or not. (True or False)", cardinality = 0, boolValue = false)
	private static final String XFA_BULLETS = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.XFA_BULLETS";
	
	@Property(label = "Use HTML Bullets", description = "Sets flag indicating whether to use HTML Bullets or not. (True or False)", cardinality = 0, boolValue = true)
	private static final String HTML_BULLETS = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.HTML_BULLETS";
	
	@Property(label = "Letter Locale", description = "Sets the locale that should be used for letter data resolution. (en_US)", cardinality = 0, value = "en_US")
	private static final String LOCALE = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.LOCALE";
	
	@Property(label = "Include Attachments", description = "Sets flag indicating whether to include letter attachements or not. (True or False)", cardinality = 0, boolValue = true)
	private static final String INCLUDE_ATTACH = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.INCLUDE_ATTACH";
	
	@Property(label = "Include Fragments", description = "Sets flag indicating whether to include fragments without evauating conditions. (True or False)", cardinality = 0, boolValue = false)
	private static final String INCLUDE_FRAGMENTS = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.INCLUDE_FRAGMENTS";
	
	@Property(label = "Support Reload", description = "Sets flag indicating whether the rendered letter will support reloading or not. (True or False)", cardinality = 0, boolValue = false)
	private static final String SUPPORT_RELOAD = "gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCallerImpl.SUPPORT_RELOAD";
	
	/* Service References */
	
	@Reference
	private static LetterRenderService letterRenderService;

	@Reference
	private static LetterService letterService;

	/* Class Methods */
	
	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCaller#renderCmLetter(java.lang.String, java.lang.String)
	 */
	@Override
	public Document renderCmLetter(String letterId, String data, LetterRenderOptionsSpec renderSpec) throws LetterGenerationException{
				
		final PDFResponseType response;
		
		//Use service defaults
		if(Primitives.isNull(renderSpec)){
			renderSpec = setUpLetterRenderOptionsSpec();		
		}
		
		//Invoke CM service method
		try{
			response = letterRenderService.renderLetter(letterId, data, renderSpec);
		} catch (Exception e){
			throw new LetterGenerationException("Failed to render Letter", e);
		}

		//Formulate document from response bytes
		final Document letterDoc = new Document(response
				.getFile()
				.getDocument());	

		return letterDoc;
	}
	
	@Override
	public Map<String, Object> processCmLetter(String letterId, String data, LetterRenderOptionsSpec renderSpec) throws LetterGenerationException{
		
		//Use service defaults
		if(Primitives.isNull(renderSpec)){
			renderSpec = setUpLetterRenderOptionsSpec();		
		}
		
		//Invoke CM service method
		try{
			return letterRenderService.processLetter(letterId, data, renderSpec);
		} catch (Exception e){
			throw new LetterGenerationException("Failed to render Letter", e);
		}
	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCaller#publishCmLetter(java.lang.String)
	 */
	@Override
	public void publishCmLetter(String letterId){
		letterService.publishLetter(letterId);
	}
	
	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCaller#getLetter(java.lang.String)
	 */
	@Override
	public Letter getLetter(String letterId){
		return letterService.getLetterWithoutData(letterId);
	}
	
	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCaller#letterExists(java.lang.String)
	 */
	@Override
	public boolean letterExists(String letterName){
		return letterService.letterExists(letterName);
	}
	
	private static LetterRenderOptionsSpec setUpLetterRenderOptionsSpec(){
		
		LetterRenderOptionsSpec renderSpec = new LetterRenderOptionsSpec();
		
		//Render Specific options
		renderSpec.setMergeDataOnServer(PropertiesUtil.toBoolean(cachedConfig.get(SERVER_MERGE), true));
		renderSpec.setUseTestData(PropertiesUtil.toBoolean(cachedConfig.get(TEST_DATA), true));
		renderSpec.setRenderInteractive(PropertiesUtil.toBoolean(cachedConfig.get(RENDER_INTERACTIVE), false));
		renderSpec.setSaveToFile(PropertiesUtil.toBoolean(cachedConfig.get(FILE_SAVE), false));
		
		//Letter specific options
		renderSpec.setUseXFABullets(PropertiesUtil.toBoolean(cachedConfig.get(XFA_BULLETS), false));
		renderSpec.setUseHTMLBullets(PropertiesUtil.toBoolean(cachedConfig.get(HTML_BULLETS), false));
		renderSpec.setLocale(PropertiesUtil.toString(cachedConfig.get(LOCALE), "en_US"));
		renderSpec.setIncludeAttachments(PropertiesUtil.toBoolean(cachedConfig.get(INCLUDE_ATTACH), true));
		renderSpec.setIncludeFragmentsUnconditionally(PropertiesUtil.toBoolean(cachedConfig.get(INCLUDE_FRAGMENTS), false));
		renderSpec.setSupportReload(PropertiesUtil.toBoolean(cachedConfig.get(SUPPORT_RELOAD), false));	
		
		return renderSpec;		
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

	protected void bindLetterRenderService(LetterRenderService injected){
		log.info("[Binding LetterRenderService]");
		letterRenderService = injected;
		log.info("[Bound LetterRenderService]");
	}

	protected void bindLetterService(LetterService injected){
		log.info("[Binding LetterService]");
		letterService = injected;
		log.info("[Bound LetterService]");
	}
}
