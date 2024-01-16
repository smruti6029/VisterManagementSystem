package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Dao.StateDao;
import com.app.dto.StateDto;
import com.app.entity.State;

@Service
public class StateService {
	
	@Autowired
	private StateDao stateDao;
	
  public List<StateDto> getAllstate()
  {
	  List<State> allstate = stateDao.getAllStates();
	  
	  List<StateDto> states=new ArrayList<>();
	  
	  allstate.forEach(x -> {
		  StateDto statedto = StateDto.convertEntityToDTO(x);
		  states.add(statedto);
	  });
	  return states;
	  
  }
}
