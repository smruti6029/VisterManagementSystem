package com.vms2.dto;

import java.util.Date;



public class CompanyDTO {

	
	private Integer id=null;
	
	private String name;
	
	private Date createAt=new Date();
	
	private Boolean isActive=true;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public CompanyDTO(Integer id, String name, Date createAt, Boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.createAt = createAt;
		this.isActive = isActive;
	}

	public CompanyDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
