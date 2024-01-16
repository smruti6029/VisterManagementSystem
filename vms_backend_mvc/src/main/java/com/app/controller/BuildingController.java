
package com.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.BuildingDto;
import com.app.response.Response;
import com.app.service.BuildingService;

@RestController
@RequestMapping("/api/building")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/save")
    public ResponseEntity<?> addBuilding(@RequestBody @Valid BuildingDto buildingDto) {
    	
    	if(buildingDto.getState().getId() == null ) {
    		
    		return new ResponseEntity<>((new Response<>("state is not selected", null, HttpStatus.BAD_REQUEST.value())),
					HttpStatus.BAD_REQUEST);	
    		
    	}
    	
    	if(buildingDto.getCity().getId() ==null) {
    		return new ResponseEntity<>((new Response<>("city is not selected", null, HttpStatus.BAD_REQUEST.value())),
					HttpStatus.BAD_REQUEST);
    	}
    	
        Response<?> createBuilding = buildingService.createBuilding(buildingDto);

        if (createBuilding != null) {
            return new ResponseEntity<>(createBuilding,HttpStatus.valueOf(createBuilding.getStatus()));
        }

        return new ResponseEntity<>("Building Not Found", null, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getBuilding(@PathVariable Integer id) {
        Response<?> buildingResponse = buildingService.getBuildingById(id);

        if (buildingResponse != null) {
            return new ResponseEntity<>(buildingResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>("Building Not Found", null, HttpStatus.NOT_FOUND);
    }
    
    
    //version v2//////for Generate Unique code of buliding
    @GetMapping("/int/{id}")
    public ResponseEntity<?> getBuilding(@PathVariable String id) {
        Response<?> buildingResponse = buildingService.getBuildingById(id);

        if (buildingResponse != null) {
            return new ResponseEntity<>(buildingResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>("Building Not Found", null, HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBuildings() {
    	
        Response<List<BuildingDto>> allBuildingsResponse = buildingService.getAllBuildings();

        if (allBuildingsResponse != null) {
            return new ResponseEntity<>(allBuildingsResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>("No buildings found", null, HttpStatus.NOT_FOUND);
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateBuilding(@RequestBody  BuildingDto updatedBuildingDto) {
    	
    	System.out.println(updatedBuildingDto.getId() + "                                 ");
    	
    	if(updatedBuildingDto.getId()==null)
    	{
    		return new ResponseEntity<>("Provide Buliding Id", null, HttpStatus.BAD_REQUEST);
    	}
        Response<?> updateResponse = buildingService.updateBuilding(updatedBuildingDto.getId(), updatedBuildingDto);

        if (updateResponse != null) {
            return new ResponseEntity<>(updateResponse, HttpStatus.valueOf(updateResponse.getStatus()));
        }

        return new ResponseEntity<>("Building Not Found", null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBuilding(@PathVariable Integer id) {
        Response<?> deleteResponse = buildingService.deleteBuilding(id);

        if (deleteResponse != null) {
            return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>("Building Not Found", null, HttpStatus.NOT_FOUND);
    }
}
