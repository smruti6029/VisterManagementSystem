package com.app.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.app.entity.City;
import com.app.entity.Company;
import com.app.entity.Department;
import com.app.entity.Role;
import com.app.entity.State;
import com.app.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDto {

	private Integer id;

	@NotBlank(message = "Provied First Name")
	private String firstName;

	@NotBlank(message = "Provide Last Name")
	private String lastName;

	@NotBlank(message = "Email address is required")
	private String email;

	@NotBlank(message = "Phone Number Required")
	@Size(min = 10, max = 10, message = "Phone Must be 10 Digit")
	private String phone;

	private String dob;

	private String image;

	
	@Size(min = 6, max = 6, message = "pincode Must be 6 Digit")
	private String pincode;

	private String created_by;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdOn;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedOn;

	private Boolean isActive;

	private Company company;

	private Role role;

	private State state;

	private City city;

	private DepartmentDto departmentDto;

	@Pattern(regexp = "^[0-9a-zA-Z]{4,10}$", message = "Invalid Format for Employee code")
	private String empCode;

	private String gender;

	private String govtId;

	private Boolean isPresent;

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
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

	public DepartmentDto getDepartmentDto() {
		return departmentDto;
	}

	public void setDepartmentDto(DepartmentDto departmentDto) {
		this.departmentDto = departmentDto;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
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

	public Boolean getIsPresent() {
		return isPresent;
	}

	public void setIsPresent(Boolean isPresent) {
		this.isPresent = isPresent;
	}

	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static UserDto convertUserToDTO(User user) throws Exception {
		UserDto userDTO = new UserDto();
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstname());
		userDTO.setLastName(user.getLastname());
		userDTO.setEmail(user.getEmail());
		userDTO.setPhone(user.getPhone());
		userDTO.setDob(user.getDob());
		userDTO.setGender(user.getGender());
		userDTO.setGovtId(user.getGovtId());
		userDTO.setPincode(user.getPinCode());
		userDTO.setCreated_by(user.getCreated_by());
		userDTO.setCreatedOn(convertDateToMilliseconds(user.getCreatedOn()));
		userDTO.setUpdatedOn(user.getUpdatedOn());
		userDTO.setIsActive(user.getIsActive());
		userDTO.setImage(user.getImage());
		userDTO.setDepartmentDto(DepartmentDto.toDepartmentDto(user.getDepartment()));
		userDTO.setEmpCode(user.getEmpCode());
		userDTO.setCompany(companyEntityToDTO(user.getCompany()));
		userDTO.setRole(roleEntityToDTO(user.getRole()));
		userDTO.setState(stateEntityToDTO(user.getState()));
		userDTO.setCity(cityEntityToDTO(user.getCity()));
		userDTO.setIsPresent(user.getIsPresent());
		userDTO.setIsPermission(user.getIsPermission());

		return userDTO;
	}

	private static Company companyEntityToDTO(Company company) {
		Company companyDTO = new Company();
		companyDTO.setId(company.getId());
		companyDTO.setName(company.getName());
		companyDTO.setBuilding(company.getBuilding());

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

	public static Date convertDateToMilliseconds(Date dateString) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = dateString;
			long milliseconds = date.getTime() + 5L * 60L * 60L * 1000L + 30L * 60L * 1000L;
			return new Date(milliseconds);
		} catch (Exception e) {
			return dateString;
		}
	}

	public static User fromDTO(UserDto userDTO) {
		User user = new User();
		user.setFirstname(userDTO.getFirstName().trim());
		user.setLastname(userDTO.getLastName().trim());
		user.setEmail(userDTO.getEmail().trim().trim().toLowerCase());
		user.setPhone(userDTO.getPhone().trim().trim());
		user.setDob(userDTO.getDob().trim());
		user.setPinCode(userDTO.getPincode().trim());
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