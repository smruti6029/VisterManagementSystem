package com.vms2.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.vms2.entity.City;
import com.vms2.entity.Company;
import com.vms2.entity.Role;
import com.vms2.entity.State;
import com.vms2.entity.User;
import com.vms2.validation.EmailConstraint;
import com.vms2.validation.PhoneConstraint;

public class UserDto {

	private Integer id;
	
	@NotBlank(message = "Provied Name")
	private String name;
	
	@NotBlank(message = "Email address is required")
	@EmailConstraint(message = "Invalid email address format")
	private String email;
	
	@NotBlank(message = "Phopne Number Requird")
	@Size(min =10,max = 10,message = "Phone Must be 10 Digit")
	@PhoneConstraint(message = "Phone Number Format Invalid")
	private String phone;
	
	private String dob;
	
	private String pinCode;
	
	private String created_by;
	
	private Date createdOn;
	
	private Date updatedBY;
	
	private Boolean isActive;
	
	private Company company;
	
	private Role role;
	
	private State state;
	
	private City city;

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

	public Date getUpdatedBY() {
		return updatedBY;
	}

	public void setUpdatedBY(Date updatedBY) {
		this.updatedBY = updatedBY;
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
		// TODO Auto-generated constructor stub
	}

	public UserDto(Integer id, String name, String email, String phone, String dob, String pinCode, String created_by,
			Date createdOn, Date updatedBY, Boolean isActive, Company company, Role role, State state,
			City city) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.dob = dob;
		this.pinCode = pinCode;
		this.created_by = created_by;
		this.createdOn = createdOn;
		this.updatedBY = updatedBY;
		this.isActive = isActive;
		this.company = company;
		this.role = role;
		this.state = state;
		this.city = city;
	}
	
	
	
	
	
	public UserDto convertUserToDTO(User user) {
	    UserDto userDTO = new UserDto();
	    userDTO.setId(user.getId());
	    userDTO.setName(user.getName());
	    userDTO.setEmail(user.getEmail());
	    userDTO.setPhone(user.getPhone());
	    userDTO.setDob(user.getDob());
	    userDTO.setPinCode(user.getPinCode());
	    userDTO.setCreated_by(user.getCreated_by());
	    userDTO.setCreatedOn(user.getCreatedOn());
	    userDTO.setUpdatedBY(user.getUpdatedBY());
	    userDTO.setIsActive(user.getIsActive());
	    
	    // You can also convert the associated entities (Company, Role, State, City) to their respective DTOs
	    userDTO.setCompany(companyEntityToDTO(user.getCompany()));
	    userDTO.setRole(roleEntityToDTO(user.getRole()));
	    userDTO.setState(stateEntityToDTO(user.getState()));
	    userDTO.setCity(cityEntityToDTO(user.getCity()));
	    
	    return userDTO;
	}

	private Company companyEntityToDTO(Company company) {
		Company companyDTO = new Company();
		companyDTO.setId(company.getId());
		companyDTO.setName(company.getName());
	    
	    return companyDTO;
	}

	private Role roleEntityToDTO(Role role) {
		Role roleDTO = new Role();
	    roleDTO.setId(role.getId());
	    roleDTO.setName(role.getName());
	    
	    
	    
	    return roleDTO;
	}

	private State stateEntityToDTO(State state) {
		State stateDTO = new State();
	    stateDTO.setId(state.getId());
	    stateDTO.setName(state.getName());
	    
	    
	    return stateDTO;
	}

	private City cityEntityToDTO(City city) {
		City cityDTO = new City();
	    cityDTO.setId(city.getId());
	    cityDTO.setName(city.getName()); 
	    return cityDTO;
	}

	
	
	
	

}
