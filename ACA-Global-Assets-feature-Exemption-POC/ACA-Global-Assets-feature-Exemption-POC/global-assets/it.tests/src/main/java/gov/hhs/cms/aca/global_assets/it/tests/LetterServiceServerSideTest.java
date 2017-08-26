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
package gov.hhs.cms.aca.global_assets.it.tests;

import static org.junit.Assert.*;
import gov.hhs.cms.aca.global_assets.core.commons.Primitives;
import gov.hhs.cms.aca.global_assets.core.commons.UtilsImpl;
import gov.hhs.cms.aca.global_assets.core.commons.W3DocumentHelpers;
import gov.hhs.cms.aca.global_assets.core.models.cm.LetterServiceCaller;
import gov.hhs.cms.aca.global_assets.core.models.cm.ProcessLetterCallable;
import gov.hhs.cms.aca.global_assets.core.models.documentservices.OutputServiceCaller;
import gov.hhs.cms.aca.global_assets.core.models.exceptions.LetterGenerationException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.sling.junit.annotations.SlingAnnotationsTestRunner;
import org.apache.sling.junit.annotations.TestReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.adobe.fd.output.api.BatchResult;
import com.adobe.fd.output.api.OutputServiceException;
import com.adobe.icc.ddg.api.LetterRenderService;

/** 
 *  Test case which uses OSGi services injection
 *  to get hold of the Commons related classes which 
 *  it wants to test server-side. 
 */
@RunWith(SlingAnnotationsTestRunner.class)
public class LetterServiceServerSideTest {

	@TestReference
	private LetterServiceCaller letterServiceCaller;
	
	private static final Logger log = LoggerFactory.getLogger(LetterServiceServerSideTest.class);

	@Test
	public void testOutputServiceCaller() throws Exception {
		assertNotNull("Expecting the LetterServiceCaller to be injected by Sling test runner", letterServiceCaller);
	}

	@Test
	public void testProcessLetter() throws LetterGenerationException, TransformerException, SAXException, IOException, ParserConfigurationException{
				
		com.adobe.aemfd.docmanager.Document templateDocument = null;
		com.adobe.aemfd.docmanager.Document dataDocument = null;
		
		String letterId = "/content/apps/cm/correspondence/letters/1001/Adhoc_LetterTemplate";
		dataDocument = new com.adobe.aemfd.docmanager.Document("/content/dam/acaglobalassets/BatchNotice.xml");
		
		String xml = W3DocumentHelpers.domToString(
				W3DocumentHelpers.aemDocToW3cDoc(dataDocument));
		
		Map<String, Object> response = letterServiceCaller.processCmLetter(letterId, xml, null);
		
		assertNotNull("Testing value from processCmLetter()", response);

		/*
		 * If we received a map response from CM, parse and extract the
		 * template and data from it to send it to the Output Service in
		 * order to produce a tagged PDF. 
		 */
		if(!Primitives.isNull(response)){
			
			//Iterate through the map to find the template and data
			for (Map.Entry<String, Object> responseEntry : response.entrySet()){

				if(responseEntry.getKey().equals(LetterRenderService.LAYOUT_TEMPLATE_KEY)){ 

					//This entry contains the template so we load it into the document.
					templateDocument = new com.adobe.aemfd.docmanager.Document((byte[]) responseEntry.getValue());
				}
				else if(responseEntry.getKey().equals(LetterRenderService.XML_DATA_KEY)){

					//This entry contains the data so we load it into the document.
					dataDocument = new com.adobe.aemfd.docmanager.Document((byte[]) responseEntry.getValue());
				}
			}
			
			File file = new File("/temp/globalassets/slingjunit/letterdata.xml");
			
			dataDocument.copyToFile(file);
		}
	}
}
