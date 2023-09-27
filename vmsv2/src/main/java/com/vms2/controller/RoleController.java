package com.vms2.controller;

import java.util.List;

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

import com.vms2.dto.RoleDTO;
import com.vms2.response.Response;
import com.vms2.service.RoleService;
import com.vms2.validation.VallidationClass;

@RestController
@RequestMapping("/api/")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private VallidationClass vallidationClass;

	@PostMapping("/add")
	public ResponseEntity<?> addRole(@RequestBody RoleDTO roleDto) {
		Response checkroleVallidOrnot = vallidationClass.checkroleVallidOrnot(roleDto);
		if (checkroleVallidOrnot.getStatus() == HttpStatus.OK.value()) {
			Response response = roleService.saveRole(roleDto);
			if (response.getStatus() == HttpStatus.OK.value()) {
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.badRequest().body("Try Again");
			}
		}
		return ResponseEntity.ok(checkroleVallidOrnot);
	}

	@GetMapping("/role/getall")
	public ResponseEntity<?> getAllrole() {
		List<RoleDTO> getallRole = roleService.getallRole();
		if (getallRole != null) {
			return ResponseEntity.ok(getallRole);
		} else {
			return ResponseEntity.badRequest().body("No Data Found");
		}

	}

	@DeleteMapping("/role/{id}")
	public Response<?> deleteByid(@PathVariable("id") Integer id) {
		Response<?> deleteRoleByid = roleService.deleteRoleByid(id);
		return deleteRoleByid;
	}

}
