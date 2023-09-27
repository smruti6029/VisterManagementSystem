package com.vms2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vms2.daoImp.StateDao;
import com.vms2.dto.StateDTO;
import com.vms2.entity.State;

@Service
public class StateService {
	
	@Autowired
	private StateDao stateDao;
	
  public List<StateDTO> getAllstate()
  {
	  List<State> allstate = stateDao.getAll();
	  
	  List<StateDTO> states=new ArrayList<>();
	  
	  allstate.forEach(x -> {
		  StateDTO statedto = StateDTO.convertEntityToDTO(x);
		  states.add(statedto);
	  });
	  return states;
	  
  }
}
