package com.demo.spring.controller;



import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.spring.domain.Response;
import com.demo.spring.model.User;
import com.demo.spring.repository.UserRepository;
import com.demo.spring.service.UserService;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class PreLoginController {
	@Autowired private UserService userService;
	
	@Autowired ImageUploadController ipc;
	@Autowired private UserRepository userRepository;
	@PostMapping(value="/registration")
	public ResponseEntity<Response> registration(@RequestParam("Email") String email,@RequestParam("Pwd") String pwd,@RequestParam("FirstName") String firstname,@RequestParam("LastName") String lastname,@RequestParam("Phone") String phone,@RequestParam("file") MultipartFile file){
		if(userRepository.findByEmailIgnoreCase(email)==null){
		User user= new User();
		user.setEmail(email);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setPassword(pwd);
		user.setPhoneNumber(phone);
		user.setRole("USER");
		user.setEanbled(true);
		user.setResetpwdtoken(RandomStringUtils.random(100, true, true));
		user.setImageName(file.getOriginalFilename());
		User dbUser= userService.save(user);
		if(dbUser!=null){
			try {
				System.out.println("andrrr");
				ipc.uplaodImage(file,dbUser.getId());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return new ResponseEntity<Response>(new Response("user is saved") ,HttpStatus.OK);
			
			 
		}}
		return new ResponseEntity<Response>(new Response("email exist") ,HttpStatus.OK);
		
	}

}
