package com.demo.spring.service;

import java.util.List;
import java.util.Optional;

import com.demo.spring.model.User;

public interface UserService {

	User save(User user);
	User save2(User user);
	User save3(User user);

	List<User> findALL();

	User getUserByEmail(String name);

	Optional<User> findById(Long id);

}
