package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.RoleDao;
import com.app.Dao.UserDao;
import com.app.dto.RoleDTO;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.response.Response;
import com.app.security.JwtHelper;

@Service
public class RoleService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private JwtHelper helper;

	@Autowired
	private UserDao userDao;

	public Response<?> saveRole(RoleDTO roleDTO) {

		Role role = Role.fromDTO(roleDTO);

		Integer save = roleDao.addRole(role);
		if (save > 0) {
			return new Response<>("Succesfully Add Role", roleDTO, HttpStatus.OK.value());
		} else {
			return new Response<>("Try Again", roleDTO, HttpStatus.BAD_REQUEST.value());
		}
	}

	public List<RoleDTO> getallRole(String string) {

		List<Role> getallRole = roleDao.getallRole();

		String usernameFromToken = helper.getUsernameFromToken(string);

		User user = userDao.getUserbyPhone(usernameFromToken);

		List<RoleDTO> roleDto = new ArrayList<>();

		if (getallRole == null) {
			return null;
		} else {

			getallRole.forEach(x -> {
				
				if (!x.getName().equalsIgnoreCase("SUPERADMIN")) {

					if (user.getRole().getName().equalsIgnoreCase("SUPERADMIN")) {
						if (x.getName().equalsIgnoreCase("ADMIN")) {
							RoleDTO entitytoDTO = RoleDTO.entitytoDTO(x);
							roleDto.add(entitytoDTO);
						}

					} else if (user.getRole().getName().equalsIgnoreCase("ADMIN")) {
						if(!x.getName().equalsIgnoreCase("ADMIN"))
						{
							RoleDTO entitytoDTO = RoleDTO.entitytoDTO(x);
							roleDto.add(entitytoDTO);
						}

					}

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
