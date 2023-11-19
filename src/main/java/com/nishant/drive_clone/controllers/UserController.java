package com.nishant.drive_clone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nishant.drive_clone.dtos.UserLoginRequestDto;
import com.nishant.drive_clone.dtos.UserLoginResponseDto;
import com.nishant.drive_clone.dtos.UserRegisterRequestDto;
import com.nishant.drive_clone.models.UserEntity;
import com.nishant.drive_clone.security.JwtService;
import com.nishant.drive_clone.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/register")
	public ResponseEntity<String> createUser(@RequestBody UserRegisterRequestDto userDto) {

		try {
			UserEntity user = userService.createUser(userDto.getEmail(), userDto.getName(), userDto.getPassword());
			return ResponseEntity.ok("User with email : " + user.getEmail() + "has been created successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<Object> verifyUser(@RequestBody UserLoginRequestDto userDto) {
		try {

			UserEntity user = userService.verifyUser(userDto.getEmail(), userDto.getPassword());
			UserLoginResponseDto responseDto = new UserLoginResponseDto();
			String token = jwtService.createToken(user.getEmail());
			responseDto.setEmail(user.getEmail());
			responseDto.setToken(token);
			return ResponseEntity.ok(responseDto);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

}
