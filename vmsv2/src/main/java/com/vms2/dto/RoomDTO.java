package com.vms2.dto;


import java.util.Date;

import com.vms2.entity.Company;
import com.vms2.entity.Room;

public class RoomDTO {

	private Integer id;

	private String roomName;

	private Integer capacity;

	private Boolean isAvailable;

	private CompanyDTO companyId;

	private Date createdAt;

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

	public CompanyDTO getCompanyId() {
		return companyId;
	}

	public void setCompanyId(CompanyDTO companyId) {
		this.companyId = companyId;
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

	public RoomDTO(Integer id, String roomName, Integer capacity, Boolean isAvailable, CompanyDTO companyId,
			Date createdAt, Date updatedAt, Boolean isActive) {
		super();
		this.id = id;
		this.roomName = roomName;
		this.capacity = capacity;
		this.isAvailable = isAvailable;
		this.companyId = companyId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.isActive = isActive;
	}

	public RoomDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public static Room fromRoomDTO(RoomDTO roomDTO) {
        Room room = new Room();
        room.setId(roomDTO.getId());
        room.setRoomName(roomDTO.getRoomName());
        room.setCapacity(roomDTO.getCapacity());
        room.setIsAvailable(roomDTO.getIsAvailable());
        room.setCreatedAt(roomDTO.getCreatedAt());
        room.setUpdatedAt(roomDTO.getUpdatedAt());
        room.setIsActive(roomDTO.getIsActive());
        Company company = new Company();
        company.setId(roomDTO.getCompanyId().getId());
        room.setCompany(company); 
        return room;
        
      
    }
	
	public static RoomDTO fromRoom(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        if(room.getId()!=null)
        {
        roomDTO.setId(room.getId());
        roomDTO.setRoomName(room.getRoomName());
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setIsAvailable(room.getIsAvailable());
        if (room.getCompany() != null) {
            roomDTO.setCompanyId(companyEntityToDTO(room.getCompany()));
        }
        
        roomDTO.setCreatedAt(room.getCreatedAt());
        roomDTO.setUpdatedAt(room.getUpdatedAt());
        roomDTO.setIsActive(room.getIsActive());
        }
        return roomDTO;
    }

	
	private static CompanyDTO companyEntityToDTO(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setId(company.getId());
		companyDTO.setName(company.getName());
		companyDTO.setAddress(company.getAddress());
		companyDTO.setLogo(company.getLogo());
		

		return companyDTO;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

