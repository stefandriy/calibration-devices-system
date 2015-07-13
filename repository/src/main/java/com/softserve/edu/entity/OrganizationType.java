package com.softserve.edu.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "`ORGANIZATION_TYPE`")
public class OrganizationType {
	@Id
	@GeneratedValue
	@Column(name="`typeId`")
	private Integer id;
	private String type;

	@ManyToMany
	@JoinTable(name = "ORGANIZATIONS_TYPES", joinColumns = @JoinColumn(name = "typeId"),
	inverseJoinColumns = @JoinColumn(name = "organizationId"))
	private Set<Organization> organizations = new HashSet<Organization>();

	public void addOrganization(Organization organization) {
		this.organizations.add(organization);
	}

	public OrganizationType() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<Organization> organizations) {
		this.organizations = organizations;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
