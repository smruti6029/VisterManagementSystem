package com.vms2.response;

public class JwtResponse {

	int id;

	String token;

	String name;

	String role;;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public JwtResponse(String token) {
		super();
		this.token = token;
	}

	
	
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public JwtResponse() {
		super();
	}

	public JwtResponse(String token, String name, String role) {
		super();
		this.token = token;
		this.name = name;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JwtResponse(int id, String token, String name, String role) {
		super();
		this.id = id;
		this.token = token;
		this.name = name;
		this.role = role;
	}

}
