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
import gov.hhs.cms.aca.global_assets.core.models.documentservices.OutputServiceCallerImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.ServerException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDF;
import com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDFImpl;
import com.adobe.cms.exemptions.core.models.exceptions.GeneratePDFException;
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
@SlingServlet(paths = GeneratePDF.CONTEXT_PATH + "/mock", methods = "POST", metatype = true, 
label = "ACA CMS Exemptions - MockGeneratePDFServlet", description = "ACA CMS Exemptions - MockGeneratePDFServlet class used for internal document render testing")
public class MockGeneratePDFServlet extends org.apache.sling.api.servlets.SlingAllMethodsServlet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3175115373287946196L;
	private static final Logger log = LoggerFactory.getLogger(MockGeneratePDFServlet.class);

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

		OutputStream outputStream = null;
		InputStream stream = null;
		InputStream paramStream = null;
		PrintWriter pw = null;


		final boolean isMultipart = org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent(request);

		Document templateDocument = null;
		Document dataDocument = null;
		Document dorDocument = null;

		String dataString = "";

		try {
			if (isMultipart) {
				final java.util.Map<String, org.apache.sling.api.request.RequestParameter[]> params = request.getRequestParameterMap();

				for (final java.util.Map.Entry<String, org.apache.sling.api.request.RequestParameter[]> pairs : params.entrySet()) {
					final String k = pairs.getKey();

					if(k.contains("template")) {
						//This is a Notice Type
						//Add to Notice Type Map
						final org.apache.sling.api.request.RequestParameter[] pArr = pairs.getValue();
						final org.apache.sling.api.request.RequestParameter param = pArr[0];

						try{
							paramStream = param.getInputStream();
							templateDocument = new Document(paramStream);
						} finally{
							if(!Primitives.isNull(paramStream)){
								paramStream.close();
							}
						}

						
					} else if (k.contains("data")) {
						//This is the file
						final org.apache.sling.api.request.RequestParameter[] pArr = pairs.getValue();

						dataString = pArr[0].toString();
					}
				}

				if(!Primitives.isNull(templateDocument) && !dataString.isEmpty()){

					log.info("Rendering PDF");

					//Render PDF
					OutputServiceCallerImpl osc = new OutputServiceCallerImpl();

					dataDocument = new Document(Primitives.toInputStream(dataString));

					dorDocument = osc.generatePDFOutputDocument(templateDocument, dataDocument, null);

					stream = dorDocument.getInputStream();

					//Set response headers
					response.setContentType(GeneratePDF.CONTENT_TYPE);

				}
				else if(!dataString.isEmpty()){

					log.info("Parsing data");

					//Parse through XSLT
					dataDocument = GeneratePDFImpl.transformData(dataString);
					stream = dataDocument.getInputStream();

					//Set response headers
					response.setContentType("text/plain");
				}


				/*
				 * Handle return
				 */

				//Grab the response stream
				outputStream = response.getOutputStream();

				response.setBufferSize(GeneratePDF.BUFFER_SIZE);
				response.setCharacterEncoding(GeneratePDF.CHARACTER_ENCODING);


				//If we have a stream buffer it in
				if(!Primitives.isNull(stream)){
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
			}
			else{
				log.warn("No operation requested");

				pw = response.getWriter();

				//Set response headers
				response.setContentType("text/plain");

				//Write instructions to screen
				String lineBreak = System.lineSeparator();
				String msgString = "Invalid parameters provided. Supported parameters for PDF rendering are: " + lineBreak
						+ "template(File): <template xdp>" + lineBreak
						+ "data(String): <txt or xml data file>" + lineBreak + lineBreak
						+ "Supported parameter for data transformation is: " + lineBreak
						+ "data(String): <txt or xml data file>";

				pw.write(msgString);

				pw.flush();			
			}
		} catch (OutputServiceException e) {
			log.error("Failed to create non-interactive document", e);
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create non-interactive document");
		} catch (GeneratePDFException e) {
			log.error("Failed to transform incoming data", e);
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to transform incoming data");
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
			if(!Primitives.isNull(pw)){
				pw.close();
			}
		}
	}
}
