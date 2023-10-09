package com.vms2.dao;

import java.util.List;

import com.vms2.entity.State;

public interface StateDao {

	List<State> getAll();

	State getStateById(Integer id);

}
