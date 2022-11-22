package com.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.user.model.Type;
import com.user.model.User;

@Service
public interface UserService {
	User save(User user);
	List<User> findAll();
	Optional<User> findById(int id);
	List<User> findByType(Type type);
	void deleteById(int id);
	Optional<User> findByEmail(String email);
}
