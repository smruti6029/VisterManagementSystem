package com.app.Dao;

import java.util.List;

import com.app.dto.CityDto;
import com.app.entity.City;

public interface CityDao {
	
	    City getCityById(Integer id);
	 
	    List<City> getAllCities();
	    
	    void saveCity(City city);
	    
	    void updateCity(City city);
	    
	    void deleteCity(City city);

		List<City> getAllCityByStateId(Integer id);
	
		City getByName(String cityName);

		List<CityDto> getCityByName(String cityName);
}
