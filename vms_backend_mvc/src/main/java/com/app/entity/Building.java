package com.app.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "building")
public class Building {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Id
	private Integer buildingId;

	@Column(name = "building_name")
	private String name;

	@Column(name = "address", nullable = false)
	private String address;

	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;

	@OneToOne
	@JoinColumn(name = "city_id")
	private City city;

	@Column(name = "created_on")
	private Date createdOn = new Timestamp(System.currentTimeMillis());

	@Column(name = "modified_on", nullable = true)
	private Date modifiedOn;

	@Column(name = "created_by", nullable = true)
	private String createdBy;

	@Column(name = "updated_by", nullable = true)
	private String updatedBy;

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	
	

}
