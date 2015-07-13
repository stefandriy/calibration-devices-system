package com.softserve.edu.entity.user;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "`USER`")
public class User {
	@Id
	private String username;
	private String password;

	@ManyToMany
	@JoinTable(name = "USERS_ROLES", joinColumns = @JoinColumn(name = "username"), 
	inverseJoinColumns = @JoinColumn(name = "id"))
	private Set<UserRole> userRoles;

	protected User() {
	}

	/**
	 * Required constructor for saving employee in database. Employee cannot
	 * exists without these parameters.
	 *
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @param role
	 *            (look through implementations of Role interface in each
	 *            User-extended class)
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
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

}
