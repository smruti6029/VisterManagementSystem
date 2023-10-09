package com.vms2.dao;


import java.util.List;

import com.vms2.dto.IdIsactiveDTO;
import com.vms2.entity.City;
import com.vms2.entity.Company;
import com.vms2.entity.State;



public interface CompanyRepo {
	
	    List<Company> getAllCompanies();

	    Company getCompanyById(Integer id);

	    Company saveCompany(Company company);

	    void deleteCompany(Integer id);

		List<Company> getCompanyByStateAndCity(State state, City city);

		Company updateCompany(Integer companyId, Company updatedCompanyDto);

		Company updateCompany(Integer companyId);

		List<Company> serachCompany(Integer stateId, Integer cityId, String companyName ,Boolean isActive);

		Integer updateActiveCompany(IdIsactiveDTO activeDto);

		Company findByEmail(String email);

}
