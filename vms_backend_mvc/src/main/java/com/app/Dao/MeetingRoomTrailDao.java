package com.app.Dao;

import java.util.List;

import com.app.entity.MeetingRoomTrial;

public interface MeetingRoomTrailDao {

	public MeetingRoomTrial save(MeetingRoomTrial meetingRoomTrial);

	List<MeetingRoomTrial> getTrialDataByMeetingId(Integer meetingId);
}
