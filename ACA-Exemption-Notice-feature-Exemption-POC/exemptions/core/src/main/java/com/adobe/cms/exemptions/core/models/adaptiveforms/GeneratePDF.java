package com.adobe.cms.exemptions.core.models.adaptiveforms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.adobe.aemfd.docmanager.Document;

/**
 * Interface use to expose public services available in this package.
 *  
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 * @see {@link com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDFImpl}
 */
public interface GeneratePDF {

	public static String CONTEXT_PATH = "/bin/generatePDF";
	
	public static String CACHE_CONTEXT_PATH = "/bin/generatePDF/cache";
	public static String CACHE_QUERY_PARAM = "stash";
	public static String CACHE_FLUSH_PARAM = "flush";
	
	public static int BUFFER_SIZE = 4096;
	public static String CONTENT_TYPE = "application/pdf";
	public static String CHARACTER_ENCODING = "UTF-8";
	
	public static String TEMPLATE_PATH = "templatePath";
	public static String RENDER_ACTION = "interactivePDF";
	public static String READER_EXTEND = "readerExtend";
	public static String RENDER_TYPE = "renderType";
	
	/**
	 * Public method extracted from GeneratePDFImpl which allows the creation of a Document based on SlingHttpServletRequest
	 * containing jcr:data and the appropriate interface parameters.
	 * 
	 * @param jcrData  
	 * @param options (must contain GuideConstants.JCR_DATA, TEMPLATE_PATH, RENDER_ACTION, READER_EXTEND parameters)
	 * @return document
	 * @throws UnsupportedEncodingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws TransformerException
	 * @throws Exception
	 */
	public abstract Document createDOR(String jcrData, GeneratePDFOptionsSpec options) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException, FactoryConfigurationError, TransformerException, Exception;
	
}
