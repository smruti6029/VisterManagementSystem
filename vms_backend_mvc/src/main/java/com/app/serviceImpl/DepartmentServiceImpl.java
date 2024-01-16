package com.app.serviceImpl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.Dao.DepartmentDao;
import com.app.Dao.UserDao;
import com.app.dto.CompanyDTO;
import com.app.dto.CompanyDepartmentResponseDTO;
import com.app.dto.DepartmentDto;
import com.app.dto.IsActiveDto;
import com.app.entity.Department;
import com.app.entity.User;
import com.app.response.Response;
import com.app.security.JwtHelper;
import com.app.service.CompanyService;
import com.app.service.DepartmentService;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ExcelImportService excelImportService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private JwtHelper jwtHelper;

	@Override
	public Response<?> saveDepartment(DepartmentDto departmentDto) {

		Department departmentExist = departmentDao.getByCompanyIdAndDepartmentName(departmentDto.getCompany().getId(),
				departmentDto.getName());

		if (departmentExist == null) {
			String name = departmentDto.getName().trim();
			departmentDto.setName(name.toUpperCase());
			Department departmentEntity = DepartmentDto.toDepartmentEntity(departmentDto);
			departmentEntity.setCreatedOn(new Date());
			departmentEntity.setUpdatedOn(new Date());
			departmentDao.save(departmentEntity);

			return new Response<>("success", null, HttpStatus.OK.value());
		} else {
			return new Response<>("Department already exists", null, HttpStatus.BAD_REQUEST.value());
		}

//		List<Department> getAllDepartmentsByCompanyId =departmentDao.getAllByCompanyId(departmentDto.getId());
//		
//		
//		if(isDepartmentNameUnique(departmentDto.getName() ,getAllDepartmentsByCompanyId )) {
//			
//			
//			 Department department = DepartmentDto.toDepartmentEntity(departmentDto);
//			 
//		        Department save = departmentDao.save(department);
//
//		        if (save != null) {
//		            return new Response<>("Department saved successfully", save, HttpStatus.OK.value());
//		        } else {
//		            return new Response<>("Department not saved", null, HttpStatus.BAD_REQUEST.value());
//		        }
//		    } else {
//		        return new Response<>("Department with the same name already exists for the company", null, HttpStatus.BAD_REQUEST.value());
//		    }
//		
	}

	private boolean isDepartmentNameUnique(String departmentName, List<Department> getAllDepartmentsByCompanyId) {
		for (Department existingDepartment : getAllDepartmentsByCompanyId) {
			if (existingDepartment.getName().replaceAll("\\s", "").equalsIgnoreCase(departmentName)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Response<?> saveAllDepartments(List<DepartmentDto> departments) {

		Integer companyId = departments.get(0).getCompany().getId();

		if (this.companyService.getCompanyById(companyId) == null) {

			return new Response<>("Company does not exists", null, HttpStatus.BAD_REQUEST.value());
		}
		for (DepartmentDto department : departments) {
			this.departmentDao.save(DepartmentDto.toDepartmentEntity(department));
		}

		return new Response<>("success", departments, HttpStatus.OK.value());
	}

	@Override
	public Response<?> getById(Integer id) {
		Department department = departmentDao.getById(id);

		if (department != null) {
			// Initialize the users collection before returning the department
			department.initializeUsers();

			return new Response<>("Department found", department, HttpStatus.OK.value());
		} else {
			return new Response<>("Department not found", null, HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public Response<?> getAll() {
		List<DepartmentDto> departments = departmentDao.getAll().stream().map(DepartmentDto::toDepartmentDto)
				.collect(Collectors.toList());

		if (departments != null && !departments.isEmpty()) {

			return new Response<>("All Departments retrieved", departments, HttpStatus.OK.value());
		} else {
			return new Response<>("No Departments found", null, HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public Response<?> getAllByCompanyId(Integer companyId) {
		List<DepartmentDto> departments = departmentDao.getAllByCompanyId(companyId).stream()
				.map(DepartmentDto::toDepartmentDto).collect(Collectors.toList());

		if (departments != null && !departments.isEmpty()) {
			return new Response<>("Departments by Company ID retrieved", departments, HttpStatus.OK.value());
		} else {
			return new Response<>("No Departments found for the given Company ID", null,
					HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> getCountOfUsersInADepartment(Integer companyId) {
		List<CompanyDepartmentResponseDTO> list = this.userDao.getUserCountByDepartmentInCompany(companyId);
		return new Response<>("success", list, HttpStatus.OK.value());
	}

	@Override
	public Response<?> update(DepartmentDto departmentDto) {

		Department department = departmentDao.getById(departmentDto.getId());
		if (department != null) {
			Department departmentExist = departmentDao
					.getByCompanyIdAndDepartmentName(departmentDto.getCompany().getId(), departmentDto.getName());
			if (departmentExist != null && !departmentExist.getId().equals(departmentDto.getId())) {
				return new Response<>("Department Name already exists", null, HttpStatus.BAD_REQUEST.value());
			}
		}
		departmentDto.getName().trim();
		department.setName(departmentDto.getName().toUpperCase());
		// department.setCompany(CompanyDTO.toCompany(departmentDto.getCompany()));
		department.setUpdatedOn(new Date());
		Department updatedDepartment = departmentDao.update(department);

		if (updatedDepartment != null) {
			return new Response<>("Department updated successfully", DepartmentDto.toDepartmentDto(updatedDepartment),
					HttpStatus.OK.value());
		} else {
			return new Response<>("Unable to update Department", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> delete(IsActiveDto isActiveDto, HttpServletRequest request) {

		try {
			String requestTokenHeader = request.getHeader("Authorization");
			String phone = jwtHelper.getUsernameFromToken(requestTokenHeader.substring(7));
			User user = this.userDao.getUserbyPhone(phone);

			if (user == null || !user.getRole().getName().equals("ADMIN")) {
				return new Response<>("Not Authorized", null, HttpStatus.BAD_REQUEST.value());
			}

			Department deletedDepartment = departmentDao.delete(isActiveDto);

			if (deletedDepartment != null) {
				return new Response<>("Department deleted successfully", null, HttpStatus.OK.value());
			} else {
				return new Response<>("Unable to delete Department", null, HttpStatus.BAD_REQUEST.value());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Internal Server Error", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}

	@Override
	public Response<?> saveDepartmentByexcell(MultipartFile file, Integer companyId) {

		try {
			Response<?> convertexcelDepatmentToList = excelImportService
					.convertexcelDepatmentToList(file.getInputStream(), companyId);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
