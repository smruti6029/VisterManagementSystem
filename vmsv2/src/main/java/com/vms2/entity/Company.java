package com.vms2.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.vms2.dto.CompanyDTO;

@Entity
@Table(name = "company")
public class Company {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "create_at")
	private Date createAt;
	
	
	@Column(name = "is_active")
	private Boolean isActive;
	
//	@OneToMany(mappedBy = "company_id")
//	private List<Location> location;
	
	@OneToMany(mappedBy = "company")
	private List<User> user;

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

//	public List<Location> getLocation() {
//		return location;
//	}
//
//	public void setLocation(List<Location> location) {
//		this.location = location;
//	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Company() {
		super();
		// TODO Auto-generated constructor stub
	}

	  public static Company convertDTOToEntity(CompanyDTO companyDTO) 
	  {
	        Company company = new Company();
	        company.setId(companyDTO.getId());
	        company.setName(companyDTO.getName());
	        company.setCreateAt(companyDTO.getCreateAt());
	        company.setIsActive(companyDTO.getIsActive());
	        return company;
	    }
	 

}
