package com.app.dto;


public class IsActiveDto {

	private Integer id;
	
	private Boolean  isActive ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public IsActiveDto(Integer id,Boolean isActive) {
		super();
		this.id = id;
		this.isActive = isActive;
	}

	public IsActiveDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
