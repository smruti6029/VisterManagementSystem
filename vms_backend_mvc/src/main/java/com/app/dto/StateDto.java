package com.app.dto;

import com.app.entity.State;

public class StateDto {
	
	
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

	public StateDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StateDto(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	public static StateDto convertEntityToDTO(State stateEntity) {
        StateDto stateDTO = new StateDto();
        stateDTO.setId(stateEntity.getId());
        stateDTO.setName(stateEntity.getName());
        return stateDTO;
    }
	
	public static State convertDTOToEntity(StateDto stateDto) {
		State state = new State();
		state.setId(stateDto.getId());
		state.setName(stateDto.getName());
		return state;
	}

}
