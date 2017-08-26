package gov.hhs.cms.aca.global_assets.core.commons;

import gov.hhs.cms.aca.global_assets.core.models.GlobalAssets;

import java.io.IOException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helpers class contains helper methods from service loaders to workflow data persistence and resource resolver. 
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 *
 */

@Component(metatype = true, immediate = true, label = "ACA Global Assets - SlingHelpers", description = "Collection of Sling and JCR API tools")
@Service
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "ACA Global Assets - Collection of Sling and JCR API tools"),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "package", value = "ACA Global Assets", propertyPrivate = true)})

public class SlingHelpersImpl implements GlobalAssets{

	/* Class Statics */

	private static final Logger log = LoggerFactory.getLogger(SlingHelpersImpl.class);

	/* Service References */

	@Reference
	private static ConfigurationAdmin configurationAdmin;

	@Reference
	private static ResourceResolverFactory resourceResolverFactory;
	
	@Reference 
	private static JobManager jobManager;

	private static Configuration conf = null;

	/* Class Methods */

	/**
	 * Helper method to retrieve ResourceResolver using current session
	 * 
	 * @param session
	 * @return ResourceResolver
	 * @throws LoginException
	 * @throws org.apache.sling.api.resource.LoginException
	 */
	public static ResourceResolver getResourceResolver(Session session) throws org.apache.sling.api.resource.LoginException {
		return resourceResolverFactory.getResourceResolver(Collections.<String, Object>singletonMap(JcrResourceConstants.AUTHENTICATION_INFO_SESSION,
				session));
	}

	/**
	 * Helper method to retrieve ResourceResolver using a System User
	 * 
	 * @param sysUser
	 * @return ResourceResolver
	 * @throws org.apache.sling.api.resource.LoginException
	 */
	public static ResourceResolver getResourceResolver(String sysUser) throws org.apache.sling.api.resource.LoginException {

		//Use rep:SysUser to load ResourceResolver and fetch resource
		Map<String, Object> param = new HashMap<String, Object>();

		//Name of rep:SysUser
		param.put(ResourceResolverFactory.SUBSERVICE, sysUser); 
		final ResourceResolver resolver;

		//Get resolver
		resolver = resourceResolverFactory.getServiceResourceResolver(param);

		return resolver;
	}

	/**
	 * Helper method to retrieve OSGI service dynamically where SCR annotations may not be able to inject a service definition.
	 * 
	 * @param caller
	 * @param service
	 * @return (castable) Object
	 * @throws Exception 
	 * @sample OutputService outputService = (OutputService) Helpers.getAmbiguousService(this, OutputService.class);
	 */
	public static Object getAmbiguousService(Object caller, @SuppressWarnings("rawtypes") Class service){

		final BundleContext bundleContext = FrameworkUtil.getBundle(caller.getClass()).getBundleContext();
		final ServiceReference factoryRef = bundleContext.getServiceReference(service.getName());

		final Object object = bundleContext.getService(factoryRef);

		return object;
	}

	/**
	 * Helper method to retrive a service property
	 * 
	 * @param caller
	 * @param propertyName
	 * @return value
	 * @throws Exception 
	 */
	public static String getServiceProperty(Object caller, String property){

		final BundleContext bundleContext = FrameworkUtil.getBundle(caller.getClass()).getBundleContext();
		final String value = bundleContext.getProperty(property);

		return value;
	}

	/**
	 * Helper method to get configuration based on PID
	 * 
	 * @param pid
	 * @return Dictionary<?, ?>
	 * @throws IOException 
	 * @throws Exception 
	 * @see {@link org.apache.sling.commons.osgi.PropertiesUtils}
	 */
	public static Dictionary<?, ?> getConfig(String pid) throws IOException {

		if(!Primitives.isNull(configurationAdmin)){
			conf = configurationAdmin.getConfiguration(pid);

			if(!Primitives.isNull(conf)){
				return conf.getProperties();
			}
			else{
				throw new NullPointerException("Configuration for pid " + pid + " is null");
			}
		}
		else{
			throw new NullPointerException("Failed to retrieve configuration for pid " + pid + ". Configuration Admin is null.");
		}
	}

	/**
	 * Helper method to get specific configuration value based on PID
	 * @param pid
	 * @param key
	 * @return Dictionary<?, ?>
	 * @throws IOException 
	 * @throws Exception 
	 * @see {@link org.apache.sling.commons.osgi.PropertiesUtils}
	 */
	public static String getConfig(String pid, String key) throws IOException {
		if(!Primitives.isNull(configurationAdmin)){
			conf = configurationAdmin.getConfiguration(pid);
			if(!Primitives.isNull(conf)){
				if(!Primitives.isNull(conf.getProperties()) && !Primitives.isNull(conf.getProperties().get(key))){
					return conf.getProperties().get(key).toString();
				}
				else{
					throw new NullPointerException("Configuration property " + key + " for pid " + pid + " is null");
				}
			}
			else{
				throw new NullPointerException("Configuration for pid " + pid + " is null");
			}
		}
		else{
			throw new NullPointerException("Failed to retrieve configuration for pid " + pid + ". Configuration Admin is null.");
		}
	}

	/* Binding Methods */	

	@Activate
	protected void activate() {
		dispatchActivation();
		log.info("[Service Activated]");
	}

	@Deactivate
	protected void deactivate() {
		log.info("[Service Deactivated]");
	}

	protected void bindConfigurationAdmin(ConfigurationAdmin injected){
		log.info("[Binding ConfigurationAdmin]");
		configurationAdmin = injected;
		log.info("[Bound ConfigurationAdmin]");
	}

	protected void bindResourceResolverFactory(ResourceResolverFactory injected){
		log.info("[Binding ResourceResolverFactory]");
		resourceResolverFactory = injected;
		log.info("[Bound ResourceResolverFactory]");
	}
	
	protected void bindJobManager(JobManager injected){
		log.info("[Binding JobManager]");
		jobManager = injected;
		log.info("[Bound JobManager]");
	}
	
	public void dispatchActivation() {
		log.info("Dispatching activation event");
        final Map<String, Object> props = new HashMap<String, Object>();
        props.put("activated", true);
        jobManager.addJob("gov/hhs/cms/aca/global_assets/core/commons/SlingHelpersImpl/activation", props); 
    } 
}
