package com.softserve.edu.dto.calibrator;

public class MeasuringEquipmentPageItem {
	private Long id;
	private String name;
	private String equipmentType;
	private String manufacturer;
	private String verificationInterval;

	public MeasuringEquipmentPageItem() {
	}

	public MeasuringEquipmentPageItem(Long id, String name,
			String equipmentType, String manufacturer,
			String verificationInterval) {
		this.id = id;
		this.name = name;
		this.equipmentType = equipmentType;
		this.manufacturer = manufacturer;
		this.verificationInterval = verificationInterval;
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

	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
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
