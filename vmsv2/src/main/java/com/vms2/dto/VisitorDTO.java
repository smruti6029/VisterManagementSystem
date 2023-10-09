package com.vms2.dto;

import com.vms2.entity.Visitor;

public class VisitorDTO {

	private Integer id;
	private String name;
	private String phoneNumber;
	private String email;
	private String address;
	private String gender;
	private Integer age;
	private String image;
	private String aadhaarNumber;
	private String companyName;
	private String stateName;
	private String cityName;
	
	

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



	public String getImage() {
		return image;
	}



	public void setImage(String image) {
		this.image = image;
	}



	public String getAadhaarNumber() {
		return aadhaarNumber;
	}



	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}



	public String getCompanyName() {
		return companyName;
	}



	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}



	public String getStateName() {
		return stateName;
	}



	public void setStateName(String stateName) {
		this.stateName = stateName;
	}



	public String getCityName() {
		return cityName;
	}



	public void setCityName(String cityName) {
		this.cityName = cityName;
	}



	public  static VisitorDTO convertToDTO(Visitor visitor) {
		VisitorDTO visitorDTO = new VisitorDTO();
		visitorDTO.setId(visitor.getId());
		visitorDTO.setName(visitor.getName());
		visitorDTO.setPhoneNumber(visitor.getPhoneNumber());
		visitorDTO.setEmail(visitor.getEmail());
		visitorDTO.setAddress(visitor.getAddress());
		visitorDTO.setGender(visitor.getGender());
		visitorDTO.setAge(visitor.getAge());
		visitorDTO.setImage(visitor.getImage());
		visitorDTO.setAadhaarNumber(visitor.getAadhaarNumber());
		visitorDTO.setCompanyName(visitor.getCompanyName());
		visitorDTO.setStateName(visitor.getState() != null ? visitor.getState().getName() : null);
		visitorDTO.setCityName(visitor.getCity() != null ? visitor.getCity().getName() : null);

		return visitorDTO;
	}
}
