package com.softserve.edu.dto.admin;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class OrganizationEditDTO {
    private String name;
    private String email;
    private String phone;
    private List<String> types;
    private List<String> counters;
    private Integer employeesCapacity;
    private Integer maxProcessTime;

    private String codeEDRPOU;
    private String subordination;
    private String certificateNumrAuthoriz;
    private Long certificateDate;

    private String region;
    private String locality;
    private String district;
    private String street;
    private String building;
    private String flat;

    private String regionRegistered;
    private String localityRegistered;
    private String districtRegistered;
    private String streetRegistered;
    private String buildingRegistered;
    private String flatRegistered;

    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String middleName;
    private List<Long> serviceAreas;
}
