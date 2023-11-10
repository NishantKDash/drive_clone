package com.nishant.drive_clone.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityFilter {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.sessionManagement(sessionCreation -> sessionCreation.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		
		
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
		.addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
		.csrf(csrf -> csrf.disable())
		.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request)
			{
				CorsConfiguration cfg = new CorsConfiguration();
				cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000/"));
				cfg.setAllowedMethods(Collections.singletonList("*"));
				cfg.setAllowCredentials(true);
				cfg.setAllowedHeaders(Collections.singletonList("*"));
				cfg.setExposedHeaders(Arrays.asList("Authorization"));
				cfg.setMaxAge(3600L);
				return cfg;
			}
		}))
		.formLogin(Customizer.withDefaults())
		.httpBasic(Customizer.withDefaults());
		
		return http.build();
	}
	
	
	@Bean
	public PasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}
	
}
