package com.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.VisitorCompanyDao;
import com.app.dto.DepartmentDto;
import com.app.dto.VisitorCompanyDto;
import com.app.entity.Company;
import com.app.entity.State;
import com.app.entity.VisitorCompany;
import com.app.response.Response;
import com.app.service.VisitorCompanyService;

@Service
public class VisitorCompanyServiceImpl  implements VisitorCompanyService{

	@Autowired
	private VisitorCompanyDao visitorCompanyDao;
	
	
	@Override
	public VisitorCompany save(VisitorCompanyDto visitorCompanyDto) {
		
		VisitorCompany visitorCompany = VisitorCompanyDto.toEntity(visitorCompanyDto);
		
		if(visitorCompany!=null) {
		return visitorCompanyDao.save(visitorCompany);
		}
		
		return null;
	}
	
	@Override
	public VisitorCompany getById(Integer id) {

		if(id!=null) {
			
			VisitorCompany visitorCompany = visitorCompanyDao.getById(id);
			
			return visitorCompany;
			
		}
	
		return null;
	}

	@Override
	public Response<?> getAll() {
		List<VisitorCompanyDto> visitorCompanyDtos = visitorCompanyDao.getAll().stream(
				).map(VisitorCompanyDto::toDto).collect(Collectors.toList());

		if (visitorCompanyDtos != null && !visitorCompanyDtos.isEmpty()) {

			return new Response<>("All Visitorcompany retrieved", visitorCompanyDtos, HttpStatus.OK.value());
		} else {
			return new Response<>("No Visitorcompany found", null, HttpStatus.NOT_FOUND.value());
		}
	}
	

}


