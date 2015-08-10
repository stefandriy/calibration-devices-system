package com.softserve.edu.dto.admin;

public class OrganizationDTO {
	private String name;
	private String email;
	private String phone;
	private String[] types;
	private Integer employeesCapacity;
	private Integer maxProcessTime;
	
	private String username;
	private String password;
	private String rePassword;

	private String region;
	private String locality;
	private String district;
	private String street;
	private String building;
	private String flat;

	public OrganizationDTO() {
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
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
