package com.softserve.edu.dto.calibrator;

import com.softserve.edu.entity.MeasuringEquipment;

public class MeasuringEquipmentDTO {

	private Long id;
	private String name;
	private String deviceType;
	private String manufacturer;
	private String verificationInterval;

	public MeasuringEquipmentDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getVerificationInterval() {
		return verificationInterval;
	}

	public void setVerificationInterval(String verificationInterval) {
		this.verificationInterval = verificationInterval;
	}

	public MeasuringEquipment saveEquipment(){
		MeasuringEquipment measuringEquipment = new MeasuringEquipment();
		measuringEquipment.setName(name);
		measuringEquipment.setDeviceType(deviceType);
		measuringEquipment.setManufacturer(manufacturer);
		measuringEquipment.setVerificationInterval(verificationInterval);
		return measuringEquipment;
	}

}
