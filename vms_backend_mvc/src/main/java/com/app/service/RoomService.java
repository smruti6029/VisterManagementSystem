package com.app.service;

import java.util.List;

import com.app.dto.IsActiveDto;
import com.app.dto.RoomDto;
import com.app.entity.Room;
import com.app.response.Response;

public interface RoomService {
	public Response<?> save(RoomDto room);

	public Response<?> getById(Integer id);

	public Response<?> getAll(Integer companyId);
	
	public Response<?> getAvailableRooms(Integer companyId);

	public Response<?> update(Room room);

	public Response<?> roomAvailability(IsActiveDto isActive);

	public Response<?> delete(IsActiveDto isActiveDto);

	public Response<?> getRoomWithMeetingsfordashboard(Integer companyId);

	public Response<?> deleteRoomByID(Integer roomId);

	Room allocateRoomsRandomly(List<RoomDto> list);

	Response<?> getAll2(Integer companyId, Integer buildingId);

	public Response<?> getRoomWithMeetingsfordashboard2(Integer companyId, Integer buildingId);
}
