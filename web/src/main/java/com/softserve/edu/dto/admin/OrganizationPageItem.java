package com.softserve.edu.dto.admin;

import java.util.Set;

public class OrganizationPageItem {
	private Long id;
	private String name;
	private String email;
	private String phone;
	private Set<String> types;

	public OrganizationPageItem() {
	}

	public OrganizationPageItem(Long id, String name, String email,
			String phone, Set<String> types) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.types = types;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Set<String> getTypes() {
		return types;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

}
