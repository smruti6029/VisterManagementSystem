package com.app.Dao;

import java.util.List;

import com.app.entity.Role;


public interface RoleDao {

	Integer addRole(Role role);

	Role getRoladdRolee(String name);

	List<Role> getallRole();

	Role getroleByid(Integer id);

	Integer deleteRole(Role role);

	Role getRole(String name);


}
