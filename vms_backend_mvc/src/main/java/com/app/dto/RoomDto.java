package com.app.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.app.entity.Company;
import com.app.entity.Room;
import com.fasterxml.jackson.annotation.JsonFormat;

public class RoomDto {

	private Integer id;

	@NotBlank(message = "Name is required")
	@NotNull(message = "Name is required")
	private String roomName;

	@NotNull(message = "capacity is required")
	private Integer capacity;

	private Boolean isAvailable;

	@NotNull(message = "company is required")
	private Company company;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedAt;

	private Boolean isActive;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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

	public RoomDto(Integer id, String roomName, Integer capacity, Boolean isAvailable, Company company, Date createdAt,
			Date updatedAt, Boolean isActive) {
		super();
		this.id = id;
		this.roomName = roomName;
		this.capacity = capacity;
		this.isAvailable = isAvailable;
		this.company = company;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.isActive = isActive;
	}

	public RoomDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Room toRoom(RoomDto roomDto) {
		Room room = new Room();
		if(roomDto == null) {
			return null;
		}
		room.setId(roomDto.getId());
		room.setRoomName(roomDto.getRoomName().toUpperCase());
		room.setCapacity(roomDto.getCapacity());
		room.setIsAvailable(roomDto.getIsAvailable());
		room.setCompany(roomDto.getCompany());
		room.setCreatedAt(roomDto.getCreatedAt());
		room.setUpdatedAt(roomDto.getUpdatedAt());
		room.setIsActive(roomDto.getIsActive());
		return room;
	}

	public static RoomDto toRoomDto(Room room) {
		RoomDto roomDto = new RoomDto();
		if (room == null) {
			return null;
		}
		roomDto.setId(room.getId());
		roomDto.setRoomName(room.getRoomName());
		roomDto.setCapacity(room.getCapacity());
		roomDto.setIsAvailable(room.getIsAvailable());
		roomDto.setCompany(room.getCompany());
		roomDto.setCreatedAt(room.getCreatedAt());
		roomDto.setUpdatedAt(room.getUpdatedAt());
		roomDto.setIsActive(room.getIsActive());
		return roomDto;
	}
}
