package com.practice.familymodel.exceptions;

public class TooManyArguementsException extends Exception {

	private static final long serialVersionUID = 1L;

	public TooManyArguementsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TooManyArguementsException(String message) {
		super(message);
	}
}
