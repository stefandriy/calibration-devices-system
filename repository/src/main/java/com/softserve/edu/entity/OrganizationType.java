package com.softserve.edu.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ORGANIZATION_TYPE")
public class OrganizationType {
	@Id
	@GeneratedValue
	private Integer id;
	private String type;

	@JsonBackReference
	@ManyToMany
	@JoinTable(name = "ORGANIZATIONS_TYPES", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "organizationId"))
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

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("type", type)
				.toString();
	}

	@Override
	public int hashCode(){
		return new HashCodeBuilder()
				.append(id)
				.append(type)
				.toHashCode();
	}

	@Override
	public boolean equals(final Object obj){
		if(obj instanceof OrganizationType){
			final OrganizationType other = (OrganizationType) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(type, other.type)
					.isEquals();
		} else {
			return false;
		}
	}
}
