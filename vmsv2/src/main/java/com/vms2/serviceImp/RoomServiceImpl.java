package com.vms2.serviceImp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.vms2.dao.RoomDao;
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.dto.RoomDTO;
import com.vms2.entity.Room;
import com.vms2.response.Response;
import com.vms2.service.RoomService;

@Repository
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomDao roomDAO;

	@Override
	public Response<?> addRoom(RoomDTO roomDTO) {

		Room room = RoomDTO.fromRoomDTO(roomDTO);
		room.setIsActive(true);
		room.setUpdatedAt(new Date());
		room.setCreatedAt(new Date());
		room.setIsAvailable(true);
		Room save = roomDAO.save(room);
		if (save != null) {
			return new Response<>("Success", save, HttpStatus.OK.value());
		}
		return new Response<>("Saved Fallid", null, HttpStatus.BAD_REQUEST.value());
	}

	@Override
	public Response<?> geteroomByid(Integer id) {
		Room room = roomDAO.getById(id);

		if (room != null) {
			RoomDTO fromRoom = RoomDTO.fromRoom(room);
			return new Response<>("Success", fromRoom, HttpStatus.OK.value());
		}
		return null;
	}

	@Override
	public Response<?> getAllRooms() {
		 List<Room> allrooms = roomDAO.getAll();
		 
		 List<RoomDTO> rooms=new ArrayList<>();
		if(allrooms!=null)
		{
		 for(Room r:allrooms)
		 {
			 if(r.getIsActive()==true)
			 {
				 if(r.getIsAvailable()==true)
				 {
					 RoomDTO fromRoom = RoomDTO.fromRoom(r);
					 rooms.add(fromRoom);
				 }			 
		 }
		 }
		 return new Response<>("Success",rooms,HttpStatus.OK.value());
		}
		return new Response<>("No Data Found",null,HttpStatus.NO_CONTENT.value());
	}

	@Override
	public Response<?> deleteRoom(IdIsactiveDTO idIsactiveDTO) {
		
		Room room = roomDAO.getById(idIsactiveDTO.getId());
		
		if(room!=null)
		{
			room.setIsActive(false);
			Room update = roomDAO.update(room);
			if(update!=null)
			{
				return new Response<>("Delete Successfully",room,HttpStatus.OK.value());
			}
			else
			{
				return new Response<>("Falied",null,HttpStatus.BAD_REQUEST.value());
			}
		}
		
		return new Response<>("Falied",null,HttpStatus.BAD_REQUEST.value());
	}

	@Override
	public Response<?> upadateStatus(IdIsactiveDTO idIsactiveDTO) {
	
		Room room = roomDAO.getById(idIsactiveDTO.getId());
		if(room!=null)
		{
			room.setIsAvailable(false);
			Room update = roomDAO.update(room);
			if(update!=null)
			{
				return new Response<>("Upadte Successfully",room,HttpStatus.OK.value());
			}
			else
			{
				return new Response<>("Falied",null,HttpStatus.BAD_REQUEST.value());
			}
		}
		
		return new Response<>("Room Not Present",null,HttpStatus.BAD_REQUEST.value());
	}
	

	

}
