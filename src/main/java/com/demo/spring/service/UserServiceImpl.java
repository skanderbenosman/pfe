package com.demo.spring.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.spring.model.User;
import com.demo.spring.repository.UserRepository;
import com.demo.spring.util.PasswordUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired UserRepository userRepository;

	@Override
	public User save(User user) {
		System.out.println("3asss="+user.getPassword());
		String password = PasswordUtil.getPasswordHash(user.getPassword());
		user.setPassword(password);
		user.setCreatedDate(new Date());
		return userRepository.save(user);
	}

	@Override
	public List<User> findALL() {
		
		return (List<User>) userRepository.findAll();
	}

	@Override
	public User getUserByEmail(String name) {
		return userRepository.findByEmailIgnoreCase(name);
	}
	public User Update(User user) {
		String password = PasswordUtil.getPasswordHash(user.getPassword());
		user.setPassword(password);
		user.setCreatedDate(new Date());
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	@Override
	public User save2(User user) {
		// TODO Auto-generated method stub
		return  userRepository.save(user);
	}

	@Override
	public User save3(User user) {
		String password = PasswordUtil.getPasswordHash(user.getPassword());
		user.setPassword(password);
		
		return userRepository.save(user);
	}

	

}
