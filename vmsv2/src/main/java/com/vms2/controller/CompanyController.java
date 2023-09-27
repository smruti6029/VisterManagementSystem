package com.vms2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vms2.dto.CompanyDTO;
import com.vms2.response.Response;
import com.vms2.service.CompanyService;

@RestController
@RequestMapping("/api/")
public class CompanyController {
	
	
	
	@Autowired
	private CompanyService companyService;
	
	@PostMapping("/company/save")
	public ResponseEntity<?> addCompany(@RequestBody CompanyDTO companyDTO )
	{
		Response addCompany = companyService.addCompany(companyDTO);
		
		return ResponseEntity.ok(addCompany);
	}

}
