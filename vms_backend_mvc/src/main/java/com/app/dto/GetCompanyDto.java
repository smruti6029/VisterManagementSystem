package com.app.dto;

public class GetCompanyDto {

	
	private  String logo ;
    private  String name ;
    
    private Integer companyId;
    
    
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	public GetCompanyDto(String logo, String name, Integer companyId) {
		super();
		this.logo = logo;
		this.name = name;
		this.companyId = companyId;
	}
	public GetCompanyDto() {
		super();
		// TODO Auto-generated constructor stub
	}
      
      
      
	
}
