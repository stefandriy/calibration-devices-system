package com.softserve.edu.dto;

public class NewVerificationsSearch {

	private String idText;
	private String formattedDate;
	private String client_full_name;
	private String streetText;
	private String region;
	private String district;
	private String locality;
	private String status;
	private String employee;

	public NewVerificationsSearch() {
	}

	public NewVerificationsSearch(String idText, String formattedDate, String client_full_name, String streetText, String region, String district, String locality, String status, String employee) {

		this.idText = idText;
		this.formattedDate = formattedDate;
		this.client_full_name = client_full_name;
		this.streetText = streetText;
		this.region = region;
		this.district = district;
		this.locality = locality;
		this.status = status;
		this.employee = employee;
	}



	public String getIdText() {
		return idText;
	}

	public void setIdText(String idText) {
		this.idText = idText;
	}

	public String getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}


	public String getStreetText() {
		return streetText;
	}

	public void setStreetText(String streetText) {
		this.streetText = streetText;
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

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}


	public String getClient_full_name() {
		return client_full_name;
	}

	public void setClient_full_name(String client_full_name) {
		this.client_full_name = client_full_name;
	}
}
