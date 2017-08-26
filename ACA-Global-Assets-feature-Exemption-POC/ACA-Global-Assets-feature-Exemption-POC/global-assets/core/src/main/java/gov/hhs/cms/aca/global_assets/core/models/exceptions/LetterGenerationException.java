package gov.hhs.cms.aca.global_assets.core.models.exceptions;

public class LetterGenerationException extends Exception{

	private static final long serialVersionUID = -7264692305736437777L;

	//Parameterless Constructor
	public LetterGenerationException() {}

	//Constructor that accepts a message
	public LetterGenerationException(String message)
	{
		super(message);
	}

	//Constructor that accepts a message
	public LetterGenerationException(String message, Throwable e)
	{
		super(message, e);
	}

}
