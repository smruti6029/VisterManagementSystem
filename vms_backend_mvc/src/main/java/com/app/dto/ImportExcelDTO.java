package com.app.dto;

import java.util.List;

public class ImportExcelDTO {
	private Integer totalElement;
	private Integer successfullyAdded;
	private Integer failedData;
	private Integer duplicateData;
	private String failedDataLink;
	private String duplicateDataLink;
	
	private List<InvalidUserDto> duplicateElement;
	
	private List<InvalidUserDto> failedElement;

	public Integer getTotalElement() {
		return totalElement;
	}

	public void setTotalElement(Integer totalElement) {
		this.totalElement = totalElement;
	}

	public Integer getSuccessfullyAdded() {
		return successfullyAdded;
	}

	public void setSuccessfullyAdded(Integer successfullyAdded) {
		this.successfullyAdded = successfullyAdded;
	}

	public Integer getDuplicateData() {
		return duplicateData;
	}

	public void setDuplicateData(Integer duplicateData) {
		this.duplicateData = duplicateData;
	}

	public Integer getFailedData() {
		return failedData;
	}

	public void setFailedData(Integer failedData) {
		this.failedData = failedData;
	}

	public String getFailedDataLink() {
		return failedDataLink;
	}

	public void setFailedDataLink(String failedDataLink) {
		this.failedDataLink = failedDataLink;
	}

	public String getDuplicateDataLink() {
		return duplicateDataLink;
	}

	public void setDuplicateDataLink(String duplicateDataLink) {
		this.duplicateDataLink = duplicateDataLink;
	}

	public List<InvalidUserDto> getDuplicateElement() {
		return duplicateElement;
	}

	public void setDuplicateElement(List<InvalidUserDto> duplicateElement) {
		this.duplicateElement = duplicateElement;
	}

	public List<InvalidUserDto> getFailedElement() {
		return failedElement;
	}

	public void setFailedElement(List<InvalidUserDto> failedElement) {
		this.failedElement = failedElement;
	}

	public ImportExcelDTO(Integer totalElement, Integer successfullyAdded, Integer failedData, Integer duplicateData,
			String failedDataLink, String duplicateDataLink) {
		this.totalElement = totalElement;
		this.successfullyAdded = successfullyAdded;
		this.failedData = failedData;
		this.duplicateData = duplicateData;
		this.failedDataLink = failedDataLink;
		this.duplicateDataLink = duplicateDataLink;
	}

	public ImportExcelDTO() {
		
	}

}
