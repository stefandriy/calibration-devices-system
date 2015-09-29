package com.softserve.edu.dto.application;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.verification.ClientData;

public class ClientStageVerificationDTO {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    private String secondPhone;

    private String region;
    private String locality;
    private String district;
    private String street;
    private String building;
    private String flat;
    private Long providerId;
    private Long deviceId;
    private String verificationId;

    private String comment;

    protected ClientStageVerificationDTO() {
    }

    /**
     *
     * @param clientData
     * @param address
     * @param providerId
     * @param deviceId
     * @param verificationId
     */
    public ClientStageVerificationDTO(ClientData clientData, Address address, Long providerId, Long deviceId,
                                      String verificationId) {
        this.firstName = clientData.getFirstName();
        this.lastName = clientData.getLastName();
        this.middleName = clientData.getMiddleName();
        this.email = clientData.getEmail();
        this.phone = clientData.getPhone();
        this.secondPhone = clientData.getSecondPhone();
        this.region = address.getRegion();
        this.locality = address.getLocality();
        this.district = address.getDistrict();
        this.street = address.getStreet();
        this.building = address.getBuilding();
        this.flat = address.getFlat();
        this.providerId = providerId;
        this.deviceId = deviceId;
        this.verificationId = verificationId;
    }

    /**
     * Constructor for deligation comment
     *
     * @param clientData
     * @param address
     * @param providerId
     * @param deviceId
     * @param verificationId
     * @param comment
     */
    public ClientStageVerificationDTO(ClientData clientData, Address address, Long providerId, Long deviceId,
                                      String verificationId, String comment) {
        this(clientData, address, providerId, deviceId, verificationId);
        this.comment = comment;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
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

    public String getSecondPhone() {
        return secondPhone;
    }

    public void setSecondPhone(String secondPhone) {
        this.secondPhone = secondPhone;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
