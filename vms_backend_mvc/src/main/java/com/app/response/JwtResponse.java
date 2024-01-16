package com.app.response;

public class JwtResponse {

	int id;

	String token;

	String name;

	String role;
	
	String username;
	
	int company_id;
	
	String company_name;
	
	Long limit;
	
	Integer buildingId;


	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

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
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	
	

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String comapany_name) {
		this.company_name = comapany_name;
	}

	public JwtResponse(int id, String token, String name, String role) {
		super();
		this.id = id;
		this.token = token;
		this.name = name;
		this.role = role;
	}

}
