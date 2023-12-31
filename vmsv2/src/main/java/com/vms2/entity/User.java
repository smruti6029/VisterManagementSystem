package com.vms2.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.vms2.dto.UserDto;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "email")
	private String email;

	@Column(name = "phone")
	private String phone;

	@Column(name = "dob")
	private String dob;

	@Column(name = "pincode")
	private String pinCode;

	@Column(name = "created_by")
	private String created_by;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "is_active")
	private Boolean isActive;
		
	@Column(name = "image")
	private String image;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "govt_id")
	private String govtId;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "updated_by")
	private  Integer updatedBy;
	
	@Column(name = "emp_code")
	private String empCode;
	
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "role")
	private Role role;

	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;

	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;

	
	@OneToOne(mappedBy = "user")
	private CredentialMaster crediantial;

	
	
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

	public User(Integer id, String firstname, String lastname, String email, String phone, String dob, String pinCode,
			String created_by, Date createdOn, Date updatedOn, Boolean isActive, Company company, Role role,
			State state, City city) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.dob = dob;
		this.pinCode = pinCode;
		this.created_by = created_by;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.isActive = isActive;
		this.company = company;
		this.role = role;
		this.state = state;
		this.city = city;
	}

	public User() {
		super();
	}

	
	
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public static User fromDTO(UserDto userDTO) {
		User user = new User();
		user.setFirstname(userDTO.getFirstname());
		user.setLastname(userDTO.getLastname());
		user.setEmail(userDTO.getEmail().trim());
		user.setPhone(userDTO.getPhone().trim());
		user.setDob(userDTO.getDob().trim());
		user.setPinCode(userDTO.getPinCode().trim());
		user.setImage(userDTO.getImage());
		
		user.setAddress(userDTO.getAddress());
		user.setEmpCode(user.getEmpCode());
		user.setGender(userDTO.getGender());
		user.setGovtId(userDTO.getGovtId());
		
		user.setAddress(userDTO.getAddress());
		user.setPinCode(userDTO.getPinCode());
		user.setEmpCode(userDTO.getEmpCode());

		Company company = new Company();
		company.setId(userDTO.getCompany().getId());
		user.setCompany(company);

		Role role = new Role();
		role.setId(userDTO.getRole().getId());
		user.setRole(role);

		State state = new State();
		state.setId(userDTO.getState().getId());
		user.setState(state);

		City city = new City();
		city.setId(userDTO.getCity().getId());
		user.setCity(city);

		return user;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
