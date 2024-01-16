package com.app.dto;

import java.util.List;

import com.app.entity.Meeting;
import com.app.entity.Visitor;

public class VisitorMeetingDetailsDto {

	
	    private Visitor visitor;
	
        private List<Meeting> meetings;
   
	  

	    public Visitor getVisitor() {
	        return visitor;
	    }

	    public void setVisitor(Visitor visitor) {
	        this.visitor = visitor;
	    }

		public List<Meeting> getMeetings() {
			return meetings;
		}

		public void setMeetings(List<Meeting> meetings) {
			this.meetings = meetings;
		}


	
	
	    

}
