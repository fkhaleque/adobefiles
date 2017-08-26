/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.cms.exemptions.core.servlets.adaptiveforms;

import gov.hhs.cms.aca.global_assets.core.commons.Primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.rmi.ServerException;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemds.guide.utils.GuideConstants;
import com.adobe.aemfd.docmanager.Document;
import com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDF;
import com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDFImpl;
import com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDFOptionsSpec;
import com.adobe.cms.exemptions.core.models.exceptions.GeneratePDFException;
import com.adobe.fd.docassurance.client.api.DocAssuranceException;
import com.adobe.fd.forms.api.FormsServiceException;
import com.adobe.fd.output.api.OutputServiceException;

/**
 * Servlet in charge of handling requests forwarded from adaptive form submissions. This servlet passes the request to GeneratePDFImpl
 * in order to be processed and to generate the resulting document. If the document is generated successfully, it is buffered back to screen.
 * If an exception is thrown it is caught and the current form data is stashed securely using GeneratePDFExceptionHandler. The user is 
 * then redirected to the adaptive form from which the data was submitted and a URL is crafted in order to persist the form data.    
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 * @see {@link com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDFImpl}
 * @see {@link com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDFCacheHandler}
 */

@Component(immediate = true, metatype = true, label = "ACA CMS Exemptions - GeneratePDFServlet", description = "ACA CMS Exemptions - GeneratePDFServlet class")
@Service
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "ACA CMS Exemptions - GeneratePDFServlet class"),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "package", value = "ACA CMS Exemptions", propertyPrivate = true),
	@Property(name = "sling.servlet.methods", value = "POST", propertyPrivate = true),
	@Property(name = "sling.servlet.paths", value = GeneratePDF.CONTEXT_PATH, propertyPrivate = true)})
@Property(name=JobConsumer.PROPERTY_TOPICS, value="gov/hhs/cms/aca/global_assets/core/commons/SlingHelpersImpl/activation")
public class GeneratePDFServlet extends org.apache.sling.api.servlets.SlingAllMethodsServlet implements JobConsumer, Serializable{

	/* Class Statics */

	private static final long serialVersionUID = 2598426539166789515L;

	private static final Logger log = LoggerFactory.getLogger(GeneratePDFServlet.class);

	private static Map<String, Object> cachedConfig;

	/* Configurable Properties */

	@Property(label = "XSLT Location", description = "Location of the XSLT file used to transform incoming data (relative CRX path, leave empty to skip transformation).", cardinality = 0, value  = "/content/dam/cmsexemptions/AFParser.xslt")
	private static final String XSLT_LOCATION = "com.adobe.cms.exemptions.core.servlets.adaptiveforms.GeneratePDFServlet.XSLT_LOCATION";

	@Property(label = "Render Override", description = "Override render action for ALL Adaptive Form Submissions invoking the Generate Exemptions Document Of Record action (True or False).", cardinality = 0, boolValue  = false)
	private static final String RENDER_OVERRIDE = "com.adobe.cms.exemptions.core.servlets.adaptiveforms.GeneratePDFServlet.RENDER_OVERRIDE";

	@Property(label = "Render Action Override", description = "Override render action type. If override is enabled, ALL Adaptive Form Submissions invoking the Generate Exemptions Document Of Record will render this document type (INTERACTIVE or NON_INTERACTIVE).", cardinality = 0, value  = "NON_INTERACTIVE")
	private static final String RENDER_ACTION_OVERRIDE = "com.adobe.cms.exemptions.core.servlets.adaptiveforms.GeneratePDFServlet.RENDER_ACTION_OVERRIDE";

	@Property(label = "Render Reader Extend Override", description = "Override render reader extension. If override is enabled, ALL Adaptive Form Submissions invoking the Generate Exemptions Document Of Record will be reader extended (True or False).", cardinality = 0, boolValue  = false)
	private static final String RENDER_READER_EXTEND_OVERRIDE = "com.adobe.cms.exemptions.core.servlets.adaptiveforms.GeneratePDFServlet.RENDER_READER_EXTEND_OVERRIDE";

	/* Class Methods */

	/**
	 * POST method interface where request is handled and result is buffered back to screen. If an exception is thrown, the user 
	 * is redirected, and data persisted.
	 * 
	 * Default URI: /bin/generatePDF
	 * 
	 * @param request
	 * @param response
	 * @throws ServerException
	 * @throws IOException
	 */
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {

		//Performance Metrics
		long totalTime = System.currentTimeMillis();
		
		Document dor = null;
		GeneratePDFImpl genPDF = null;
		GeneratePDFOptionsSpec options = null;

		try {
			final String jcrData = request.getParameter(GuideConstants.JCR_DATA).toString();
			final String templatePath = request.getAttribute(GeneratePDF.TEMPLATE_PATH).toString();
			final String renderAction = request.getAttribute(GeneratePDF.RENDER_ACTION).toString();

			if(Primitives.isNull(jcrData) || jcrData.isEmpty()){
				response.sendError(SlingHttpServletResponse.SC_BAD_REQUEST, "Endpoint received no data [jcr:data]");
			}
			else if(Primitives.isNull(templatePath) || templatePath.isEmpty()){
				response.sendError(SlingHttpServletResponse.SC_BAD_REQUEST, "Endpoint received no templatePath [templatePath]");
			}
			else if(Primitives.isNull(renderAction) || renderAction.isEmpty()){
				response.sendError(SlingHttpServletResponse.SC_BAD_REQUEST, "Endpoint received no renderAction [renderAction]");
			}

			//Instantiate the GeneratePDFImpl class and GeneratePDFOptionsSpec bean which wraps our service.
			genPDF = new GeneratePDFImpl();
			options = new GeneratePDFOptionsSpec();

			//Evaluate configuration overrides and request parameters
			if(Boolean.valueOf(cachedConfig.get(RENDER_OVERRIDE).toString())){
				//Proceed with service override
				options.setTemplatePath(templatePath);
				options.setRenderAction(cachedConfig.get(RENDER_ACTION_OVERRIDE).toString());
				options.setReaderExtend(Boolean.valueOf(cachedConfig.get(RENDER_READER_EXTEND_OVERRIDE).toString()));

				log.warn("[Service override detected] - Handling request with the following parameters: {Template Path: " + options.getTemplatePath() + ", Render Action: " + options.getRenderAction() + ", Reader Extend: " + options.isReaderExtend() + "}.");
			}
			else{
				//Proceed with request parameters
				options.setTemplatePath(templatePath);
				options.setRenderAction(renderAction);

				//Could be passed a null value from the Adaptive Form
				if(!Primitives.isNull(request.getAttribute(GeneratePDF.READER_EXTEND)) && request.getAttribute(GeneratePDF.READER_EXTEND).toString().equals("true")){
					options.setReaderExtend(true);
				}
				else{
					options.setReaderExtend(false);
				}

				log.info("Handling request with the following parameters: {Template Path: " + options.getTemplatePath() + ", Render Action: " + options.getRenderAction() + ", Reader Extend: " + options.isReaderExtend() + "}.");
			}

			/*
			 * Send the request to the service to have the data extracted, 
			 * transformed, and rendered with the appropriate template as a flat PDF
			 */
			log.debug("Servlet received template path: " + request.getAttribute(GeneratePDF.TEMPLATE_PATH));			
			dor = genPDF.createDOR(jcrData, options);

			if(!Primitives.isNull(dor)){
				renderOnScreen(request, response, dor);
				log.info("GeneratePDFServlet Response time (End-End): [" + (System.currentTimeMillis()-totalTime) +"]");
			}
			else{
				log.error("Failed to generate document");
				response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate document");
			}


		} catch (LoginException e) {
			log.error("Failed to read resource", e);
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to read resource");
		} catch (FormsServiceException e) {
			log.error("Failed to create interactive document", e);
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create interactive document");
		} catch (OutputServiceException e) {
			log.error("Failed to create non-interactive document", e);
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create non-interactive document");
		} catch (DocAssuranceException e) {
			log.error("Failed to apply usage rights to document", e);
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to apply usage rights to document");
		} catch (GeneratePDFException e) {
			log.error("Failed to transform incoming data", e);
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to transform incoming data");
		}
	}

	private static void renderOnScreen(SlingHttpServletRequest request, SlingHttpServletResponse response, Document dor) throws IOException, RuntimeException{

		OutputStream outputStream = null;
		InputStream stream = null;

		try {
			//Set response headers
			response.setContentType(GeneratePDF.CONTENT_TYPE);

			//Long docLen = dor.length(); //TODO .length() does not exist on PDF document?
			//Integer contLen = docLen != null ? docLen.intValue() : null;
			//response.setContentLength(contLen);

			response.setBufferSize(GeneratePDF.BUFFER_SIZE);
			response.setCharacterEncoding(GeneratePDF.CHARACTER_ENCODING);

			//Grab the streams from response and document
			outputStream = response.getOutputStream();
			stream = dor.getInputStream();

			//If we have a stream buffer it in
			if(stream != null){
				//Buffer input stream into the output stream
				byte[] buffer = new byte[GeneratePDF.BUFFER_SIZE]; // Adjust if you want
				int bytesRead = 0;

				//While we have bytes, buffer
				while (bytesRead != -1){
					bytesRead = stream.read(buffer);

					if(bytesRead != -1){
						outputStream.write(buffer, 0, bytesRead);
					}
				}
			}

			//Flush and close streams
			outputStream.flush();

		} catch (Exception e) {
			throw new IOException("Failed to buffer response to stream", e);
		} finally{
			//Always close streams
			if(!Primitives.isNull(outputStream)){
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error("Failed to close streams", e);
				}
			}
			if(!Primitives.isNull(stream)){
				try {
					stream.close();
				} catch (IOException e) {
					log.error("Failed to close streams", e);
				}
			}
		}
	}

	/* Binding Methods */	

	@Activate
	protected void activate(final Map<String, Object> config) {
		cachedConfig = config;

		//Cache XSLT
		GeneratePDFImpl.loadXSLT(cachedConfig.get(XSLT_LOCATION).toString());

		log.info("[Service Activated]");
	}

	@Deactivate
	protected void deactivate() {
		log.info("[Service Deactivated]");
	}

	/**
	 * Asynchronous event based XSLT caching if Global is deployed after Exemptions
	 */
	@Override
	public JobResult process(final Job job) {
		log.info("Event dispatch received.");
		
		boolean activated = false;
		Object propObject = job.getProperty("activated");

		if(propObject != null){
			activated = (boolean) job.getProperty("activated");

			if(activated){
				GeneratePDFImpl.loadXSLT(cachedConfig.get(XSLT_LOCATION).toString());
				return JobResult.OK;
			}
		}

		return JobResult.FAILED;
	}
}
