package com.app.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.app.entity.Visitor;

public class VisitorDto {

	private Integer id;

	@NotBlank(message = "Name is required")
	private String name;

	@NotNull
	@NotBlank(message = "Phone number is required")
	@Size(min = 10, max = 13, message = "Phone number should be between 10 and 15 characters")
	private String phoneNumber;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

//	@NotBlank(message = "Address is required")
	private String address;

//	@NotBlank(message = "Gender is required")
	private String gender;

	@NotNull(message = "Age is required")
	private Integer age;

//	@NotBlank(message = "Image is required")
	private String imageUrl;

//	@NotBlank(message = "Address Proof is required")
	private String aadhaarNumber;
	
	private VisitorCompanyDto visitorCompanyDto;

	private StateDto state;
	private CityDto city;

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	

	public VisitorCompanyDto getVisitorCompanyDto() {
		return visitorCompanyDto;
	}

	public void setVisitorCompanyDto(VisitorCompanyDto visitorCompanyDto) {
		this.visitorCompanyDto = visitorCompanyDto;
	}

	public StateDto getState() {
		return state;
	}

	public void setState(StateDto state) {
		this.state = state;
	}

	public CityDto getCity() {
		return city;
	}

	public void setCity(CityDto city) {
		this.city = city;
	}
	
	
	

	public static VisitorDto convertToDTO(Visitor visitor) {
		VisitorDto visitorDTO = new VisitorDto();
		if(visitor == null) {
			return null;
		}
		visitorDTO.setId(visitor.getId());
		visitorDTO.setName(visitor.getName());
		visitorDTO.setPhoneNumber(visitor.getPhoneNumber());
		visitorDTO.setEmail(visitor.getEmail());
		visitorDTO.setAddress(visitor.getAddress());
		visitorDTO.setGender(visitor.getGender());
		visitorDTO.setAge(visitor.getAge());
		visitorDTO.setImageUrl(visitor.getImage());
		visitorDTO.setAadhaarNumber(visitor.getAadhaarNumber());
		visitorDTO.setVisitorCompanyDto(VisitorCompanyDto.toDto(visitor.getCompany()));
		visitorDTO.setState(StateDto.convertEntityToDTO(visitor.getState()));
		visitorDTO.setCity(CityDto.convertEntityToDTO(visitor.getCity()));

		return visitorDTO;
	}

	public static Visitor toVisitor(VisitorDto visitorDto) {
		Visitor visitor = new Visitor();
		if(visitorDto == null) {
			return null;
		}
		visitor.setId(visitorDto.getId());
		visitor.setName(visitorDto.getName());
		visitor.setPhoneNumber(visitorDto.getPhoneNumber());
		visitor.setEmail(visitorDto.getEmail());
		visitor.setAddress(visitorDto.getAddress());
		visitor.setGender(visitorDto.getGender());
		visitor.setAge(visitorDto.getAge());
		visitor.setImage(visitorDto.getImageUrl());
		visitor.setAadhaarNumber(visitorDto.getAadhaarNumber());
		visitor.setCompany(VisitorCompanyDto.toEntity(visitorDto.getVisitorCompanyDto()));
		visitor.setState(StateDto.convertDTOToEntity(visitorDto.getState()));
		visitor.setCity(CityDto.convertDtoToEntity(visitorDto.getCity()));

		return visitor;
	}
}
