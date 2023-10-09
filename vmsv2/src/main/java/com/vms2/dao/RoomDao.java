package com.vms2.dao;

import java.util.List;

import com.vms2.dto.IdIsactiveDTO;
import com.vms2.entity.Room;

public interface RoomDao {
	
	public Room save(Room room);
	
	public Room getById(Integer id);
	
	public List<Room> getAll();
	
	public Room getByStatus(IdIsactiveDTO idIsactiveDTO);
	
	public Room update(Room room);
	
	public Room delete(IdIsactiveDTO idIsactiveDTO);


}
