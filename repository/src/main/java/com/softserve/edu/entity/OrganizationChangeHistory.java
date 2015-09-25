package com.softserve.edu.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ORGANIZATION_CHANGE_HISTORY")
public class OrganizationChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date date;

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

    public OrganizationChangeHistory(Date date, String name, String email, String phone, Integer employeesCapacity, Integer maxProcessTime, String types, String username, String firstName, String lastName, String middleName, Organization organization, Address address, String adminName) {
        this.date = date;
        this.name = name;
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
