package com.softserve.edu.dto.admin;

public class OrganizationEditDTO {
	private String name;
	private String email;
	private String phone;
	private String[] types;
	private Integer employeesCapacity;
	private Integer maxProcessTime;

	private String region;
	private String locality;
	private String district;
	private String street;
	private String building;
	private String flat;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public Integer getEmployeesCapacity() {
		return employeesCapacity;
	}

	public void setEmployeesCapacity(Integer employeesCapacity) {
		this.employeesCapacity = employeesCapacity;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getFlat() {
		return flat;
	}

	public void setFlat(String flat) {
		this.flat = flat;
	}

	public Integer getMaxProcessTime() {
		return maxProcessTime;
	}

	public void setMaxProcessTime(Integer maxProcessTime) {
		this.maxProcessTime = maxProcessTime;
	}
	
}
