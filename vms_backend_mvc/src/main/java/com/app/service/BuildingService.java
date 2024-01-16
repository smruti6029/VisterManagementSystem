package com.app.service;

import java.util.List;

import com.app.dto.BuildingDto;
import com.app.response.Response;

public interface BuildingService {

	Response<?> createBuilding(BuildingDto buildingDto);

	Response<?> getBuildingById(Integer buildingId);

	Response<List<BuildingDto>> getAllBuildings();

	Response<?> updateBuilding(Integer buildingId, BuildingDto updatedBuildingDto);

	Response<?> deleteBuilding(Integer buildingId);

	Response<?> getBuildingById(String buildingId);

}
