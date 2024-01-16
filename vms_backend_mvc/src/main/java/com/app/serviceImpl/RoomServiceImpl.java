
package com.app.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.MeetingDao;
import com.app.Dao.RoomDao;
import com.app.dto.IsActiveDto;
import com.app.dto.MeetingDto;
import com.app.dto.RoomDto;
import com.app.dto.RoomWithMeetingForDashoboardDTO;
import com.app.entity.Meeting;
import com.app.entity.Room;
import com.app.response.Response;
import com.app.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private MeetingDao meetingDao;

	@Override
	public Response<?> save(RoomDto room) {

		Room roomExist = roomDao.getRoomByName(RoomDto.toRoom(room));
		if (roomExist != null) {
			return new Response<>("Room already exist", null, HttpStatus.BAD_REQUEST.value());
		}
		room.getRoomName();
		room.getRoomName();
		room.setIsAvailable(true);
		room.setCreatedAt(new Date());
		room.setUpdatedAt(new Date());
		room.setIsActive(true);
		this.roomDao.save(RoomDto.toRoom(room));
		return new Response<>("success", RoomDto.toRoom(room), HttpStatus.OK.value());
	}

	@Override
	public Response<?> getById(Integer id) {
		return new Response<>("success", RoomDto.toRoomDto(this.roomDao.getById(id)), HttpStatus.OK.value());
	}

	@Override
	public Response<?> getAll(Integer companyId) {

		List<Room> allRooms = this.roomDao.getAll(companyId);

		return new Response<>("success", allRooms, HttpStatus.OK.value());
	}

	@Override
	public Response<?> getAvailableRooms(Integer companyId) {
		List<RoomDto> list = this.roomDao.getAllactive(companyId).stream().map(RoomDto::toRoomDto)
				.filter(roomDto -> roomDto.getIsAvailable()).collect(Collectors.toList());
		System.out.println(list.size() + "room size");
		return new Response<>("success", list, HttpStatus.OK.value());
	}

	@Override
	public Room allocateRoomsRandomly(List<RoomDto> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		Room selectedRoom = null;
		Random random = new Random();

		while (selectedRoom == null || !listContainsRoomWithId(list, selectedRoom.getId())) {

			int randomIndex = random.nextInt(list.size());
			RoomDto randomlySelectedRoomDto = list.get(randomIndex);

			selectedRoom = RoomDto.toRoom(randomlySelectedRoomDto);
		}

		return selectedRoom;
	}

	private boolean listContainsRoomWithId(List<RoomDto> list, Integer roomId) {
		return list.stream().anyMatch(roomDto -> roomDto.getId() != null && roomDto.getId().equals(roomId));
	}

	@Override
	public Response<?> update(Room room) {
		Room roomExist = roomDao.getById(room.getId());
		if (roomExist != null) {
			Room roomBySameName = roomDao.getRoomByName(room);
			if (roomBySameName != null && !roomBySameName.getId().equals(room.getId())) {
				return new Response<>("Room Name already exists", null, HttpStatus.BAD_REQUEST.value());
			}
			room.setUpdatedAt(new Date());
			roomDao.update(room);
			return new Response<>("success", null, HttpStatus.OK.value());
		} else {
			return new Response<>("Room not found", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> getAll2(Integer companyId, Integer buildingId) {

		List<Room> allRooms = this.roomDao.getAll2(companyId, buildingId);
		System.out.println("room size = " + allRooms.size());

		return new Response<>("success", allRooms, HttpStatus.OK.value());
	}

	public Response<?> roomAvailability(IsActiveDto isActive) {
		return new Response<>("success", this.roomDao.roomAvailability(isActive), HttpStatus.OK.value());
	}

	@Override
	public Response<?> delete(IsActiveDto isActiveDto) {

		Room byId = roomDao.getById(isActiveDto.getId());
		if (!isActiveDto.getIsActive() && !byId.getIsAvailable()) {
			return new Response<>("Room is Occupied in another meeting ", null, 400);
		}

		Room delete = roomDao.delete(isActiveDto);
		return new Response<>("Success", roomDao.delete(isActiveDto), 200);
	}

	@Override
	public Response<?> getRoomWithMeetingsfordashboard(Integer companyId) {

		List<Room> allrooms = roomDao.getAllactive(companyId);
		System.out.println("All rooms size, " + allrooms.size());
		if (allrooms.isEmpty()) {
			return new Response<>("Room Not Found For Your Company", null, HttpStatus.NO_CONTENT.value());
		}

		Map<String, RoomWithMeetingForDashoboardDTO> allroomsWithMeetings = new HashMap<>();

		for (Room room : allrooms) {

			List<Meeting> meetingByroomFortodayDate = meetingDao.getMeetingByroomFortodayDate(room.getId());

			if (meetingByroomFortodayDate.isEmpty()) {

				allroomsWithMeetings.put(room.getRoomName(),
						RoomWithMeetingForDashoboardDTO.convertTODashboad(null, room));

			} else {

				List<MeetingDto> meetings = meetingByroomFortodayDate.stream().map(x -> {

					try {

						MeetingDto convertToDTO = MeetingDto.convertToDTO(x);

						return convertToDTO;

					} catch (Exception e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}

					return null;
				}).collect(Collectors.toList());

				allroomsWithMeetings.put(room.getRoomName(),
						RoomWithMeetingForDashoboardDTO.convertTODashboad(meetings, room));
			}

		}

		return new Response<>("Success", allroomsWithMeetings, HttpStatus.OK.value());
	}

	@Override
	public Response<?> getRoomWithMeetingsfordashboard2(Integer companyId, Integer buildingId) {

		List<Room> allrooms = roomDao.getAllactive2(companyId, buildingId);

		System.out.println("All rooms size, " + allrooms.size());
		if (allrooms.isEmpty()) {
			return new Response<>("Room Not Found For Your Company", null, HttpStatus.NO_CONTENT.value());
		}

		Map<String, RoomWithMeetingForDashoboardDTO> allroomsWithMeetings = new HashMap<>();

		for (Room room : allrooms) {

			List<Meeting> meetingByroomFortodayDate = meetingDao.getMeetingByroomFortodayDate(room.getId());

			if (meetingByroomFortodayDate.isEmpty()) {

				allroomsWithMeetings.put(room.getRoomName(),
						RoomWithMeetingForDashoboardDTO.convertTODashboad(null, room));

			} else {

				List<MeetingDto> meetings = meetingByroomFortodayDate.stream().map(x -> {

					try {

						MeetingDto convertToDTO = MeetingDto.convertToDTO(x);

						return convertToDTO;

					} catch (Exception e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}

					return null;
				}).collect(Collectors.toList());

				allroomsWithMeetings.put(room.getRoomName(),
						RoomWithMeetingForDashoboardDTO.convertTODashboad(meetings, room));
			}

		}

		return new Response<>("Success", allroomsWithMeetings, HttpStatus.OK.value());
	}

	@Override
	public Response<?> deleteRoomByID(Integer roomId) {

		Room byId = roomDao.getById(roomId);

		if (byId != null) {

			roomDao.delete(roomId);

			return new Response<>("Success", "Room delete successfully ", HttpStatus.OK.value());

		}
		return new Response<>("Room Not deleted ", "Room Id does not exist ", HttpStatus.BAD_REQUEST.value());

	}

}