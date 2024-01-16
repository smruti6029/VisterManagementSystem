package com.app.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.app.dto.DepartmentDto;
import com.app.dto.IsActiveDto;
import com.app.response.Response;

public interface DepartmentService {

	public Response<?> saveDepartment(DepartmentDto departmentDto);

	public Response<?> saveAllDepartments(List<DepartmentDto> departments);

	public Response<?> getById(Integer id);

	public Response<?> getAll();

	public Response<?> getAllByCompanyId(Integer companyId);

	public Response<?> getCountOfUsersInADepartment(Integer companyId);

	public Response<?> update(DepartmentDto departmentDto);

	public Response<?> delete(IsActiveDto isActiveDto, HttpServletRequest request);

	public Response<?> saveDepartmentByexcell(MultipartFile file, Integer companyId);

}