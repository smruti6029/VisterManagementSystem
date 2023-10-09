package com.vms2.response;

import java.util.List;

public class PaginatedResponse<T> {
	
	
	private List<?> users;;
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;
	public List<?> getUsers() {
		return users;
	}
	public void setUsers(List<?> users) {
		this.users = users;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public PaginatedResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PaginatedResponse(List<?> users, int currentPage, int pageSize, long totalItems, int totalPages) {
		super();
		this.users = users;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.totalPages = totalPages;
	}
    
    
	
	

}
