package com.softserve.edu.dto;

public class NewVerificationsFilterSearch {
	
	private String id;
	private String date;
	private String endDate;
	private String client_last_name;
	private String street;
	private String region;
	private String district;
	private String locality;
	private String status;
	private String employee_last_name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getClient_last_name() {
		return client_last_name;
	}
	public void setClient_last_name(String client_last_name) {
		this.client_last_name = client_last_name;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmployee_last_name() {
		return employee_last_name;
	}
	public void setEmployee_last_name(String employee_last_name) {
		this.employee_last_name = employee_last_name;
	}


	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
