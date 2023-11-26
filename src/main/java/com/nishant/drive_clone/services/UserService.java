package com.nishant.drive_clone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nishant.drive_clone.exceptions.InvalidCredentialsException;
import com.nishant.drive_clone.exceptions.UserAlreadyExistsException;
import com.nishant.drive_clone.exceptions.UserDoesNotExistException;
import com.nishant.drive_clone.models.UserEntity;
import com.nishant.drive_clone.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	private PasswordEncoder encoder;

	public UserEntity createUser(String email, String name, String password) throws Exception {
		if (userRepository.findByEmail(email) != null)
			throw new UserAlreadyExistsException("A user with email:" + email + "is already registerd");
		UserEntity user = UserEntity.builder().name(name).email(email).password(encoder.encode(password)).build();
		userRepository.save(user);
		emailNotificationService.sendEmail(email, "Account Creation", "Hi , Your account has been successfully created !");
		
		return user;
	}

	public UserEntity verifyUser(String email, String password) throws Exception {
		if (userRepository.findByEmail(email) == null)
			throw new UserDoesNotExistException("A user with email:" + email + "does not exist");

		UserEntity user = userRepository.findByEmail(email);
		if (!encoder.matches(password, user.getPassword()))
			throw new InvalidCredentialsException("The provided password is incorrect");

		return user;
	}
}
