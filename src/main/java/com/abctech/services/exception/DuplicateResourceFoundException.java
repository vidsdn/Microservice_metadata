package com.abctech.services.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateResourceFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	String resourceName;
	String fieldName;
	String fieldValue;

	public DuplicateResourceFoundException(String message) {
		super(message);
	}

}