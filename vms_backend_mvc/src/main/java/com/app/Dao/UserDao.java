package com.app.Dao;

import java.util.List;

import com.app.dto.CompanyDepartmentResponseDTO;
import com.app.dto.IsActiveDto;
import com.app.entity.User;

public interface UserDao {

	Integer saveUser(User user);

	User getUserbyPhone(String phone);

	User getUserByEmpCode(String empCode);

	List<User> getallUser(Integer company_id);

	User getuserByid(Integer id);

	Integer updateUser(User user);

	public List<CompanyDepartmentResponseDTO> getUserCountByDepartmentInCompany(Integer companyId);

	List<User> getallUsers();

	User updateUserPresent(IsActiveDto activeDto);

	void absentAll();

	User getUserByEmail(String email);

	User getUserByGoveID(String govtId);


	Long countActiveUsers(Integer companyId);

	void presentAll();

	List<User> getallUser2(Integer company_id, Integer buildingID);

}
