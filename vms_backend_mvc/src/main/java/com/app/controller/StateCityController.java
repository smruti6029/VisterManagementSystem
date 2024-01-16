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
import org.springframework.web.bind.annotation.RestController;

import com.app.Dao.CityDao;
import com.app.Dao.StateCityRepo;
import com.app.Dao.StateDao;
import com.app.dto.CustomResponseDTO;
import com.app.entity.City;
import com.app.entity.State;

@RestController
@RequestMapping("/sc")
public class StateCityController {
	
	@Autowired
	private StateCityRepo StateCityRepo;
	
	@Autowired
	private StateDao stateRepo;
	
	@Autowired
	private CityDao cityRepo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	 @GetMapping("/states")
	    public ResponseEntity<?> getAllStates() {
		 
	        List<State> states = StateCityRepo.getAllStates();
	        
	        return new ResponseEntity<>(new CustomResponseDTO(states, HttpStatus.OK.name(), HttpStatus.OK.value()),
					HttpStatus.OK);
	    }

	    @GetMapping("/cities")
	    public ResponseEntity<?> getAllCities() {
	    	
	        List<City> cities = StateCityRepo.getAllCities();
	        
	        return new ResponseEntity<>(new CustomResponseDTO(cities, HttpStatus.OK.name(), HttpStatus.OK.value()),
					HttpStatus.OK);
	    }
	
	    @GetMapping("/all/{stateId}")
	    public ResponseEntity<?> getAllCityByStateId(@PathVariable  Integer stateId){
	    	
	    	List<City> cities  = cityRepo.getAllCityByStateId(stateId);
	    	
	    	  return new ResponseEntity<>(new CustomResponseDTO(cities, HttpStatus.OK.name(), HttpStatus.OK.value()),
						HttpStatus.OK);
	 	    	
	   }
	    
	 
	    
	    
	
}
