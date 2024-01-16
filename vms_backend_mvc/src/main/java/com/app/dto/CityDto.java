package com.app.dto;

import com.app.entity.City;
import com.app.entity.State;

public class CityDto {

	private Integer id;

	private String name;

	private StateDto state;

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

	public StateDto getState() {
		return state;
	}

	public void setState(StateDto state) {
		this.state = state;
	}

	public CityDto(Integer id, String name, StateDto state) {
		super();
		this.id = id;
		this.name = name;
		this.state = state;
	}

	public CityDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CityDto(City cityEntity) {
		this.id = cityEntity.getId();
		this.name = cityEntity.getName();
		State state2 = cityEntity.getState();
		this.state = StateDto.convertEntityToDTO(state2);
	}

	public static CityDto convertEntityToDTO(City city) {
		CityDto citydto = new CityDto();
		citydto.setId(city.getId());
		citydto.setName(city.getName());
		return citydto;
	}

	public static City convertDtoToEntity(CityDto citydto) {
		City city = new City();
		city.setId(citydto.getId());
		city.setName(citydto.getName());
		return city;
	}

}
