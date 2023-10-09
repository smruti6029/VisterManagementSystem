package com.vms2.dto;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vms2.exception.UniqueEmail;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyDTO {
	
	int id;
	
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @UniqueEmail
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 13, message = "Phone number should be between 10 and 15 characters")
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
    private Integer state;

    @NotNull(message = "City is required")
    private Integer city;
    
    @NotEmpty
    @Pattern(regexp = "\\d{6}", message = "Pincode must be 6 digits")
    private String pincode;

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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}
	

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CompanyDTO(@NotBlank(message = "Name is required") String name,
			@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
			@NotBlank(message = "Phone number is required") @Size(min = 10, max = 13, message = "Phone number should be between 10 and 15 characters") String phoneNumber,
			@NotBlank(message = "Address is required") String address, String logo, MultipartFile image,
			@NotBlank(message = "Industry is required") String industry,
			@NotBlank(message = "About us is required") String aboutUs,
			@NotNull(message = "State is required") Integer state, @NotNull(message = "City is required") Integer city,
			@NotEmpty @Pattern(regexp = "\\d{6}", message = "Pincode must be 6 digits") String pincode) {
		super();
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.logo = logo;
		this.image = image;
		this.industry = industry;
		this.aboutUs = aboutUs;
		this.state = state;
		this.city = city;
		this.pincode = pincode;
	}

	public CompanyDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	

//	public State getState() {
//		return state;
//	}
//
//	public void setState(State state) {
//		this.state = state;
//	}
//
//	public City getCity() {
//		return city;
//	}
//
//	public void setCity(City city) {
//		this.city = city;
//	}

    
    

}
