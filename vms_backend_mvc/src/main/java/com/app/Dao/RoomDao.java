package com.app.Dao;



import java.util.List;

import com.app.dto.IsActiveDto;
import com.app.entity.Room;



public interface RoomDao {
	
    public Room save(Room room);
	
	public Room getById(Integer id);
	
	public List<Room> getAll(Integer companyId);
	
	public Room update(Room room);
	
	public void delete(Integer roomId);
	
	public Room roomAvailability(IsActiveDto isActive);
	
	public Room delete(IsActiveDto isActiveDto);

	void availableAllroom();

	List<Room> getAllactive(Integer companyId);

	Room getRoomByName(Room room);

	List<Room> getAll2(Integer companyId, Integer buildingId);

	public List<Room> getAllactive2(Integer companyId, Integer buildingId);

	
}
