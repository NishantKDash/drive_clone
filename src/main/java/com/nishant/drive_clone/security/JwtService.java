package com.nishant.drive_clone.security;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	
	public String createToken(String email)
	{
		SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
		String jws = Jwts.builder()
				         .claim("email", email)
				         .claim("authorities", "USER")
				         .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				         .id(UUID.randomUUID().toString())
				         .signWith(key)
				         .compact();
		
		return jws;
	}
  
}
