package com.demo.spring.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.spring.domain.Response;
import com.demo.spring.model.User;
import com.demo.spring.repository.UserRepository;
import com.demo.spring.service.UserService;

@RestController
public class UserController {
	
	
	@Autowired private UserService userService;
	@Autowired private UserRepository userRepository;
	@GetMapping(value="/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> getAllusers(){
		List<User> users = userService.findALL();
		return new ResponseEntity<List<User>>(users,HttpStatus.OK);
		
	}
	@GetMapping(value="/getuser")
	public ResponseEntity<User> getUsers(Principal principal){
		User user = userService.getUserByEmail(principal.getName());
		return new ResponseEntity<User>(user,HttpStatus.OK);
		
	}
	@PostMapping(value="/updateuser")
	public ResponseEntity<Response> update(@RequestParam("Email") String email,@RequestParam("Pwd") String pwd,@RequestParam("FirstName") String firstname,@RequestParam("LastName") String lastname,@RequestParam("Phone") String phone,@RequestParam("id") Long id){
		System.out.println("d5al"+phone);
		Optional<User> Ouser =userRepository.findById(id);
		User u=Ouser.get();
		System.out.println("d5al="+u.getEmail());
		if(userRepository.findByEmailIgnoreCase(email)==null || email.equals(u.getEmail()) ){
			System.out.println("d5al2");
			u.setEmail(email);
			u.setFirstName(firstname);
			u.setLastName(lastname);
			u.setPassword(pwd);
			u.setPhoneNumber(phone);
			User dbUser= userService.save(u);
			return new ResponseEntity<Response>(new Response("user is updated") ,HttpStatus.OK);
		}
		
		return new ResponseEntity<Response>(new Response("user not saved") ,HttpStatus.OK);
		
	}
	@PostMapping(value="/getuserupdated")
	public ResponseEntity<User> getUsers(@RequestParam("id") Long id){
		Optional<User> Ouser =userRepository.findById(id);
		User u=Ouser.get();
		System.out.println("5RAJ"+u.getId());
		return new ResponseEntity<User>(u,HttpStatus.OK);
		
	}
	@GetMapping(value="/newusers")	
	public int getAllNewusers(){
		List<User> users = userService.findALL();
		int i=0;
		Date d = new Date();
		for(User u: users){
			if( u.getCreatedDate().getYear()==d.getYear() && u.getCreatedDate().getMonth()==d.getMonth() && u.getCreatedDate().getDate()==d.getDate()){
			i=i+1;
		}}
		return i;
		
	}
	@GetMapping(value="/newusers2")	
	public int getAllNewusers2(){
		List<User> users = userService.findALL();
		int i=0;
		Date d = new Date();
		for(User u: users){
			if( u.getCreatedDate().getYear()==d.getYear() && u.getCreatedDate().getMonth()==d.getMonth() && u.getCreatedDate().getDate()==d.getDate()-1){
			i=i+1;
		}}
		return i;
		
	}
	@GetMapping(value="/nbusers")	
	public int getNBusers(){
		List<User> users = userService.findALL();
		int i=0;
		
		for(User u: users){
			
			i=i+1;
		}
		return i;
		
	}
	@PostMapping(value="/userUpdatedState")
	public ResponseEntity<Response> userUpdatedState(@RequestParam("id") Long id){
		Optional<User> Ouser =userRepository.findById(id);
		User u=Ouser.get();
		if(u.isEanbled()==true){
			System.out.print("mo77");
			u.setEanbled(false);
		}else
		{
			System.out.print("mo772");
			u.setEanbled(true);
		}
		User dbUser= userService.save2(u);
		return new ResponseEntity<Response>(new Response("user is updated"),HttpStatus.OK);
		
	}
	}
