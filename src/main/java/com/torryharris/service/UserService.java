package com.torryharris.service;


import com.torryharris.dto.UserRegistrationDto;
import com.torryharris.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
}
