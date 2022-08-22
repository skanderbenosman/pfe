package com.demo.spring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.spring.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	User findByEmailIgnoreCase(String username);
	Optional<User> findById(Long id);
	

}
