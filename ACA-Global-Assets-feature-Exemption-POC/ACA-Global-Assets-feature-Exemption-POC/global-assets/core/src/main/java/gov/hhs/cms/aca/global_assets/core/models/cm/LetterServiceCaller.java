package gov.hhs.cms.aca.global_assets.core.models.cm;

import java.util.Map;

import gov.hhs.cms.aca.global_assets.core.models.exceptions.LetterGenerationException;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.icc.dbforms.obj.Letter;
import com.adobe.icc.dbforms.obj.LetterRenderOptionsSpec;

public interface LetterServiceCaller {

	/**
	 * Method used to render Letter with data. 
	 * 
	 * @param letterId
	 * @param data
	 * @param renderSpec TODO
	 * @return document
	 * @throws LetterGenerationException
	 */
	public abstract Document renderCmLetter(String letterId, String data, LetterRenderOptionsSpec renderSpec)
			throws LetterGenerationException;

	/**
	 * Exposed LetterService method to publish letter
	 * @param letterId
	 */
	public abstract void publishCmLetter(String letterId);
	
	/**
	 * Method used to retrieve a letter from the CM store.
	 * 
	 * @param letterId
	 * @return letter
	 */
	public abstract Letter getLetter(String letterId);
	
	/**
	 * Method to query letter existence.
	 * 
	 * @param letterName
	 * @return boolean
	 */
	public abstract boolean letterExists(String letterId);

	public abstract Map<String, Object> processCmLetter(String letterId, String data,
			LetterRenderOptionsSpec renderSpec)
			throws LetterGenerationException;

}