package com.vms2.service;

import com.vms2.dto.IdIsactiveDTO;
import com.vms2.dto.RoomDTO;
import com.vms2.response.Response;

public interface RoomService {

	Response<?> addRoom(RoomDTO roomDTO);

	Response<?> geteroomByid(Integer id);

	Response<?> getAllRooms();

	Response<?> deleteRoom(IdIsactiveDTO idIsactiveDTO);

	Response<?> upadateStatus(IdIsactiveDTO idIsactiveDTO);


}
