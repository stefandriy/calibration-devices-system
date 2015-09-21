package com.softserve.edu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.DeviceType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "ORGANIZATION")
public class Organization {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private Integer employeesCapacity;
    private Integer maxProcessTime;

    @Embedded
    private Address address;

    /**
     * Identification number of the certificate that allows this UserCalibrator
     * to perform verifications.
     */
    private String certificateNumber;

    /**
     * Identification number of the certificate that allows this calibrator to
     * perform verifications.
     */
    private Date certificateGrantedDate;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    @ElementCollection
    @JoinTable(name = "ORGANIZATION_TYPE", joinColumns = @JoinColumn(name = "organizationId"))
    @Column(name = "value", length = 20)
    @Enumerated(EnumType.STRING)
    private Set<OrganizationType> organizationTypes = new HashSet<>();

    @ElementCollection
    @JoinTable(name = "DEVICE_TYPE", joinColumns = @JoinColumn(name = "organizationId"))
    @Column(name = "value", length = 20)
    @Enumerated(EnumType.STRING)
    private Set<DeviceType> deviceTypes = new HashSet<>();

    public Organization(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Organization(String name, String email, String phone, Integer employeesCapacity, Integer maxProcessTime, Address address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.employeesCapacity = employeesCapacity;
        this.maxProcessTime = maxProcessTime;
        this.address = address;
    }

    public void addUser(User user) {
        user.setOrganization(this);
        users.add(user);
    }

    public void addOrganizationType(OrganizationType organizationType) {
        organizationTypes.add(organizationType);
    }

    public void addDeviceType(OrganizationType organizationType) {
        organizationTypes.add(organizationType);
    }
}
