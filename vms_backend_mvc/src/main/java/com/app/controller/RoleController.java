package com.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.authorize.Authorization;
import com.app.dto.RoleDTO;
import com.app.response.Response;
import com.app.service.RoleService;
import com.app.validation.VallidationClass;

@RestController
@RequestMapping("/api/")
public class RoleController {

	@Autowired
	private Authorization authorization;

	@Autowired
	private RoleService roleService;

	@Autowired
	private VallidationClass vallidationClass;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/add")
	public ResponseEntity<?> addRole(@RequestBody RoleDTO roleDto) {
		Response<?> checkroleVallidOrnot = vallidationClass.checkroleVallidOrnot(roleDto);
		if (checkroleVallidOrnot.getStatus() == HttpStatus.OK.value()) {
			Response<?> response = roleService.saveRole(roleDto);
			if (response.getStatus() == HttpStatus.OK.value()) {
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.badRequest().body("Try Again");
			}
		}
		return ResponseEntity.ok(checkroleVallidOrnot);
	}

	@GetMapping("/role/getall")
	public ResponseEntity<?> getAllrole(HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}

		List<RoleDTO> getallRole = roleService.getallRole(header.substring(7));
		if (getallRole != null) {
			return new ResponseEntity<>(new Response<>("Success", getallRole, HttpStatus.OK.value()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Response<>("No Data", null, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("/role/{id}")
	public Response<?> deleteByid(@PathVariable("id") Integer id) {
		Response<?> deleteRoleByid = roleService.deleteRoleByid(id);
		return deleteRoleByid;
	}
	

}
