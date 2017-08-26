package gov.hhs.cms.aca.global_assets.core.models.documentservices;

import org.apache.sling.api.resource.LoginException;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.docassurance.client.api.DocAssuranceException;
import com.adobe.fd.docassurance.client.api.ReaderExtensionOptions;

public interface DocAssuranceServiceCaller {

	/**
	 * This method is responsible for the calls to the DocAssuranceService. It reads the ACA Batch Notices - Doc Assurance Service Caller configurations and sets them 
	 * locally before passing the provided options and document to the DocAssuranceService.
	 * 
	 * @param pdfDoc
	 * @param extensionOptions (null to use OSGI service defaults)
	 * @return doc
	 * @throws DocAssuranceException 
	 * @throws LoginException 
	 * @throws Exception
	 */
	public abstract Document applyUsageRights(Document pdfDoc,
			ReaderExtensionOptions extensionOptions) throws LoginException, DocAssuranceException ;

}