package com.app.service;

import com.app.dto.VisitorCompanyDto;
import com.app.entity.VisitorCompany;
import com.app.response.Response;

public interface VisitorCompanyService {
	
	
	public VisitorCompany save(VisitorCompanyDto visitorCompanyDto);

	public VisitorCompany  getById(Integer id);

	public Response<?> getAll();
	
}
