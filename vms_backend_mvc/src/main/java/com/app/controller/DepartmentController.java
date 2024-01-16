package com.app.controller;



import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.authorize.Authorization;
import com.app.dto.DepartmentDto;
import com.app.dto.IsActiveDto;
import com.app.entity.User;
import com.app.response.Response;
import com.app.service.DepartmentService;
import com.app.serviceImpl.ExcelImportService;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	

	@Autowired
	private Authorization authorization;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/create")
	public ResponseEntity<?> createDepartment(@RequestBody @Valid DepartmentDto departmentDto) {
		
		if(departmentDto.getCompany()== null && departmentDto.getName()==null )
		{
			return new ResponseEntity<>(new Response<>("Provide Vallid Data",null,HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
		}
		

		Response<?> response = this.departmentService.saveDepartment(departmentDto);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/saveAll")
	public ResponseEntity<?> saveAll(@RequestBody List<DepartmentDto> departments) {

		Response<?> response = this.departmentService.saveAllDepartments(departments);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/")
	public ResponseEntity<?> getDepartmentById(@RequestParam Integer id) {
		Response<?> response = this.departmentService.getById(id);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllDepartments() {
		Response<?> response = this.departmentService.getAll();
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/companyId")
	public ResponseEntity<?> getAllDepartmentsByCompany(@RequestParam Integer companyId) {
		Response<?> response = this.departmentService.getAllByCompanyId(companyId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/user-count-department")
	public ResponseEntity<?> getCountOfUserOfADepartement(@RequestParam Integer companyId) {
		Response<?> response = this.departmentService.getCountOfUsersInADepartment(companyId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/update")
	public ResponseEntity<?> update(@RequestBody @Valid DepartmentDto department ,HttpServletRequest request) {
		
		
		String header = request.getHeader("Authorization");
		
		if (header == null) {
			
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
			
		}else {
			
			Response<?> response = this.departmentService.update(department);
			
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
			
		}
	
		
	
	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteDepartment(IsActiveDto isActiveDto, HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You Are Not Authorize");
		}

		Response<?> response = this.departmentService.delete(isActiveDto, request);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/excel/upload")
	public ResponseEntity<?> upload(@RequestParam MultipartFile file, @RequestParam Integer companyId)
			throws IOException {
		if (ExcelImportService.checkExcelFormat(file)) {
			// true
			Response<?> saveUsersByexcell = departmentService.saveDepartmentByexcell(file, companyId);
			return new ResponseEntity<>(saveUsersByexcell, HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Upload a excel Sheet");
		}
	}

}
