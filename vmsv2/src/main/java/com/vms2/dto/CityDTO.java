package com.vms2.dto;

import com.vms2.entity.City;
import com.vms2.entity.State;

public class CityDTO {

	
	private Integer id;
	
	private String name;
	
	private StateDTO state;

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

	public StateDTO getState() {
		return state;
	}

	public void setState(StateDTO state) {
		this.state = state;
	}

	public CityDTO(Integer id, String name, StateDTO state) {
		super();
		this.id = id;
		this.name = name;
		this.state = state;
	}

	public CityDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public CityDTO(City cityEntity) {
        this.id = cityEntity.getId();
        this.name = cityEntity.getName();
        State state2 = cityEntity.getState();
        this.state = StateDTO.convertEntityToDTO(state2);
    }
	
	
	
}
