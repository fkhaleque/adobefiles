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

import static org.junit.Assert.assertNotNull;
import gov.hhs.cms.aca.global_assets.core.commons.Primitives;
import gov.hhs.cms.aca.global_assets.core.commons.W3DocumentHelpers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.sling.junit.annotations.SlingAnnotationsTestRunner;
import org.apache.sling.junit.annotations.TestReference;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** 
 *  Test case which uses OSGi services injection
 *  to get hold of the Commons related classes which 
 *  it wants to test server-side. 
 */
@RunWith(SlingAnnotationsTestRunner.class)
public class CommonsServerSideTest {

	@TestReference
	private SlingSettingsService settings;

	@Test
	public void testHelloWorldModelServerSide() throws Exception {

		assertNotNull("Expecting the slingsettings to be injected by Sling test runner", settings);

	}


	/**
	 * Run the com.adobe.aemfd.docmanager.Document
	 * w3cDocToAemDoc(org.w3c.dom.Document) method test
	 * @throws TransformerException 
	 */
	@Test
	public void testW3cDocToAemDocT() throws TransformerException {

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<batchNoticeData xmlns:xsi=\"htt//www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"htt//www.cms.hhs.gov/notices BatchNotice.xsd \">" +
				"  <NoticeDate></NoticeDate>" +
				"  <PolicyId></PolicyId>" +
				"  <applicationSignatureDate></applicationSignatureDate>" +
				"  <SystemDate></SystemDate>" +
				"  <NoticeNumber></NoticeNumber>" +
				"  <AccountId></AccountId>" +
				"  <InsuranceMarketType></InsuranceMarketType>" +
				"  <NoticeTypeCode></NoticeTypeCode>" +
				"  <UniqueNoticeID></UniqueNoticeID>" +
				"  <CoverageYear></CoverageYear>" +
				"  <locale></locale>" +
				"  <ApplicationId></ApplicationId>" +
				"  <Barcode></Barcode>" +
				"<PostalCode></PostalCode>" +
				"  <FileName></FileName>" +
				"  <FileType></FileType>" +
				"  <FormName></FormName>" +
				"  <VersionNumber></VersionNumber>" +
				"  <SubmissionDate></SubmissionDate>" +
				"  <TrackingNumber></TrackingNumber>" +
				"  <NoticeData>" +
				"    <EmployerName></EmployerName>" +
				"	<JobIncomeEmployerName></JobIncomeEmployerName>" +
				"	<EmployeNameMatched></EmployeNameMatched>" +
				"    <StreetName1></StreetName1>" +
				"    <CityName></CityName>" +
				"    <State></State>" +
				"    <ZipCode></ZipCode>" +
				"    <EmployerTelphoneNumber></EmployerTelphoneNumber>" +
				"    <EmployereIN></EmployereIN>" +
				"    <ContactPersonFirstName></ContactPersonFirstName>" +
				"    <ContactPersonLastName></ContactPersonLastName>" +
				"    <ContactPersonTelephoneNumber></ContactPersonTelephoneNumber>" +
				"    <PersonFirstName></PersonFirstName>" +
				"    <PersonLastName></PersonLastName>" +
				"	<newParent>" +
				"		<newNode></newNode>" +
				"	</newParent>" +
				"    <PersonSuffix></PersonSuffix>" +
				"    <SSN></SSN>" +
				"    <BirthDate></BirthDate>" +
				"    <CoveredInsuredMember></CoveredInsuredMember>" +
				"  </NoticeData>" +
				"</batchNoticeData>";
		Document result = W3DocumentHelpers.stringToDom(xml);

		com.adobe.aemfd.docmanager.Document doc = W3DocumentHelpers.w3cDocToAemDoc(result);

		assertNotNull("Testing W3DocumentHelpers.w3cDocToAemDoc()", doc);
	}

	/**
	 * Run the org.w3c.dom.Document
	 * aemDocToW3cDoc(com.adobe.aemfd.docmanager.Document) method test
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	@Test
	public void testAemDocToW3cDoc() throws SAXException, IOException, ParserConfigurationException {
		com.adobe.aemfd.docmanager.Document crxDocument = new com.adobe.aemfd.docmanager.Document("/content/dam/acaglobalassets/BatchNotice.xml");

		Document w3Document = W3DocumentHelpers.aemDocToW3cDoc(crxDocument);

		assertNotNull("Testing W3DocumentHelpers.aemDocToW3cDoc()", w3Document);
	}

	/**
	 * Run the void writeDoc(Document, String) method test
	 * @throws IOException 
	 */
	@Test
	public void testWriteAndReadDoc() throws IOException {
		com.adobe.aemfd.docmanager.Document crxDocument = new com.adobe.aemfd.docmanager.Document("/content/acaglobalassets/jcr:content/image/file/jcr:content/dam:thumbnails/dam:thumbnail_319.png");
		String fileNameString = "test.png";
		String pathString = "/temp/globalassets/slingjunit/";

		Primitives.createMissingFolder(pathString);

		Primitives.writeDoc(crxDocument, pathString+fileNameString);

		com.adobe.aemfd.docmanager.Document resultDocument = Primitives.readDoc("/temp/globalassets/slingjunit/test.png");

		assertNotNull("Testing Primitives.readDoc()", resultDocument);

		File rmFile = new File(pathString+fileNameString);

		rmFile.delete();
	}
}
