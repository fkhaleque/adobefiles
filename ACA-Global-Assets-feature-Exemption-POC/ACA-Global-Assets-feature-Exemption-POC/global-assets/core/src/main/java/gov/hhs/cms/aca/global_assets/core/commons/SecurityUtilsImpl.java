package gov.hhs.cms.aca.global_assets.core.commons;

import gov.hhs.cms.aca.global_assets.core.models.GlobalAssets;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(metatype = true, immediate = true, label = "ACA Global Assets - SecurityUtilsImpl", description = "Collection of security related methods")
@Service
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "ACA Global Assets - Collection of security related methods"),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "package", value = "ACA Global Assets", propertyPrivate = true)})
public class SecurityUtilsImpl implements GlobalAssets{

	@Property(label = "Invalid Character Detection Pattern", description = "Regular expression that rejects any invalid characters from incoming data", cardinality = 0, value = "[^\\w\\s\\-\\.]")
	private static final String INVALID_CHAR_PATTERN = "gov.hhs.cms.aca.global_assets.core.commons.SecurityUtilsImpl.INVALID_CHAR_PATTERN";

	/* Class Statics */

	private static final Logger log = LoggerFactory.getLogger(SecurityUtilsImpl.class);

	private static Map<String, Object> cachedConfig;
	
	public static boolean validateQueryString(RequestParameterMap paramMap) throws SecurityUtilException{

		//Loop through all param keys
		for (Entry<String, RequestParameter[]> entry : paramMap.entrySet()){
			
			log.debug("Validating Query String Param: [" + entry.getKey() + "].");
			validateStringValue(entry.getKey());
			
			RequestParameter[] requestParameters = entry.getValue();
			
			//Loop through all param values
			for(int i=0; i<requestParameters.length; i++){
				log.debug("Validating Query String Value: [" + requestParameters[i].getString() + "].");
				log.debug("Filename: " + requestParameters[i].getFileName());
				
				if(Primitives.isNull(requestParameters[i].getFileName())){
					
					//This is a simple type parameter, validate the string value
					validateStringValue(requestParameters[i].getString());
				}
			}
		}
		return true;
	}

	/**
	 * Method to validate value against invalid characters.
	 * @param value
	 * @return boolean
	 * @throws SecurityUtilException 
	 */
	public static boolean validateStringValue(String value) throws SecurityUtilException{

		// String to be scanned to find the pattern.
		String pattern = cachedConfig.get(INVALID_CHAR_PATTERN).toString();

		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(value);
		
		if(m.find()){
			throw new SecurityUtilException("Query string parameter failed validation: [" + value + "].");
		}
		else{
			return true;
		}
	}
	
	/* Binding Methods */	

	@Activate
	protected void activate(final Map<String, Object> config) throws Exception {
		cachedConfig = config;
		log.info("[Service Activated]");
	}

	@Deactivate
	protected void deactivate() throws Exception {
		log.info("[Service Deactivated]");
	}
}
