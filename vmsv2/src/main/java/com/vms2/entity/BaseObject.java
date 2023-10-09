package com.vms2.entity;


import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;



@MappedSuperclass
public class BaseObject {
	

	    @JsonIgnore
	    @Column(name = "is_active")
	    private boolean active = true;
	
	    @JsonIgnore
	    @Column(name = "created_on")
	    private Date createdOn = new Timestamp(System.currentTimeMillis());

	    @JsonIgnore
	    @Column(name = "modified_on",nullable = true)
	    private Date  modifiedOn;
	    
	    @JsonIgnore
	    @Column(name = "created_by",nullable = true)	   
	    private String createdBy;
	    
	    @JsonIgnore
	    @Column(name = "updated_by",nullable = true)
	    private String updatedBy;
	     

		public boolean getisActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

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

	    
	    


	
}
