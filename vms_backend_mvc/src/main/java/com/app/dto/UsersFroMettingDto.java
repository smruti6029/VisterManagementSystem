package com.app.dto;

import com.app.entity.User;

public class UsersFroMettingDto {

	Integer id;

	String image;

	String name;

	Boolean isPresent;
	
	String companyName;

	String email;
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	String department;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsPresent() {
		return isPresent;
	}

	public void setIsPresent(Boolean isPresent) {
		this.isPresent = isPresent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	

	public UsersFroMettingDto(Integer id, String image, String name, Boolean isPresent, String companyName,
			String email, String department) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.isPresent = isPresent;
		this.companyName = companyName;
		this.email = email;
		this.department = department;
	}

	public UsersFroMettingDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static UsersFroMettingDto convertTodto(User user) {
		UsersFroMettingDto obj = new UsersFroMettingDto();
		obj.setImage(user.getImage());
		obj.setName(user.getFirstname() + " " + user.getLastname());
		obj.setId(user.getId());
		obj.setIsPresent(user.getIsPresent());
		obj.setEmail(user.getEmail());
		obj.setCompanyName(user.getCompany().getName());
		if(!user.getDepartment().getName().isEmpty()) {
			obj.setDepartment(user.getDepartment().getName());
		}
		

		return obj;

	}

}
