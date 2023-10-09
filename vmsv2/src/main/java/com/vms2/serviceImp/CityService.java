package com.vms2.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vms2.dao.CityDao;
import com.vms2.daoImp.CityDaoImp;
import com.vms2.dto.CityDTO;
import com.vms2.entity.City;
import com.vms2.response.Response;

@Service
public class CityService {

	@Autowired
	private CityDao cityDao;
	
	public Response<List<CityDTO>> getCitybyid(Integer id) {
		
		List<City> getcitys = cityDao.getcitys(id);
		
		List<CityDTO> citys=new ArrayList<>();
		
		getcitys.forEach( x -> {
			CityDTO obj=new CityDTO(x);
			citys.add(obj);
		});
		
		return new Response<List<CityDTO>>("ok",citys,HttpStatus.OK.value());
		
		
	}

}
