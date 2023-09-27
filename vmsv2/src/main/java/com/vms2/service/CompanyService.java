package com.vms2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vms2.daoImp.CompanyDaoImp;
import com.vms2.dto.CompanyDTO;
import com.vms2.entity.Company;
import com.vms2.response.Response;

@Service
public class CompanyService {
	
	
	@Autowired
	private CompanyDaoImp companyDao;
	
	public Response addCompany(CompanyDTO companyDTO)
	{
		
		Company convertDTOToEntity = Company.convertDTOToEntity(companyDTO);
		
		int addCompany = companyDao.addCompany(convertDTOToEntity);
		
		return new Response("Ok",convertDTOToEntity,HttpStatus.OK.value());
		
		
		
		
		
		
	}

}
