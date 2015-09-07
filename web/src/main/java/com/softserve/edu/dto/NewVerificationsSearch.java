package com.softserve.edu.dto;

public class NewVerificationsSearch {

	private String idText;
	private String formattedDate;
	private String lastNameText;
	private String firstNameText;
	private String streetText;
	private String region;
	private String district;
	private String locality;
	private String status;
	private String employee;

	public NewVerificationsSearch() {
	}

	public NewVerificationsSearch(String idText, String formattedDate, String lastNameText, String firstNameText, String streetText, String region, String district, String locality, String status, String employee) {

		this.idText = idText;
		this.formattedDate = formattedDate;
		this.lastNameText = lastNameText;
		this.firstNameText = firstNameText;
		this.streetText = streetText;
		this.region = region;
		this.district = district;
		this.locality = locality;
		this.status = status;
		this.employee = employee;
	}

	public String getFirstNameText() {
		return firstNameText;
	}

	public void setFirstNameText(String firstNameText) {
		this.firstNameText = firstNameText;
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

	public String getLastNameText() {
		return lastNameText;
	}

	public void setLastNameText(String lastNameText) {
		this.lastNameText = lastNameText;
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
}
