package com.vms2.dto;

public class IdIsactiveDTO {
	
	
	Integer id;
	
	Boolean isActive;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isAactive) {
		this.isActive = isAactive;
	}

	public IdIsactiveDTO(Integer id, Boolean isAactive) {
		super();
		this.id = id;
		this.isActive = isAactive;
	}

	public IdIsactiveDTO() {
		super();
	}
	
	

}
