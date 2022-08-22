package com.demo.spring.model;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "files")
public class FileInfo   {
 
	@Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  private String name;
  private String Algo;
  private Long User_id;


public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public FileInfo(String name, Long user_id) {
	
	this.name = name;
	User_id = user_id;
}

public FileInfo() {
	
	
}
public Long getUser_id() {
	return User_id;
}
public void setUser_id(Long user_id) {
	User_id = user_id;
}
public String getAlgo() {
	return Algo;
}
public void setAlgo(String algo) {
	Algo = algo;
}



 
}