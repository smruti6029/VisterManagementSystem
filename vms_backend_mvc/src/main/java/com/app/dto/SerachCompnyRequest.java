package com.app.dto;



public class SerachCompnyRequest {
	
     private String companyName;
     
     private Boolean isActive;
     
     private CityDto cityId;
     
     private StateDto stateId;



	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public CityDto getCityId() {
		return cityId;
	}

	public void setCityId(CityDto cityId) {
		this.cityId = cityId;
	}

	public StateDto getStateId() {
		return stateId;
	}

	public void setStateId(StateDto stateId) {
		this.stateId = stateId;
	}
	
     
     

}
