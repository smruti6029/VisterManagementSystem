package com.app.Dao;

import java.util.List;

import com.app.entity.Building;

public interface BuildingDao {

	Building createBuilding(Building building);

	Building getBuildingById(Integer buildingId);

	List<Building> getAllBuildings();

	Building updateBuilding(Building building);

	Building deleteBuilding(Building building);

	Building getBuildingById(String buildingId);
	
	Building getBuildingById1(Integer Id);

	Building getBuildingName(String name);
	
	
	
	
}
