package com.app.dto;

import java.util.Date;

public class CredentialDTO {

	private Integer id;

	private Integer userId;

	private String username;

	private String password;

	private String created_by;

	private Date createdOn;

	private Date updatedBY;

	private Boolean isActive;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedBY() {
		return updatedBY;
	}

	public void setUpdatedBY(Date updatedBY) {
		this.updatedBY = updatedBY;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public CredentialDTO(Integer id, Integer userId, String username, String password, String created_by,
			Date createdOn, Date updatedBY, Boolean isActive) {
		super();
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.created_by = created_by;
		this.createdOn = createdOn;
		this.updatedBY = updatedBY;
		this.isActive = isActive;
	}

	public CredentialDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
