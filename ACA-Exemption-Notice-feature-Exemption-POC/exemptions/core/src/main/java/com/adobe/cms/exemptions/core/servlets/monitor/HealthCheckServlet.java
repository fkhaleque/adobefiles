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
package com.adobe.cms.exemptions.core.servlets.monitor;

import gov.hhs.cms.aca.global_assets.core.commons.Primitives;
import gov.hhs.cms.aca.global_assets.core.servlets.monitor.HealthCheckConstants;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Health check endpoint to test that the bundle is running and operational.
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 * @see {@link com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDFCacheHandler}
 */
@SlingServlet(paths = HealthCheckConstants.CONTEXT_URL + HealthCheckServlet.packageName, methods = "GET", metatype=true,
label = "ACA CMS Exemptions - HealthCheck Servlet", description = "ACA CMS Exemptions - HealthCheck Servlet class")
public class HealthCheckServlet extends SlingSafeMethodsServlet {

	/* Class Statics */

	private static final Logger log = LoggerFactory.getLogger(HealthCheckServlet.class);
	private static final long serialVersionUID = -2292089883572438953L;
	protected static final String packageName = "cmsexemptions";

	/* Class Methods */

	@Override
	protected void doGet(final SlingHttpServletRequest request,
			final SlingHttpServletResponse response) throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		JSONObject json = new JSONObject();
		JSONObject test = new JSONObject();
		JSONObject tests = new JSONObject();

		try {

			//TODO Implement testing logic

			json.put(HealthCheckConstants.RESULT_KEY, "Success");
			json.put(HealthCheckConstants.PACKAGE_KEY, packageName);

			//Test performed
			test.put(HealthCheckConstants.TEST_NAME, "None");
			test.put(HealthCheckConstants.TEST_DESC, "An empty test scenario which only validates the bundle state.");
			test.put(HealthCheckConstants.TEST_PASS, true);

			//Add test to tests
			tests.put(HealthCheckConstants.TEST_KEY, test);

			//Add tests to response
			json.put(HealthCheckConstants.TESTS_KEY, tests);


			//Write response
			pw.write(json.toString(4));

			//Flush and close
			pw.flush();
			pw.close();


		} catch (JSONException e) {
			log.error("Health Check failed validation", e);
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "HealthCheck [FAIL] for: [" + packageName + "].");
		} finally{
			if(!Primitives.isNull(pw)){
				pw.close();
			}
		}
	}
}
