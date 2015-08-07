package com.softserve.edu.dto;

public class NewVerificationsSearch {

	private String idText;
	private String formattedDate;
	private String lastNameText;
	private String streetText;
	private String status;
	private String employee;
	
	public NewVerificationsSearch() {};
	
	public NewVerificationsSearch(String idText, String formattedDate, String lastNameText, String streetText, String status, String employee) {

		this.idText = idText;
		this.formattedDate = formattedDate;
		this.lastNameText = lastNameText;
		this.streetText = streetText;
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
