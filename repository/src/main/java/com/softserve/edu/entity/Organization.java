package com.softserve.edu.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.edu.entity.user.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ORGANIZATION")
public class Organization {
	@Id
	@GeneratedValue
	@Column(name = "organizationId")
	private Long id;
	private String name;
	private String email;
	private String phone;
	private Boolean isAvailable;

	@Embedded
	private Address address;

	/**
	 * Identification number of the certificate that allows this Usercalibrator
	 * to perform verifications.
	 */
	private String certificateNumber;

	/**
	 * Identification number of the certificate that allows this calibrator to
	 * perform verifications.
	 */
	private Date certificateGrantedDate;


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
	private Set<User> users = new HashSet<User>(0);

//	@ManyToMany
//	@JoinTable(name = "ORGANIZATIONS_TYPES", joinColumns = @JoinColumn(name = "organizationId"),
//	inverseJoinColumns = @JoinColumn(name = "typeId"))
//	private Set<OrganizationType> organizationTypes = new HashSet<OrganizationType>();

	@JsonManagedReference
	 @ManyToMany
	 @JoinTable(name = "ORGANIZATIONS_TYPES", joinColumns = @JoinColumn(name = "organizationId"),
	 inverseJoinColumns = @JoinColumn(name = "id"))
	 private Set<OrganizationType> organizationTypes = new HashSet<OrganizationType>();
	
	
	public void addOrganizationType(OrganizationType organizationType) {
		this.organizationTypes.add(organizationType);
	}

	public Organization() {
	}

	public Organization(String name, String email, String phone) {
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	public Organization(String name, String email, String phone, Address address) {
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<OrganizationType> getOrganizationTypes() {
		return organizationTypes;
	}

	public void setOrganizationTypes(Set<OrganizationType> organizationTypes) {
		this.organizationTypes = organizationTypes;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public Date getCertificateGrantedDate() {
		return certificateGrantedDate;
	}

	public void setCertificateGrantedDate(Date certificateGrantedDate) {
		this.certificateGrantedDate = certificateGrantedDate;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
