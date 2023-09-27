package com.vms2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vms2.dao.RoleDao;
import com.vms2.dto.RoleDTO;
import com.vms2.entity.Role;
import com.vms2.response.Response;

@Service
public class RoleService {

	@Autowired
	private RoleDao roleDao;

	public Response saveRole(RoleDTO roleDTO) {

		Role role = Role.fromDTO(roleDTO);

		Integer save = roleDao.addRole(role);
		if (save > 0) {
			return new Response("Succesfully Add Role", roleDTO, HttpStatus.OK.value());
		} else {
			return new Response("Try Again", roleDTO, HttpStatus.BAD_REQUEST.value());
		}
	}

	public List<RoleDTO> getallRole() {

		List<Role> getallRole = roleDao.getallRole();

		List<RoleDTO> roleDto = new ArrayList<>();
		if (getallRole == null) {
			return null;
		} else {

			getallRole.forEach(x -> {
				if(!x.getName().equals("SUPERADMIN"))
				{
				RoleDTO entitytoDTO = RoleDTO.entitytoDTO(x);
				roleDto.add(entitytoDTO);
				}
			});
			return roleDto;
		}

	}

	public Response<?> deleteRoleByid(Integer id) {
		Role role = roleDao.getroleByid(id);
		if (role != null) {
			Integer deleteRole = roleDao.deleteRole(role);
			if (deleteRole != 0) {
				return new Response<>(" Deleted Succesfully", role, HttpStatus.OK.value());
			} else {
				return new Response<>("Try Again", id, HttpStatus.BAD_REQUEST.value());
			}
		} else {
			return new Response<>("No Data Found", id, HttpStatus.BAD_REQUEST.value());
		}

	}

}
