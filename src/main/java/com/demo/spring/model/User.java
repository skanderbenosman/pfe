package com.demo.spring.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author skander
 *
 */
@Entity
@Table(name="user")
public class User implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 8099685227285203356L;
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
@Column(name="id")
private Long id;
@Column(name="firstName")
private String firstName;
@Column(name="lastName")
private String lastName;
@Column(name="email")
private String email;
@Column(name="password")
private String password;
@Column(name="role")
private String role;
@Column(name="eanbled")
private boolean eanbled;
@Column(name="phoneNumber")
private String phoneNumber;
@Column(name="createdDate")
private Date createdDate;
@Column(name="imageName")
private String imageName;
@Column(name="resetpwdtoken")
private String resetpwdtoken;


public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}

public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}

public String getPhoneNumber() {
	return phoneNumber;
}
public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}
public Date getCreatedDate() {
	return createdDate;
}
public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}
public boolean isEanbled() {
	return eanbled;
}
public void setEanbled(boolean eanbled) {
	this.eanbled = eanbled;
}
public String getImageName() {
	return imageName;
}
public void setImageName(String imageName) {
	this.imageName = imageName;
}
public String getResetpwdtoken() {
	return resetpwdtoken;
}
public void setResetpwdtoken(String resetpwdtoken) {
	this.resetpwdtoken = resetpwdtoken;
}




}
