package com.mart.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mart.entity.User;
import com.mart.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
