package com.softserve.edu.dto.provider;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.device.Counter;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.ClientData;

import com.softserve.edu.entity.verification.calibration.AdditionalInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationStageVerificationDTO {
	private String firstName;
	private String lastName;
	private String middleName;
	private String email;
	private String phone;
	private String secondPhone;

	private String comment;

	private String region;
	private String locality;
	private String district;
	private String street;
	private String building;
	private String flat;
	private Long providerId;
	private Long calibratorId;
	private String calibratorName;
	private Long deviceId;
	private String verificationId;

	private Boolean dismantled;

	private Long dateOfDismantled;
	private Long dateOfMounted;
	private String numberCounter;
	private String releaseYear;

	private String symbol;
	private String standardSize;


	private String entrance;
	private String doorCode;
	private String floor;
	private Long dateOfVerif;
	private String time;

	private Boolean serviceability;
	private Long noWaterToDate;
	private String notes;

	private Long initialDate;
	private Long expirationDate;


	public OrganizationStageVerificationDTO() {
	}

	public OrganizationStageVerificationDTO(ClientData clientData, Address address, Long providerId, Long calibratorId,
											Long deviceId, String verificationId) {
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
		this.calibratorId = calibratorId;
		this.deviceId = deviceId;
		this.verificationId = verificationId;
	}

	public OrganizationStageVerificationDTO(ClientData clientData, Address address, String verificationId,
											Organization calibrator, String comment, AdditionalInfo info,
											Boolean dismantled, Counter counter) {
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

		this.verificationId = verificationId;
		this.calibratorName = (calibrator != null) ? calibrator.getName() : null;
		this.comment = comment;

		this.entrance = (info != null) ? "" + info.getEntrance() : null;
		this.doorCode = (info != null) ? "" + info.getDoorCode() : null;
		this.floor = (info != null) ? "" + info.getFloor() : null;
		this.dateOfVerif = (info != null && info.getDateOfVerif() != null) ? info.getDateOfVerif().getTime() : null;
		this.serviceability = (info != null) ? info.getServiceability() : null;
		this.noWaterToDate = (info != null && info.getNoWaterToDate() != null) ? info.getNoWaterToDate().getTime() : null;
		this.notes = (info != null) ? info.getNotes() : null;
		this.time = (info != null) ? info.getTimeFrom() + "-" + info.getTimeTo() : null;

		this.dismantled = dismantled;

		this.dateOfDismantled = ( counter != null && counter.getDateOfDismantled() != null) ?
				counter.getDateOfDismantled().getTime() : null;
		this.dateOfMounted = (counter != null && counter.getDateOfMounted() != null) ?
				counter.getDateOfMounted().getTime() : null;
		this.numberCounter = (counter != null) ? counter.getNumberCounter() : null;
		this.releaseYear = (counter != null) ? counter.getReleaseYear() : null;

		this.symbol = (counter != null && counter.getCounterType() != null) ? counter.getCounterType().getSymbol() : null;
		this.standardSize = (counter != null && counter.getCounterType() != null) ?
				counter.getCounterType().getStandardSize() : null;
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

	public Long getCalibratorId() {
		return calibratorId;
	}

	public void setCalibratorId(Long calibratorId) {
		this.calibratorId = calibratorId;
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

}
