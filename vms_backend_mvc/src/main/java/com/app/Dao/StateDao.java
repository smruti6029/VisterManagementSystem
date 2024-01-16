package com.app.Dao;

import java.util.List;

import com.app.entity.State;

public interface StateDao {
	
State getStateById(Integer id);
    
    List<State> getAllStates();
    
    void saveState(State state);
    
    void updateState(State state);
    
    void deleteState(State state);

    State getByName(String name);

}
