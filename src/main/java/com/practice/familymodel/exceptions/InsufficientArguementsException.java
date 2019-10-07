package com.practice.familymodel.exceptions;

public class InsufficientArguementsException extends Exception {
	private static final long serialVersionUID = 1L;

	public InsufficientArguementsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InsufficientArguementsException(String message) {
		super(message);
	}
}
