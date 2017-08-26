package gov.hhs.cms.aca.global_assets.core.models.documentservices;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.forms.api.FormsServiceException;
import com.adobe.fd.forms.api.PDFFormRenderOptions;

public interface FormsServiceCaller {

	/**
	 * This method is responsible for the calls to the FormsService. It reads the ACA Batch Notices - Forms Service Caller configurations and sets them 
	 * locally before passing the provided template and data to the FormsService.
	 * 
	 * @param templateLoc
	 * @param data
	 * @param formsOptions (null to use OSGI service defaults)
	 * @return doc
	 * @throws FormsServiceException
	 */
	public abstract Document callFormsService(String templateLoc, 
			Document data, 
			PDFFormRenderOptions formsOptions) throws FormsServiceException;

	/**
	 * This method is responsible for calling the FormsService when the operation only requires 
	 * data to be imported into a pre-rendered PDF.
	 * 
	 * @param data
	 * @param pdf
	 * @return doc
	 * @throws FormsServiceException
	 */
	public abstract Document callFormsImportData(Document pdf, 
			Document data) throws FormsServiceException;

}