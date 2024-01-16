package com.app.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.app.entity.Building;
import com.app.entity.City;
import com.app.entity.State;

public class BuildingDto {

	private Integer id;

	private Integer BuildingId;

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Address is required")
	private String address;

	@NotNull(message = "State is required")
	private State state;

	@NotNull(message = "City is required")
	private City city;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}



	public Integer getBuildingId() {
		return BuildingId;
	}

	public void setBuildingId(Integer buildingId) {
		BuildingId = buildingId;
	}

	public BuildingDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static BuildingDto convertToDto(Building building) {

		BuildingDto buildingDto = new BuildingDto();

		if (building == null) {
			return null;
		}

		buildingDto.setId(building.getBuildingId());
		buildingDto.setName(building.getName());
		buildingDto.setState(building.getState());
		buildingDto.setCity(building.getCity());
		buildingDto.setAddress(building.getAddress());
		buildingDto.setBuildingId(building.getBuildingId());

		return buildingDto;
	}

	public static Building convertToEntity(BuildingDto buildingDto) {

		if (buildingDto == null) {
			return null;
		}

		Building building = new Building();
		
		building.setId(buildingDto.getId());
		building.setName(buildingDto.getName());
		building.setState(buildingDto.getState());
		building.setCity(buildingDto.getCity());
		building.setAddress(buildingDto.getAddress());
		building.setBuildingId(buildingDto.getBuildingId());
		
		
		return building;
	}

}