package com.softserve.edu.entity.user;

import com.softserve.edu.entity.AddEmployeeBuilderNew;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USER")
public class User {
	@Id
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String middleName;
	private String email;
	private String phone;
	private Boolean isAvaliable = false;

	@Embedded
	private Address address;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private Organization organization;

	@ManyToMany
	@JoinTable(name = "USERS_ROLES", joinColumns = @JoinColumn(name = "username") , inverseJoinColumns = @JoinColumn(name = "id") )
	private Set<UserRole> userRoles = new HashSet<UserRole>();

	public User(AddEmployeeBuilderNew builder) {
		username = builder.username;
		password = builder.password;
		firstName = builder.firstName;
		lastName = builder.lastName;
		middleName = builder.middleName;
		email = builder.email;
		phone = builder.phone;
		address = builder.address;
		isAvaliable = builder.isAveliable;
	}

	/**
	 * Required constructor for saving employee in database. Employee cannot
	 * exists without these parameters.
	 *
	 * @param username
	 *            username
	 * @param password
	 *            password
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User() {
	}

	/**
	 * Required constructor for saving employee in database. Employee cannot
	 * exists without these parameters.
	 *
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @param organization
	 *            its organization
	 */
	public User(String username, String password, Organization organization) {
		this.username = username;
		this.password = password;
		this.organization = organization;
	}

	/**
	 * Completes constructor above with optional values *
	 *
	 * @param firstName
	 *            first name
	 * @param lastName
	 *            last name
	 * @param middleName
	 *            Middle name
	 */
	public User(String firstName, String lastName, String middleName,
				String username, String password, Organization organization) {
		this(username, password, organization);
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
	}

	/**
	 * Completes constructor above with optional values *
	 *
	 * @param firstName
	 *            first name
	 * @param lastName
	 *            last name
	 * @param email
	 *            email
	 * @param phone
	 *            phone number
	 */
	public User(String username, String password, Organization organization, String firstName, String lastName,
			String email, String phone) {
		this(username, password, organization);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void addUserRole(UserRole userRole) {
		this.userRoles.add(userRole);
	}

	public void deleteAllUsersRoles() {
		this.userRoles.clear();
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public Boolean getIsAvaliable() {
		return isAvaliable;
	}

	public void setIsAvaliable(Boolean isAvaliable) {
		this.isAvaliable = isAvaliable;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("username", username).append("password", password)
				.append("firstName", firstName).append("lastName", lastName).append("middleName", middleName)
				.append("phone", phone).append("email", email).append("isAvalisble", isAvaliable).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(username).append(password).append(firstName).append(phone).append(lastName)
				.append(middleName).append(email).append(isAvaliable).toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof User) {
			final User other = (User) obj;
			return new EqualsBuilder().append(username, other.username).append(password, other.password)
					.append(firstName, other.firstName).append(phone, other.phone).append(lastName, other.lastName)
					.append(middleName, other.middleName).append(email, other.email)
					.append(isAvaliable, other.isAvaliable).isEquals();
		} else {
			return false;
		}
	}

}
