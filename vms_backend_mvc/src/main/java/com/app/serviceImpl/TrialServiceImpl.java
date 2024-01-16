package com.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.MeetingDao;
import com.app.Dao.MeetingRoomTrailDao;
import com.app.Dao.RoomDao;
import com.app.Dao.UserDao;
import com.app.dto.MeetingDto;
import com.app.dto.MeetingRoomTrialDto;
import com.app.dto.RoomDto;
import com.app.dto.UserTrialDto;
import com.app.entity.MeetingRoomTrial;
import com.app.response.Response;
import com.app.service.TrialService;

@Service
public class TrialServiceImpl implements TrialService {

	@Autowired
	private MeetingRoomTrailDao roomTrailDao;

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private UserDao userDao;

	@Override
	public Response<?> getRoomTrialDataByMeetingId(Integer meetingId) {
		try {
			if(meetingId == null) {
				return new Response<>("Meeting ID is required", null, HttpStatus.BAD_REQUEST.value());
			}
			List<MeetingRoomTrial> roomTrialData = roomTrailDao.getTrialDataByMeetingId(meetingId);
			List<MeetingRoomTrialDto> dtoList = new ArrayList<>();
			if (roomTrialData != null) {
				for (MeetingRoomTrial data : roomTrialData) {
					MeetingRoomTrialDto dto = new MeetingRoomTrialDto();
					dto.setId(data.getId());
					dto.setMeeting(MeetingDto.convertToDTO(meetingDao.getById(data.getMeetingId())));
					dto.setRoom(RoomDto.toRoomDto(roomDao.getById(data.getRoomId())));
					dto.setCreatedBy(UserTrialDto.userToUserTrial(userDao.getuserByid(data.getCreatedBy())));
					dto.setText(data.getText());
					dto.setCreatedAt(data.getCreatedAt());
					dtoList.add(dto);
				}
			}
			return new Response<>("success", dtoList, HttpStatus.OK.value());

		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Something went wrong", null, HttpStatus.BAD_REQUEST.value());
		}
	}

}
