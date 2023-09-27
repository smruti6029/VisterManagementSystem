package com.vms2.dao;

import java.util.List;

import com.vms2.entity.Role;

public interface RoleDao {

	Integer addRole(Role role);

	Role getRole(String name);

	List<Role> getallRole();

	Role getroleByid(Integer id);

	Integer deleteRole(Role role);

}
