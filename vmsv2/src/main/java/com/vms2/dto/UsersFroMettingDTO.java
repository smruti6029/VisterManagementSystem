package com.vms2.dto;

import com.vms2.entity.User;

public class UsersFroMettingDTO {
	
	Integer id;
	
	String image;
	
	String name;
	
	Boolean status;

	
	
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

	public UsersFroMettingDTO(Integer id, String image, String name) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
	}

	public UsersFroMettingDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public static UsersFroMettingDTO convertTodto(User user)
	{
		UsersFroMettingDTO obj=new UsersFroMettingDTO();
		obj.setImage(user.getImage());
		obj.setName(user.getFirstname()+" "+user.getLastname());
		obj.setId(user.getId());
		obj.setStatus(user.getIsActive());
		
		
		
		return obj;
	
	}

}
