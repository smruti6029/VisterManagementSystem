package com.app.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangePasswordDto {
	
	
	@NotBlank(message = "Provied Name")
	private String username;
	
	
	private String oldpassword;
	
	
	@NotBlank(message = "Enter New Password")	
	private String newPassword;
	
	private Integer otp;
	
	
	
	
	
	

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

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



	public ChangePasswordDto(String username, String oldpassword, String newPassword, String newPassword1) {
		super();
		this.username = username;
		this.oldpassword = oldpassword;
		this.newPassword = newPassword;
	}

	public ChangePasswordDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
