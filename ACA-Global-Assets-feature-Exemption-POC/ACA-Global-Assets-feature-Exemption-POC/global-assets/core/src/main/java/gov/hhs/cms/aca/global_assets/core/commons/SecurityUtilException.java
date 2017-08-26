package gov.hhs.cms.aca.global_assets.core.commons;

public class SecurityUtilException extends Exception{

	private static final long serialVersionUID = -2688780954845918679L;

	//Parameterless Constructor
	public SecurityUtilException() {}

	//Constructor that accepts a message
	public SecurityUtilException(String message)
	{
		super(message);
	}

	//Constructor that accepts a message
	public SecurityUtilException(String message, Throwable e)
	{
		super(message, e);
	}

}
