package com.vms2.service;

import java.util.List;

import com.vms2.dto.CompanyDTO;
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.entity.Company;



public interface CompanyService {
	
	   List<Company> getAllCompanies();

	    Company getCompanyById(Integer id);

	    Company saveCompany(CompanyDTO companyDto);
	    
	    public Company convertToEntity(CompanyDTO companyDto);

		Company updateCompany(Integer id, Company updatedCompany);

		Company updateCompany(Integer id);

		void isActiveCompany(Integer id);

		List<Company> serachCompany(Integer stateId, Integer cityId, String companyName ,Boolean isActive);

		Integer updateCompany(IdIsactiveDTO activeDto);

		boolean isEmailAlreadyInUse(String email);

	

}
