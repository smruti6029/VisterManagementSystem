package com.app.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cache.annotation.Cacheable;

import com.app.dto.CompanyDTO;
import com.app.dto.CompanyPaginatedResponse;
import com.app.dto.IsActiveDto;
import com.app.dto.PaginationRequest;
import com.app.entity.Company;
import com.app.response.Response;

public interface CompanyService {
	
	

	
	CompanyPaginatedResponse getAllCompanies(PaginationRequest paginationRequest);
	
	List<Company> getAllCompanies();

	CompanyDTO getCompanyById(Integer id);

	CompanyDTO saveCompany(CompanyDTO companyDto);

	CompanyDTO updateCompany(Integer id, CompanyDTO updatedCompanyDto);

	void isActiveCompany(Integer id);

	List<CompanyDTO> searchCompany(Integer stateId, String companyName, Boolean isActive);

	boolean isEmailAlreadyInUse(String email);

	Integer updateCompany(IsActiveDto activeDto);

	List<IsActiveDto> getAllCompaniesStatus();

	boolean existsByNameAndStateIdAndCityId(String name, Integer state, Integer city);

	Response<?> getAllCompaniesByBuildingId(Integer buildingId);

	Response<?> getCompanyByphone(@Valid CompanyDTO companyDTO);

	boolean isPhoneNumberInUser(String phone);

	boolean isCompanyNameInUse(String Name);

	
	

}