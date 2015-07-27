package com.softserve.edu.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("name", name)
				.append("deviceType", deviceType)
				.append("manufacturer", manufacturer)
				.append("verificationInterval", verificationInterval)
				.toString();
	}

	@Override
	public int hashCode(){
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.append(deviceType)
				.append(manufacturer)
				.append(verificationInterval)
				.toHashCode();
	}

	@Override
	public boolean equals(final Object obj){
		if(obj instanceof MeasuringEquipment){
			final MeasuringEquipment other = (MeasuringEquipment) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(name, other.name)
					.append(deviceType, other.deviceType)
					.append(manufacturer, other.manufacturer)
					.append(verificationInterval, other.verificationInterval)
					.isEquals();
		} else {
			return false;
		}
	}


}
