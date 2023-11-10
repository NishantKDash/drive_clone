package com.nishant.drive_clone.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BadCredentialsException.class)
	public final ResponseEntity<Object> handleBadCredentialException(Exception ex, WebRequest request) {
		ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(error, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public final ResponseEntity<Object> handleUserAlreadyExistsException(Exception ex, WebRequest request) {
		ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);

	}

	@ExceptionHandler(UserDoesNotExistException.class)
	public final ResponseEntity<Object> handleUserDoesNotExistsException(Exception ex, WebRequest request) {
		ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public final ResponseEntity<Object> handlewrongpassword(Exception ex, WebRequest request) {
		ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);

	}
}
