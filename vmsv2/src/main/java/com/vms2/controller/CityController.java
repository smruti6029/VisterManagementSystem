package com.vms2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vms2.dto.CityDTO;
import com.vms2.entity.City;
import com.vms2.response.Response;
import com.vms2.service.CityService;

@RestController
@RequestMapping("/api/")
public class CityController {
	
	@Autowired
	private CityService cityService;
	
	@GetMapping("/city/{id}")
	public ResponseEntity<?> getCity(@PathVariable ("id") Integer id)
	{
	Response<List<CityDTO>> citybyid = cityService.getCitybyid(id);
	
		
		return ResponseEntity.ok(citybyid);
	}

}
