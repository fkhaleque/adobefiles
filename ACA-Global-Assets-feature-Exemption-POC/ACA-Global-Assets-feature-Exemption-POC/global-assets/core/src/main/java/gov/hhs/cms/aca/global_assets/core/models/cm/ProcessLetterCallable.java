package gov.hhs.cms.aca.global_assets.core.models.cm;

import gov.hhs.cms.aca.global_assets.core.models.exceptions.LetterGenerationException;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.livecycle.content.sling.ResourceResolverHolder;

/**
 * Callable<Type> implementation in order to invoke the Correspondence Management API within a thread specific ResourceResolver
 * allowing these APIs to be called within the context of an AEM Workflow. This relates to a bug documented as part of the development
 * of this feature for which a future release fix has been requested.
 * 
 * @see {@link <a href="https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Callable.html">Interface Callable</a>}
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 *
 */
public class ProcessLetterCallable implements Callable<Map<String, Object>> {
	
	final private String letterId;
	final private String letterData;
	
	private static final Logger log = LoggerFactory.getLogger(ProcessLetterCallable.class);
	
	public ProcessLetterCallable(String letterId, String letterData, ResourceResolver resolver){
		this.letterId = letterId;
		this.letterData = letterData;
		ResourceResolverHolder.setResourceResolver(resolver);
	}
	
	@Override
	public Map<String, Object> call() throws LetterGenerationException {
		
		log.debug("LetterRenderServiceCallable Callable<T> call() running with letterId: [" + letterId + "] and letterData: [Size: " + letterData.length() + "]"); 
		
		final LetterServiceCallerImpl letterService = new LetterServiceCallerImpl();
		
		return letterService.processCmLetter(this.letterId, this.letterData, null);
	}
}