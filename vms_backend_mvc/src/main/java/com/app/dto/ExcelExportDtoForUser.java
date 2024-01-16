package com.app.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExcelExportDtoForUser {

	public String firstName;
	public String lastName;
	public String email;
	public String phone;
	public String dob;
	public String pincode;

	public String companyName;
	public String roleName;
	public String stateName;
	public String cityName;
	public String departmentName;
	public String empCode;
	public String gender;
	public String govtId;

	public String joinDate;

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

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
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

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public List<ExcelExportDtoForUser> convertUsersToExcelExportDto(List<UserDto> users) {
		List<ExcelExportDtoForUser> excelExportDtos = new ArrayList<>();

		for (UserDto user : users) {
			excelExportDtos.add(convertUserToExcelExportDto(user));
		}

		return excelExportDtos;
	}

	private ExcelExportDtoForUser convertUserToExcelExportDto(UserDto user) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		ExcelExportDtoForUser excelExportDto = new ExcelExportDtoForUser();
//	        excelExportDto.id = user.getId();
		excelExportDto.firstName = user.getFirstName();
		excelExportDto.lastName = user.getLastName();
		excelExportDto.email = user.getEmail();
		excelExportDto.phone = user.getPhone();
		excelExportDto.dob = user.getDob();
		excelExportDto.pincode = user.getPincode();
		excelExportDto.companyName = (user.getCompany() != null) ? user.getCompany().getName() : null;
		excelExportDto.roleName = (user.getRole() != null) ? user.getRole().getName() : null;
		excelExportDto.stateName = (user.getState() != null) ? user.getState().getName() : null;
		excelExportDto.cityName = (user.getCity() != null) ? user.getCity().getName() : null;
		excelExportDto.departmentName = (user.getDepartmentDto() != null) ? user.getDepartmentDto().getName() : null;
		excelExportDto.empCode = user.getEmpCode();
		excelExportDto.gender = user.getGender();
		excelExportDto.govtId = user.getGovtId();

		excelExportDto.joinDate = (user.getCreatedOn() != null) ? dateFormat.format(user.getCreatedOn()) : null;

		return excelExportDto;
	}

}
