package com.practice.familymodel.exceptions;

public class UndefinedRelationshipException extends Exception{
	private static final long serialVersionUID = 1L;

	public UndefinedRelationshipException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UndefinedRelationshipException(String message) {
		super(message);
	}

}
