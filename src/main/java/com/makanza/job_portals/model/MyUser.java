package com.makanza.job_portals.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Transient;

@Entity
public class MyUser {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;
private String username;
private String email;
private String password;

private String photo;
@Transient
private MultipartFile photoFile;
private String role;
public MyUser() {
	super();
	// TODO Auto-generated constructor stub
}
public MyUser(String username, String email, String password,MultipartFile photoFile,String photo,  String role) {
	super();
	this.username = username;
	this.email = email;
	this.photo=photo;
	this.password = password;
	this.photoFile=photoFile;
	this.role = role;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
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
public MultipartFile getPhotoFile() {
	return photoFile;
}
public void setPhotoFile(MultipartFile photoFile) {
	this.photoFile = photoFile;
}
public String getPhoto() {
	return photo;
}
public void setPhoto(String photo) {
	this.photo = photo;
}



}
