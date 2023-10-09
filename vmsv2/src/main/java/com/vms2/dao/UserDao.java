package com.vms2.dao;

import java.util.List;

import com.vms2.entity.User;

public interface UserDao {

	Integer saveUser(User user);

	User getUserbyPhone(String phone);

	List<User> getallUser(Integer company_id);

	User getuserByid(Integer id);

	Integer updateUser(User user);

	List<User> getallUsers();
	
	

}
