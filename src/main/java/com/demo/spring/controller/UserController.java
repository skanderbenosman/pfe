package com.demo.spring.controller;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import com.demo.spring.domain.Response;
import com.demo.spring.model.User;
import com.demo.spring.repository.UserRepository;
import com.demo.spring.service.UserService;

@RestController
public class UserController {
	
	 @Autowired
	    public JavaMailSender emailSender;
	@Autowired private UserService userService;
	@Autowired private UserRepository userRepository;
	@GetMapping(value="/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> getAllusers(){
		List<User> users = userService.findALL();
		List<User> users2=new ArrayList<User>();
		for(User u:users){
				if(u.getRole().equals("USER")){
					users2.add(u);
				}
		}
		return new ResponseEntity<List<User>>(users2,HttpStatus.OK);
		
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
	 @PostMapping(value="/sendemail")
	  	public ResponseEntity<Response> getemail(@RequestParam("email") String email) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		 User user=userRepository.findByEmailIgnoreCase(email);
		 if(user != null){
			
 	  SimpleMailMessage message = new SimpleMailMessage();
       
       message.setTo(email);
       message.setSubject("Test Simple Email");
       message.setText("https://pfe-front-end.herokuapp.com/auth/resetpwd2/"+user.getResetpwdtoken());

       // Send Message!
       System.out.println(email);
      
       emailSender.send(message);

       return new ResponseEntity<Response>(new Response("email sent"),HttpStatus.OK);}
		 else{return new ResponseEntity<Response>(new Response("email not found"),HttpStatus.OK);}
	  	 
	  		
	  	}
	 @PostMapping(value="/resetpass")
		public ResponseEntity<Response> update(@RequestParam("Email") String email,@RequestParam("Pwd") String pwd){
		
		 User user=userRepository.findByResetpwdtoken(email);
			
			
			if(user !=null ){
				System.out.println("d5al2");
			
				user.setPassword(pwd);
				user.setResetpwdtoken(RandomStringUtils.random(100, true, true));
				User dbUser= userService.save3(user);
				return new ResponseEntity<Response>(new Response("user is updated") ,HttpStatus.OK);
			}
			
			return new ResponseEntity<Response>(new Response("user not saved") ,HttpStatus.OK);
			
		}
	 @GetMapping(value="/getimage/{id}")
		public File getuserimage(@PathVariable("id") Long id){
		 File f = new File("/uploads2/606/cc.png");
		 return f;
	 }
	 @PreAuthorize("hasRole('ADMIN')")
	 @GetMapping(value="/Userstats")
		public List<Integer> Userstats(){
		 Date d = new Date();
		 List<Integer> ScoreList = new ArrayList<Integer>();
		 List<User> users = userService.findALL();
		 for (int j = 0; j < 12; j++) {
			 int i=0;
		 for(User u: users){
				if( u.getCreatedDate().getYear()==d.getYear() && u.getCreatedDate().getMonth()==j){
				i=i+1;
			}}
		 ScoreList.add(i);
		 }
		return ScoreList;
	 }
	 @PreAuthorize("hasRole('ADMIN')")
	 @GetMapping(value="/Userstats2")
		public List<Integer> Userstats2(){
		 Date d = new Date();
		 List<Integer> ScoreList = new ArrayList<Integer>();
		 List<User> users = userService.findALL();
		 for (int j = 0; j < 12; j++) {
			 int i=0;
		 for(User u: users){
				if( u.getCreatedDate().getYear()==d.getYear()-1 && u.getCreatedDate().getMonth()==j){
				i=i+1;
			}}
		 ScoreList.add(i);
		 }
		 return ScoreList;
	 }
	 @GetMapping(value="/nbusersactive")	
		public int getNBusersactive(){
			List<User> users = userService.findALL();
			int i=0;
			
			for(User u: users){
				if(u.isEanbled()==true){
					i=i+1;}
				}
			return i;
			
		}
	 @GetMapping(value="/nbusersbaned")	
		public int getNBusersbaned(){
			List<User> users = userService.findALL();
			int i=0;
			
			for(User u: users){
				if(u.isEanbled()==false){
				i=i+1;}
			}
			return i;
			
		}
	}
