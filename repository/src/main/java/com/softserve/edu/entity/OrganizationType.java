package com.softserve.edu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "ORGANIZATION_TYPE")
public class OrganizationType {

	@Id
	@GeneratedValue
	private Integer id;
	private String type;

	@JsonBackReference
	@ManyToMany
	@JoinTable(name = "ORGANIZATIONS_TYPES", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "organizationId"))
	private Set<Organization> organizations = new HashSet<>();
}
