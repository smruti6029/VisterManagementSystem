package com.app.dto;

public class IdNameDto {

	private Long id;

	private String name;

	public IdNameDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IdNameDto(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

}
