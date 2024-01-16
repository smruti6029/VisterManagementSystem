package com.app.dto;

import java.util.List;

public class CompanyDepartmentResponseDTO {

	private Integer departmentId;

	private String departmentName;

	private Integer companyId;

	private String companyName;

	private Long userCount;

//	private List<UserDto> users;
//
//	public List<UserDto> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<UserDto> users) {
//		this.users = users;
//	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getUserCount() {
		return userCount;
	}

	public void setUserCount(Long userCount) {
		this.userCount = userCount;
	}

	public CompanyDepartmentResponseDTO(int companyId, String companyName, int departmentId, String departmentName,
			long userCount) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.userCount = userCount;
	}

	public CompanyDepartmentResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
