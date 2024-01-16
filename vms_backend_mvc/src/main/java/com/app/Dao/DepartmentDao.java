package com.app.Dao;

import java.util.List;

import com.app.dto.CompanyDTO;
import com.app.dto.CompanyDepartmentResponseDTO;
import com.app.dto.IsActiveDto;
import com.app.entity.Department;

public interface DepartmentDao {

	public Department save(Department department);

	public Department getById(Integer id);

	public List<Department> getAll();

	public List<CompanyDepartmentResponseDTO> getDepartmentsWithUserCount(Integer companyId);

	public List<Department> getAllByCompanyId(Integer companyId);

	public Department update(Department department);

	public Department delete(IsActiveDto isActiveDto);

	public Department getByName(String upperCase);

	public Department getByCompanyIdAndDepartmentName(Integer companyId, String companyName);

}
