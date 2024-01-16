package com.app.dto;

import java.util.List;

public class AlluserBycompanyId {

	int countUser;

	String excelUrl;

	List<UserDto> allUser;

	public String getExcelUrl() {
		return excelUrl;
	}

	public void setExcelUrl(String excelUrl) {
		this.excelUrl = excelUrl;
	}

	public int getCountUser() {
		return countUser;
	}

	public void setCountUser(int countUser) {
		this.countUser = countUser;
	}

	public List<UserDto> getAllUser() {
		return allUser;
	}

	public void setAllUser(List<UserDto> allUser) {
		this.allUser = allUser;
	}

	public AlluserBycompanyId(int countUser, List<UserDto> allUser) {
		this.countUser = countUser;
		this.allUser = allUser;
	}

}

