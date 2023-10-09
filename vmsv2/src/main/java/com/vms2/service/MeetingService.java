package com.vms2.service;

import com.vms2.dto.MeetingDTO;
import com.vms2.dto.VisitormeetingDTO;
import com.vms2.entity.Visitor;
import com.vms2.response.Response;

public interface MeetingService {

	Response<?> addMeeting(VisitormeetingDTO visitorDto);

	Response<?> getmeetingWithstatusByuser(Integer id);

	Response<?> getallmeetingWithstatus();

	Response<?> updatemeetingStatus(MeetingDTO meetingDTO);

	Response<?> addMeetingByuser(VisitormeetingDTO visitorDto);

	Response<?> updateCheckoutStatus(Visitor visitor);

}
