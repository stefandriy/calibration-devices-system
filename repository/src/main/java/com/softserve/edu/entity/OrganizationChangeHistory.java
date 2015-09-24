package com.softserve.edu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.edu.entity.util.OrganizationChangeHistoryPK;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by vova on 22.09.15.
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
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
    private String types;

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

    public OrganizationChangeHistory(String name, OrganizationChangeHistoryPK organizationChangeHistoryPK, String email, String phone, Integer employeesCapacity, Integer maxProcessTime, String types, String username, String firstName, String lastName, String middleName, Organization organization, Address address, String adminName) {
        this.name = name;
        this.organizationChangeHistoryPK = organizationChangeHistoryPK;
        this.email = email;
        this.phone = phone;
        this.employeesCapacity = employeesCapacity;
        this.maxProcessTime = maxProcessTime;
        this.types = types;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
        this.address = address;
        this.middleName = middleName;
        this.adminName = adminName;
    }

}
