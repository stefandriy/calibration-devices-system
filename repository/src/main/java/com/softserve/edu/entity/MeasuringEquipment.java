package com.softserve.edu.entity;

import javax.persistence.*;


@Entity
@Table(name ="MEASURING_EQUIPMENT")
public class MeasuringEquipment {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String deviceType;

	private String manufacturer;

	private String verificationInterval;

	public MeasuringEquipment() {}

	public MeasuringEquipment(Long id, String name, String manufacturer) {
		super();
		this.id = id;
		this.name = name;
		this.manufacturer = manufacturer;
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

}
