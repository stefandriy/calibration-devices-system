package com.softserve.edu.dto.provider;

import com.softserve.edu.entity.user.ProviderEmployee;

public class VerificationSearchDTO {
	int pageNumber;
	int itemsPerPage;
	String searchById;
	String searchByDate;
	String searchByLastName;
	String searchByStreet;
	ProviderEmployee providerEmployee;
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	public String getSearchById() {
		return searchById;
	}
	public void setSearchById(String searchById) {
		this.searchById = searchById;
	}
	public String getSearchByDate() {
		return searchByDate;
	}
	public void setSearchByDate(String searchByDate) {
		this.searchByDate = searchByDate;
	}
	public String getSearchByLastName() {
		return searchByLastName;
	}
	public void setSearchByLastName(String searchByLastName) {
		this.searchByLastName = searchByLastName;
	}
	public String getSearchByStreet() {
		return searchByStreet;
	}
	public void setSearchByStreet(String searchByStreet) {
		this.searchByStreet = searchByStreet;
	}

	public ProviderEmployee getProviderEmployee() {
		return providerEmployee;
	}

	public void setProviderEmployee(ProviderEmployee providerEmployee) {
		this.providerEmployee = providerEmployee;
	}
}
