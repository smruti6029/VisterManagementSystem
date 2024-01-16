package com.app.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.app.dto.UserDto;

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

	@Column(name = "updated_by")
	private Date updatedOn;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "image")
	private String image;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

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

	@Column(name = "is_present")
	private Boolean isPresent;

	@Column(name = "gender")
	private String gender;

	@Column(name = "govt_id")
	private String govtId;

	@Column(name = "approve_permission")
	private Boolean isPermission;

	public Boolean getIsPermission() {
		return isPermission;
	}

	public void setIsPermission(Boolean isPermission) {
		this.isPermission = isPermission;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
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

	public CredentialMaster getCrediantial() {
		return crediantial;
	}

	public void setCrediantial(CredentialMaster crediantial) {
		this.crediantial = crediantial;
	}

	public Boolean getIsPresent() {
		return isPresent;
	}

	public void setIsPresent(Boolean isPresent) {
		this.isPresent = isPresent;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public User(Integer id, String firstname, String lastname, String email, String phone, String dob, String pinCode,
			String created_by, Date createdOn, Date updatedOn, Boolean isActive, String image, Department department,
			String empCode, Company company, Role role, State state, City city, CredentialMaster crediantial,
			Boolean isPresent) {
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
		this.image = image;
		this.department = department;
		this.empCode = empCode;
		this.company = company;
		this.role = role;
		this.state = state;
		this.city = city;
		this.crediantial = crediantial;
		this.isPresent = isPresent;
	}

	public User() {
		super();
	}

	public static User fromDTO(UserDto userDTO) {
		User user = new User();
		user.setFirstname(userDTO.getFirstName().trim());
		user.setLastname(userDTO.getLastName().trim());
		user.setEmail(userDTO.getEmail().trim().toLowerCase());
		user.setPhone(userDTO.getPhone().trim());
		user.setDob(userDTO.getDob().trim());
		user.setPinCode(userDTO.getPincode());
		user.setEmpCode(userDTO.getEmpCode().trim());
		user.setImage(userDTO.getImage());
		user.setGender(userDTO.getGender());
		user.setGovtId(userDTO.getGovtId().trim());
		user.setIsPermission(userDTO.getIsPermission());

		Department department = new Department();
		department.setId(userDTO.getDepartmentDto().getId());
		user.setDepartment(department);

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
}
