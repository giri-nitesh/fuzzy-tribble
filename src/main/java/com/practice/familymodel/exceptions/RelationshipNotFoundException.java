package com.practice.familymodel.exceptions;

public class RelationshipNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public RelationshipNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RelationshipNotFoundException(String message) {
		super(message);
	}
}
