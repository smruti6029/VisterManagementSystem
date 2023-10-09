package com.vms2.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vms2.entity.City;
import com.vms2.entity.Company;
import com.vms2.entity.Role;
import com.vms2.entity.State;
import com.vms2.entity.User;
import com.vms2.validation.EmailConstraint;
import com.vms2.validation.PhoneConstraint;

public class UserDto {

	private Integer id;

	private String firstname;

	private String lastname;

	@NotBlank(message = "Email address is required")
	@EmailConstraint(message = "Invalid email address format")
	private String email;

	@NotBlank(message = "Phopne Number Requird")
	@Size(min = 10, max = 10, message = "Phone Must be 10 Digit")
	@PhoneConstraint(message = "Phone Number Format Invalid")
	private String phone;

	private String dob;

	private String image;

	private String pinCode;

	private String created_by;
	
	private String gender;
	
	private String govtId;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createdOn;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:mmm")
	private Date updatedOn;

	private Boolean isActive;

	private Company company;

	private Role role;

	private State state;

	private City city;
	
	
	private String address;
	
	private  Integer updatedBy=null;
	
	private String empCode;

	


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
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

	public Date getupdatedOn() {
		return updatedOn;
	}

	public void setupdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public UserDto() {
		super();
		
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGovtId() {
		return govtId;
	}

	public void setGovtId(String govtId) {
		this.govtId = govtId;
	}

	public UserDto(Integer id, String firstname, String lastname,
			@NotBlank(message = "Email address is required") String email,
			@NotBlank(message = "Phopne Number Requird") @Size(min = 10, max = 10, message = "Phone Must be 10 Digit") String phone,
			String dob, String image, String pinCode, String created_by, String gender, String govtId, Date createdOn,
			Date updatedOn, Boolean isActive, Company company, Role role, State state, City city) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.dob = dob;
		this.image = image;
		this.pinCode = pinCode;
		this.created_by = created_by;
		this.gender = gender;
		this.govtId = govtId;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.isActive = isActive;
		this.company = company;
		this.role = role;
		this.state = state;
		this.city = city;
	}

	public static UserDto convertUserToDTO(User user) {
		UserDto userDTO = new UserDto();
		userDTO.setId(user.getId());
		userDTO.setFirstname(user.getFirstname());
		userDTO.setLastname(user.getLastname());
		userDTO.setEmail(user.getEmail());
		userDTO.setPhone(user.getPhone());
		userDTO.setDob(user.getDob());
		userDTO.setPinCode(user.getPinCode());
		userDTO.setCreated_by(user.getCreated_by());
		userDTO.setCreatedOn(converUtc(user.getCreatedOn()));
		userDTO.setupdatedOn(user.getupdatedOn());
		userDTO.setIsActive(user.getIsActive());
		userDTO.setImage(user.getImage());

		userDTO.setCompany(companyEntityToDTO(user.getCompany()));
		userDTO.setRole(roleEntityToDTO(user.getRole()));
		userDTO.setState(stateEntityToDTO(user.getState()));
		userDTO.setCity(cityEntityToDTO(user.getCity()));
		
		userDTO.setGender(user.getGender());
		userDTO.setGovtId(user.getGovtId());
		userDTO.setAddress(user.getAddress());
		userDTO.setPinCode(user.getPinCode());
		userDTO.setEmpCode(user.getEmpCode());
		

		return userDTO;
	}

	private static Company companyEntityToDTO(Company company) {
		Company companyDTO = new Company();
		companyDTO.setId(company.getId());
		companyDTO.setName(company.getName());
		companyDTO.setAddress(company.getAddress());
		companyDTO.setLogo(company.getLogo());
		

		return companyDTO;
	}

	private static Role roleEntityToDTO(Role role) {
		Role roleDTO = new Role();
		roleDTO.setId(role.getId());
		roleDTO.setName(role.getName());

		return roleDTO;
	}

	private static State stateEntityToDTO(State state) {
		State stateDTO = new State();
		stateDTO.setId(state.getId());
		stateDTO.setName(state.getName());

		return stateDTO;
	}

	private static City cityEntityToDTO(City city) {
		City cityDTO = new City();
		cityDTO.setId(city.getId());
		cityDTO.setName(city.getName());
		return cityDTO;
	}

	public static Date converUtc(Date datetime) {

		Date date = datetime;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set
		Date createdOn2 = datetime;
		return createdOn2;
//	    Calendar calendar = Calendar.getInstance();
//        calendar.setTime(createdOn2);
//        calendar.add(Calendar.HOUR_OF_DAY, 5);
//
//        // Get the updated Date
//        Date updatedDate = calendar.getTime();
//        return updatedDate;
	}

}
