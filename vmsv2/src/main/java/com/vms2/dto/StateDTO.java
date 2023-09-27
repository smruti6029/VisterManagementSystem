package com.vms2.dto;

import com.vms2.entity.State;

public class StateDTO {
	
	
	private Integer id=null;
	
	private String name;

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

	public StateDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StateDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	public static StateDTO convertEntityToDTO(State stateEntity) {
        StateDTO stateDTO = new StateDTO();
        stateDTO.setId(stateEntity.getId());
        stateDTO.setName(stateEntity.getName());
        return stateDTO;
    }

}
