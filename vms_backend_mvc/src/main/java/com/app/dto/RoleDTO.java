package com.app.dto;

import com.app.entity.Role;

public class RoleDTO {

	
    private Integer id;
    
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
	public RoleDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoleDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	
	public static RoleDTO entitytoDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }
  
	
	
}
