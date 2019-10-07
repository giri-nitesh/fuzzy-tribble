package com.practice.familymodel.exceptions;

public class InvalidGenderException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidGenderException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidGenderException(String message) {
		super(message);
	}
}
