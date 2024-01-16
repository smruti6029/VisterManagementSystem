package com.app.dto;

import com.app.entity.User;

public class UserTrialDto {

	private Integer id;

	private String firstName;

	private String lastName;

	private String role;

	private String email;

	private String phone;

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public UserTrialDto(Integer id, String firstName, String lastName, String role, String email, String phone) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.email = email;
		this.phone = phone;
	}

	public UserTrialDto() {
		super();
	}

	public static UserTrialDto userToUserTrial(User user) {
		if (user == null) {
			return null;
		}

		UserTrialDto userDto = new UserTrialDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstname());
		userDto.setLastName(user.getLastname());
		userDto.setEmail(user.getEmail());
		userDto.setPhone(user.getPhone());
		if (user.getRole() != null) {
			userDto.setRole(user.getRole().getName());
		}
		return userDto;
	}

}
