
package com.app.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.BuildingDao;
import com.app.dto.BuildingDto;
import com.app.entity.Building;
import com.app.response.Response;
import com.app.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {

	@Autowired
	private BuildingDao buildingDao;

	@Override
	public Response<?> createBuilding(BuildingDto buildingDto) {

		Building buildingName = buildingDao.getBuildingName(buildingDto.getName());
		if(buildingName!=null)
		{
			return new Response<>("Building Name Already Exists !!!", null, HttpStatus.BAD_REQUEST.value());
		}
		
		Building building = BuildingDto.convertToEntity(buildingDto);

		while (true) {

			int randomNumber = (int) (Math.random() * 900000) + 100000; // Generates a random number between 100000 and
																		// 999999
			Building buildingById = buildingDao.getBuildingById(randomNumber);

			if (buildingById == null) {
				building.setBuildingId(randomNumber);
				break;

				
			}

		}

		Building buildingDetails = buildingDao.createBuilding(building);

		if (buildingDetails != null) {
			return new Response<>("Building saved successfully", buildingDetails, HttpStatus.OK.value());
		} else {
			return new Response<>("Building not saved", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> getBuildingById(Integer buildingId) {
		Building buildingDetails = buildingDao.getBuildingById(buildingId);

		if (buildingDetails != null) {
			return new Response<>("Building retrieved successfully", buildingDetails, HttpStatus.OK.value());
		} else {
			return new Response<>("Building not found", null, HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public Response<List<BuildingDto>> getAllBuildings() {

		List<Building> allBuildings = buildingDao.getAllBuildings();

		if (!allBuildings.isEmpty()) {

			List<BuildingDto> buildingDtos = allBuildings.stream().map(BuildingDto::convertToDto)
					.collect(Collectors.toList());

			return new Response<>("All Buildings ", buildingDtos, HttpStatus.OK.value());
		} else {
			return new Response<>("No buildings found", null, HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public Response<?> updateBuilding(Integer buildingId, BuildingDto updatedBuildingDto) {
		
		
		Building existingBuilding = buildingDao.getBuildingById(buildingId);

			
			Building buildingName = buildingDao.getBuildingName(updatedBuildingDto.getName());
			if(buildingName!=null  && !existingBuilding.getBuildingId().equals(buildingName.getBuildingId()))
			{
				return new Response<>("Building Name Can't Be Duplicate", null, HttpStatus.BAD_REQUEST.value());
			}


		if (existingBuilding != null) {
			existingBuilding.setName(updatedBuildingDto.getName());
			existingBuilding.setAddress(updatedBuildingDto.getAddress());
			existingBuilding.setState(updatedBuildingDto.getState());
			existingBuilding.setCity(updatedBuildingDto.getCity());
			Building updateBuilding = buildingDao.updateBuilding(existingBuilding);

			return new Response<>("Building updated successfully", updateBuilding, HttpStatus.OK.value());
		} else {
			return new Response<>("Building not found", null, HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public Response<?> deleteBuilding(Integer buildingId) {
		Building existingBuilding = buildingDao.getBuildingById(buildingId);

		if (existingBuilding != null) {
			Building building = buildingDao.deleteBuilding(existingBuilding);
			return new Response<>("Building deleted successfully", building, HttpStatus.OK.value());
		} else {
			return new Response<>("Building not found", null, HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public Response<?> getBuildingById(String buildingId) {

		Building existingBuilding = buildingDao.getBuildingById(buildingId);
		return new Response<>("Building updated successfully", existingBuilding, HttpStatus.OK.value());

	}
}
