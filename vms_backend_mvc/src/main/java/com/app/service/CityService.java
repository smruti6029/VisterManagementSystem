package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.CityDao;
import com.app.dto.CityDto;
import com.app.entity.City;
import com.app.response.Response;

@Service
public class CityService {

	@Autowired
	private CityDao cityDao;
	
	public Response<List<CityDto>> getCitybyid(Integer id) {
		
		List<City> getcitys = cityDao.getAllCityByStateId(id);
		
		List<CityDto> citys=new ArrayList<>();
		
		getcitys.forEach( x -> {
			CityDto obj=new CityDto(x);
			citys.add(obj);
		});
		
		return new Response<List<CityDto>>("ok",citys,HttpStatus.OK.value());
		
		
	}

	public Response<List<CityDto>> getCityByName(String cityName) {
		
		 try {
	            List<CityDto> cities = cityDao.getCityByName(cityName);
	            return new Response<>("Success", cities, HttpStatus.OK.value());
	        } catch (Exception e) {
	            return new Response<>("Error retrieving cities by name", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
	        }
	}


}
