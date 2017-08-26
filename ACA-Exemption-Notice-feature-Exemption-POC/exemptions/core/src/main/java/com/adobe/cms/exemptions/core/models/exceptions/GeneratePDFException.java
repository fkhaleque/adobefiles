package com.adobe.cms.exemptions.core.models.exceptions;

public class GeneratePDFException extends Exception{

	private static final long serialVersionUID = -7264692305736437777L;

	//Parameterless Constructor
	public GeneratePDFException() {}

	//Constructor that accepts a message
	public GeneratePDFException(String message)
	{
		super(message);
	}

	//Constructor that accepts a message
	public GeneratePDFException(String message, Throwable e)
	{
		super(message, e);
	}

}
