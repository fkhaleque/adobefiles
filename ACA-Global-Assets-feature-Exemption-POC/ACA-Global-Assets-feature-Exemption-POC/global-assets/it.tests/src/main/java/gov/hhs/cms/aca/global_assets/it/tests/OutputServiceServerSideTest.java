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
import gov.hhs.cms.aca.global_assets.core.commons.W3DocumentHelpers;
import gov.hhs.cms.aca.global_assets.core.models.documentservices.OutputServiceCaller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

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

/** 
 *  Test case which uses OSGi services injection
 *  to get hold of the Commons related classes which 
 *  it wants to test server-side. 
 */
@RunWith(SlingAnnotationsTestRunner.class)
public class OutputServiceServerSideTest {

	@TestReference
	private OutputServiceCaller outputServiceCaller;
	
	private static final Logger log = LoggerFactory.getLogger(OutputServiceServerSideTest.class);

	@Test
	public void testOutputServiceCaller() throws Exception {
		assertNotNull("Expecting the OutputServiceCaller to be injected by Sling test runner", outputServiceCaller);
	}
	
	@Test
	public void testGeneratePDF() throws OutputServiceException, SAXException, IOException, ParserConfigurationException, TransformerException{
		
		com.adobe.aemfd.docmanager.Document dataDocument = new com.adobe.aemfd.docmanager.Document("/content/dam/acaglobalassets/BatchNotice.xml");

		com.adobe.aemfd.docmanager.Document templateDocument = new com.adobe.aemfd.docmanager.Document("/content/dam/batchnoticeprocessing/Form1.xdp");
		
		com.adobe.aemfd.docmanager.Document result = outputServiceCaller.generatePDFOutputDocument(templateDocument, dataDocument, null);

		File file = new File("/temp/globalassets/slingjunit/pdf.pdf");
		
		result.copyToFile(file);
		
		assertNotNull("Testing OutputServiceCaller.generatePDFOutputDocument()", result);		
	}

	@Test
	public void testGeneratePDFOutputBatch() throws OutputServiceException, SAXException, IOException, ParserConfigurationException, TransformerException{
		com.adobe.aemfd.docmanager.Document crxDocument = new com.adobe.aemfd.docmanager.Document("/content/dam/acaglobalassets/BatchNotice.xml");

		HashMap<String, String> templates = new HashMap<>();
		templates.put("1", "crx:///content/dam/batchnoticeprocessing/Form1.xdp");
			
		HashMap<String, com.adobe.aemfd.docmanager.Document> dataDocs = new HashMap<>();
		dataDocs.put("1", crxDocument);
		
		BatchResult batchResult = outputServiceCaller.generatePDFOutputBatch(templates, dataDocs, null, null);

		assertNotNull("Testing OutputServiceCaller.generatePDFOutputBatch()", batchResult);
		
		int pageCount = outputServiceCaller.getPDFPageCount(batchResult.getMetaDataDoc());
		
		assertEquals("Testing Page Count with OutputServiceCaller.getPageCount()", 1, pageCount);
		
	}

}
