package gov.hhs.cms.aca.global_assets.core.models.exceptions;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public interface CombinedMessageErrorHandler extends ErrorHandler {

	void throwCombinedErrors() throws SAXException;

}
