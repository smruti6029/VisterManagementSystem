package com.app.Dao;

import java.util.List;

import com.app.entity.City;
import com.app.entity.State;

public interface StateCityRepo {
	
	List<State> getAllStates();

    List<City> getAllCities();
	

}
