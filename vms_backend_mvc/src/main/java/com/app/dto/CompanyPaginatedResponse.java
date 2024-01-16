package com.app.dto;


import java.util.List;

public class CompanyPaginatedResponse {

	private int pageSize;
	
	private Long totalElements;


	private int totalPages;

	
	private List<CompanyDTO> companies;
	
	public List<CompanyDTO> getCompanies() {
		return companies;
	}

	public void setCompanies(List<CompanyDTO> companies) {
		this.companies = companies;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public CompanyPaginatedResponse(int pageSize, Long totalElements, int totalPages, List<CompanyDTO> companies) {
		super();
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.companies = companies;
	}

	

}
