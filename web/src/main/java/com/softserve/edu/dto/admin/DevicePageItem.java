package com.softserve.edu.dto.admin;

public class DevicePageItem {
	private Long id;
	private String deviceSign;
	private String number;
	
	public DevicePageItem() {
		}
	

	public DevicePageItem(Long id, String deviceSign, String number) {
		this.id = id;
		this.deviceSign = deviceSign;
		this.number = number;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	
	
	
}
