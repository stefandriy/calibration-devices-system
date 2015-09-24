package com.softserve.edu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.edu.entity.util.OrganizationChangeHistoryPK;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by vova on 22.09.15.
 */

@Entity
@Table(name = "ORGANIZATION_CHANGE_HISTORY")
public class OrganizationChangeHistory implements Serializable{


    @EmbeddedId
    private OrganizationChangeHistoryPK organizationChangeHistoryPK;

    private String name;
    private String email;
    private String phone;
    private Integer employeesCapacity;
    private Integer maxProcessTime;

    private String username;
    private String firstName;
    private String lastName;
    private String middleName;
    
    private String adminName;

    @Embedded
    private Address address;


    @ManyToOne
    @JoinColumn(name = "organizationId")
    private Organization organization;

    public OrganizationChangeHistory (){}

    public OrganizationChangeHistory(String name, OrganizationChangeHistoryPK organizationChangeHistoryPK, String email, String phone, Integer employeesCapacity, Integer maxProcessTime, String username, String firstName, String lastName, String middleName, Organization organization, Address address, String adminName) {
        this.name = name;
        this.organizationChangeHistoryPK = organizationChangeHistoryPK;
        this.email = email;
        this.phone = phone;
        this.employeesCapacity = employeesCapacity;
        this.maxProcessTime = maxProcessTime;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
        this.address = address;
        this.middleName = middleName;
        this.adminName = adminName;
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

    public Integer getMaxProcessTime() {
        return maxProcessTime;
    }

    public void setMaxProcessTime(Integer maxProcessTime) {
        this.maxProcessTime = maxProcessTime;
    }

    public Integer getEmployeesCapacity() {
        return employeesCapacity;
    }

    public void setEmployeesCapacity(Integer employeesCapacity) {
        this.employeesCapacity = employeesCapacity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getadminName() {
        return adminName;
    }

    public void setadminName(String adminName) {
        this.adminName = adminName;
    }

    public OrganizationChangeHistoryPK getOrganizationChangeHistoryPK() {
        return organizationChangeHistoryPK;
    }

    public void setOrganizationChangeHistoryPK(OrganizationChangeHistoryPK organizationChangeHistoryPK) {
        this.organizationChangeHistoryPK = organizationChangeHistoryPK;
    }
}
