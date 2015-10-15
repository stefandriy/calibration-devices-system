package com.softserve.edu.dto.admin;

public class DeviceDTO {
	private Long id;
	private String deviceType;
	private String deviceSign;
	private String number;
	private String deviceName;

	public DeviceDTO() {
	}

	public DeviceDTO(Long id, String deviceType, String deviceSign,
					 String number, String deviceName) {
		this.id = id;
		this.deviceType = deviceType;
		this.deviceSign = deviceSign;
		this.number = number;
		this.deviceName = deviceName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceSign() {
		return deviceSign;
	}

	public void setDeviceSign(String deviceSign) {
		this.deviceSign = deviceSign;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	

}
