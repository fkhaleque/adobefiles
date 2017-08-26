package gov.hhs.cms.aca.global_assets.core.models.documentservices;

import java.util.Map;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.output.api.BatchOptions;
import com.adobe.fd.output.api.BatchResult;
import com.adobe.fd.output.api.OutputServiceException;
import com.adobe.fd.output.api.PDFOutputOptions;

public interface OutputServiceCaller {

	/**
	 * This method is responsible for the calls to the OuputService. It reads the ACA Batch Notices - Output Service Caller configurations and sets them 
	 * locally before passing the provided template and data to the OutputService.
	 * 
	 * @param template
	 * @param data
	 * @param outputOptions (null to use OSGI service defaults)
	 * @return generatedDocument
	 * @throws OutputServiceException
	 * @see https://helpx.adobe.com/aem-forms/6/aem-document-services-programmatically.html#generatePDFOutput 
	 */
	public abstract Document generatePDFOutputDocument(Document template,
			Document data, 
			PDFOutputOptions outputOptions)
					throws OutputServiceException;

	public abstract BatchResult generatePDFOutputBatch(Map<String, String> templates,
			Map<String, Document> dataDocs, PDFOutputOptions outputOptions,
			BatchOptions batchOptions) throws OutputServiceException;

	public abstract int getPDFPageCount(Document metaDataDocument);


}