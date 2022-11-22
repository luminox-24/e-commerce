package com.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.user.model.Type;
import com.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public List<User> findByType(Type type);
	public Optional<User> findByEmail(String email);
}
