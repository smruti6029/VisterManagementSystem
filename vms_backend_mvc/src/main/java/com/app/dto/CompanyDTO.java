package com.app.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Building;
import com.app.entity.City;
import com.app.entity.Company;
import com.app.entity.State;

public class CompanyDTO {

	private Integer id;

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Phone number is required")
	@Size(min = 10, max = 13, message = "Phone number should be 10 characters")
	private String phoneNumber;

	@NotBlank(message = "Address is required")
	private String address;

	private String logo;

	private MultipartFile image;

	@NotBlank(message = "Industry is required")
	private String industry;

	@NotBlank(message = "About us is required")
	private String aboutUs;

	@NotNull(message = "State is required")
	private State state;

	@NotNull(message = "City is required")
	private City city;

	@NotEmpty
	@Pattern(regexp = "\\d{6}", message = "Pincode must be 6 digits")
	private String pincode;

	@NotNull(message = "userLimit is required")
	private Long userLimit;

	private Boolean isActive;
	
	@NotNull(message = "buildingId is required")
	private Building building;

	public Long getUserLimit() {
		return userLimit;
	}

	public void setUserLimit(Long userLimit) {
		this.userLimit = userLimit;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public CompanyDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static CompanyDTO toCompanyDto(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		if (company == null) {
			return null;
		}
         
		companyDTO.setId(company.getId());
		companyDTO.setName(company.getName());
		companyDTO.setEmail(company.getEmail());
		companyDTO.setPhoneNumber(company.getPhoneNumber());
		companyDTO.setAddress(company.getAddress());
		companyDTO.setLogo(company.getLogo());
		companyDTO.setIndustry(company.getIndustry());
		companyDTO.setPincode(company.getPincode());
		companyDTO.setAboutUs(company.getAboutUs());
		companyDTO.setCity(company.getCity());
		companyDTO.setState(company.getState());
		companyDTO.setUserLimit(company.getUserLimit());
		companyDTO.setIsActive(company.isActive());
		companyDTO.setBuilding(company.getBuilding());
		return companyDTO;
	}

	public static Company toCompany(CompanyDTO companyDTO) {
		if (companyDTO == null) {
			return null;
		}
		Company company = new Company();
		company.setId(companyDTO.getId());
		company.setName(companyDTO.getName());
		company.setEmail(companyDTO.getEmail());
		company.setPhoneNumber(companyDTO.getPhoneNumber());
		company.setAddress(companyDTO.getAddress());
		company.setLogo(companyDTO.getLogo());
		company.setIndustry(companyDTO.getIndustry());
		company.setAboutUs(companyDTO.getAboutUs());
		company.setPincode(companyDTO.getPincode());
		company.setState(companyDTO.getState());
		company.setCity(companyDTO.getCity());
		company.setUserLimit(companyDTO.getUserLimit());
		company.setBuilding(companyDTO.getBuilding());
		return company;
	}

}
