package com.app.dto;

import com.app.entity.VisitorCompany;

public class VisitorCompanyDto {

	private Integer id;
	private String name;

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

	public VisitorCompanyDto(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public VisitorCompanyDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static VisitorCompanyDto toDto(VisitorCompany visitorCompany) {
		if (visitorCompany == null) {
			return null;
		}
		VisitorCompanyDto visiitorCompanyDto = new VisitorCompanyDto();

		visiitorCompanyDto.setId(visitorCompany.getId());
		if (visitorCompany.getName() != null && !visitorCompany.getName().isEmpty()) {
			visiitorCompanyDto.setName(visitorCompany.getName().trim());

		}


		return visiitorCompanyDto;
	}

	public static VisitorCompany toEntity(VisitorCompanyDto visitorCompanyDto) {
		if (visitorCompanyDto == null) {
			return null;
		}
		VisitorCompany visiitorCompany = new VisitorCompany();

		visiitorCompany.setId(visitorCompanyDto.getId());
		if (visitorCompanyDto.getName() != null && !visitorCompanyDto.getName().isEmpty()) {
			visiitorCompany.setName(visitorCompanyDto.getName().trim());
		}
		return visiitorCompany;
	}

}
