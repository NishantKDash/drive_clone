package com.nishant.drive_clone.exceptions;

public class UserAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -2235378354540390565L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}

}
