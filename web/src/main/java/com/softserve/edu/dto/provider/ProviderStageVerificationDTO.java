package com.softserve.edu.dto.provider;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.User;

public class ProviderStageVerificationDTO {
    private String surname;
    private String name;
    private String middleName;
    private String phone;
    private String secondPhone;
    private String region;
    private String district;
    private String locality;
    private String street;
    private String building;
    private String flat;
    private Organization calibrator;
    private Device device;
    
    private User providerEmployee;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public Organization getCalibrator() {
        return calibrator;
    }

    public void setCalibrator(Organization calibrator) {
        this.calibrator = calibrator;
    }

    public User getProviderEmployee() {
        return providerEmployee;
    }

    public void setProviderEmployee(User providerEmployee) {
        this.providerEmployee = providerEmployee;
    }

    public String getSecondPhone() {
        return secondPhone;
    }

    public void setSecondPhone(String secondPhone) {
        this.secondPhone = secondPhone;
    }

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
}
