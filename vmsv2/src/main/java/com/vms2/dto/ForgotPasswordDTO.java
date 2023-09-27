package com.vms2.dto;

import javax.validation.constraints.NotBlank;

public class ForgotPasswordDTO {
	
	
	@NotBlank(message = "Provied Name")
	String username;
	
	
	@NotBlank(message = "Provied Old Password")
	String oldpassword;
	
	@NotBlank(message = "Enter New Password")
	String newPassword;
	
	@NotBlank(message = "Enter Again  Same New Password")
	String newPassword1;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}

	public ForgotPasswordDTO(String username, String oldpassword, String newPassword, String newPassword1) {
		super();
		this.username = username;
		this.oldpassword = oldpassword;
		this.newPassword = newPassword;
		this.newPassword1 = newPassword1;
	}

	public ForgotPasswordDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
