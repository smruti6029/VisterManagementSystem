package com.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.app.emun.MeetingContext;
import com.app.emun.MeetingStatus;

@Entity
@Table(name = "meeting")
public class Meeting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "meeting_status")
	private MeetingStatus status;

	@JoinColumn(name = "employee_id")
	@ManyToOne
	private User employee;

	@JoinColumn(name = "visitor_id")
	@ManyToOne
	private Visitor visitor;

	@Enumerated(EnumType.STRING)
	@Column(name = "context")
	private MeetingContext context;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "start_datetime")
	private Date meetingStartDateTime;

	@Column(name = "end_datetime")
	private Date meetingEndDateTime;

	@Column(name = "checkin_datetime")
	private Date checkInDateTime;

	@Column(name = "checkout_datetime")
	private Date checkOutDateTime;

	@JoinColumn(name = "room_id")
	@ManyToOne
	private Room room;

	@Column(name = "room_changed")
	private Boolean roomChanged;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "is_active")
	private Boolean isActive;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MeetingStatus getStatus() {
		return status;
	}

	public void setStatus(MeetingStatus status) {
		this.status = status;
	}

	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}

	public Visitor getVisitor() {
		return visitor;
	}

	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}

	public MeetingContext getContext() {
		return context;
	}

	public void setContext(MeetingContext context) {
		this.context = context;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getMeetingStartDateTime() {
		return meetingStartDateTime;
	}

	public void setMeetingStartDateTime(Date meetingStartDateTime) {
		this.meetingStartDateTime = meetingStartDateTime;
	}

	public Date getMeetingEndDateTime() {
		return meetingEndDateTime;
	}

	public void setMeetingEndDateTime(Date meetingEndDateTime) {
		this.meetingEndDateTime = meetingEndDateTime;
	}

	public Date getCheckInDateTime() {
		return checkInDateTime;
	}

	public void setCheckInDateTime(Date checkInDateTime) {
		this.checkInDateTime = checkInDateTime;
	}

	public Date getCheckOutDateTime() {
		return checkOutDateTime;
	}

	public void setCheckOutDateTime(Date checkOutDateTime) {
		this.checkOutDateTime = checkOutDateTime;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Boolean getRoomChanged() {
		return roomChanged;
	}

	public void setRoomChanged(Boolean roomChanged) {
		this.roomChanged = roomChanged;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Meeting(Integer id, MeetingStatus status, User employee, Visitor visitor, MeetingContext context,
			String remarks, Date meetingStartDateTime, Date meetingEndDateTime, Date checkInDateTime,
			Date checkOutDateTime, Room room, Date createdAt, String createdBy, Date updatedAt, String updatedBy,
			Boolean isActive) {
		super();
		this.id = id;
		this.status = status;
		this.employee = employee;
		this.visitor = visitor;
		this.context = context;
		this.remarks = remarks;
		this.meetingStartDateTime = meetingStartDateTime;
		this.meetingEndDateTime = meetingEndDateTime;
		this.checkInDateTime = checkInDateTime;
		this.checkOutDateTime = checkOutDateTime;
		this.room = room;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
		this.isActive = isActive;
	}

	public Meeting() {
		super();
		// TODO Auto-generated constructor stub
	}

}
