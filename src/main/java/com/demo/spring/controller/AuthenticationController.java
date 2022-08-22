package com.demo.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.UnauthorizedException;
import com.demo.spring.domain.UserDTO;
import com.demo.spring.model.User;
import com.demo.spring.security.JwtTokenUtil;
import com.demo.spring.security.JwtUser;

@RestController
public class AuthenticationController {
	@Value("${jwt.header}")
	private   String header;
	
	@Autowired private AuthenticationManager  authenticationManager;
	@Autowired(required=false) private JwtTokenUtil  jwtTokenUtil;
	
	
	@PostMapping(value="/login")
	public ResponseEntity<UserDTO> login(@RequestBody User user , HttpServletRequest request , HttpServletResponse response){
		try{
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
			final JwtUser userDetails =(JwtUser) authentication.getPrincipal();
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final String token= jwtTokenUtil.generateToken(userDetails);
			//final String token="aaa";
			response.setHeader("Token", token);
			BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
			
			return new ResponseEntity<UserDTO>(new UserDTO(userDetails.getUser(),token) , HttpStatus.OK);
		}catch (Exception e) {
			 System.out.println("test skander"+user.isEanbled());
			throw new UnauthorizedException(e.getMessage());
			
			
		}
			
		
	}
}
