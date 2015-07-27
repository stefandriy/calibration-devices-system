package com.softserve.edu.entity.user;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLE")
public class UserRole {
    @Id
    @GeneratedValue
    private Integer id;
    private String role;

    @ManyToMany
    @JoinTable(name = "USERS_ROLES", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "username"))
    private Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserRole(String role) {
        this.role = role;
    }

    public UserRole() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("role", role).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(role)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof UserRole) {
            final UserRole other = (UserRole) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(role, other.role)
                    .isEquals();
        } else {
            return false;
        }
    }
}
