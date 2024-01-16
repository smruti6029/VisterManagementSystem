package com.app.Dao;

import java.util.Collection;
import java.util.List;

import com.app.dto.VisitorCompanyDto;
import com.app.entity.VisitorCompany;

public interface VisitorCompanyDao {

	public VisitorCompany save(VisitorCompany visitorCompany);

	public VisitorCompany getById(Integer id);

	List<VisitorCompany> searchCompanyByName(String companyName);

	VisitorCompany getBycompanyName(String name);

	public List<VisitorCompany> getAll();

}
