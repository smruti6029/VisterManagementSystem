package com.app.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.CityDto;
import com.app.response.Response;
import com.app.service.CityService;


@RestController
@RequestMapping("/api/")
public class CityController {

	@Autowired
	private CityService cityService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/city/{id}")
	public ResponseEntity<?> getCity(@PathVariable("id") Integer id) {

		if (id == null) {

			return new ResponseEntity<>(new Response<>("No State Selected", null, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}
		Response<List<CityDto>> citybyid = cityService.getCitybyid(id);

		return ResponseEntity.ok(citybyid);
	}
	
	    @GetMapping("/cityByName")
	    public ResponseEntity<?> getCityByName(@RequestParam("cityName") String cityName) {
	    	
	        if (cityName == null || cityName.trim().isEmpty()) {
	            return new ResponseEntity<>(new Response<>("City name cannot be empty", null, HttpStatus.BAD_REQUEST.value()),
	                    HttpStatus.BAD_REQUEST);
	        }

	        Response<List<CityDto>> cityByNameResponse = cityService.getCityByName(cityName);

	        return ResponseEntity.ok(cityByNameResponse);
	    }
	
	

}
