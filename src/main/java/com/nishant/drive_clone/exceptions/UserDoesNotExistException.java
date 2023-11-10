package com.nishant.drive_clone.exceptions;

public class UserDoesNotExistException extends Exception {

	private static final long serialVersionUID = 8953928523716483005L;

	public UserDoesNotExistException(String message) {
		super(message);
	}

}
