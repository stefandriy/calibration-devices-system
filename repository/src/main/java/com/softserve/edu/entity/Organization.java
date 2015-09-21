package com.softserve.edu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.edu.entity.user.User;
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
    @Column(name = "organizationId")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @JsonBackReference
    private Set<User> users = new HashSet<User>();

    @OneToMany(mappedBy = "provider")
    @JsonBackReference
    private Set<Device> devices;

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    @ManyToMany
    @JoinTable(name = "ORGANIZATIONS_TYPES", joinColumns = @JoinColumn(name = "organizationId"), inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<OrganizationType> organizationTypes = new HashSet<>();

    public void addOrganizationType(OrganizationType organizationType) {
        this.organizationTypes.add(organizationType);
    }

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
}
