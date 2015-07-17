package com.softserve.edu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.softserve.edu.entity.util.DeviceType;

@Entity
@Table(name = "'MEASURING_EQUIPMENT'")
public class MeasuringEquipment {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	private DeviceType deviceType;

	@ManyToOne
	private Manufacturer manufacturer;

	@Column(nullable = false)
	private String verificationInterval;

	public MeasuringEquipment() {}

	public MeasuringEquipment(Long id, String name, Manufacturer manufacturer) {
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

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getVerificationInterval() {
		return verificationInterval;
	}

	public void setVerificationInterval(String verificationInterval) {
		this.verificationInterval = verificationInterval;
	}

}
