package com.app.Dao;

import java.util.List;

import org.springframework.data.domain.Page;

import com.app.dto.CompanyDTO;
import com.app.dto.IsActiveDto;
import com.app.dto.PaginationRequest;
import com.app.entity.City;
import com.app.entity.Company;
import com.app.entity.State;

public interface CompanyDao {
	
	    List<Company> getAllCompanies();

	    Company getCompanyById(Integer id);

	    Company saveCompany(Company company);

	    void deleteCompany(Integer id);

		List<Company> getCompanyByStateAndCity(State state, City city);

		Company updateCompany(Integer companyId, Company updatedCompanyDto);

		Company updateCompany(Company company);

		List<Company> searchCompany(Integer stateId, String companyName ,Boolean isActive);

		Integer updateActiveCompany(IsActiveDto activeDto);

		Company findByEmail(String email);

		List<Object[]> findAllCompaniesStatus();

		boolean existsByNameAndStateIdAndCityId(String name, Integer state, Integer city);

		List<Company> getAllCompaniesByBuildingId(Integer buildingId);

		List<Company>  getBuildingById(Integer Id);

		Page<Company> getAllCompanies(PaginationRequest request);

		List<Company> getCompanyByNameAndBuilding(String companyName, int buildingId);

		Company getcompanyByphoneWithBuildingID(CompanyDTO companyDTO);

		Company findByPhone(String phone);

		Company findByCompanyName(String Name);

}