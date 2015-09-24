package com.softserve.edu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.edu.entity.util.DeviceType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "`DEVICE`")
public class Device {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String deviceName;

	@Enumerated(EnumType.STRING)
	private DeviceType deviceType;

	private String deviceSign;

	private String number;

	@OneToMany(mappedBy = "device")
	@JsonBackReference
	private Set<Verification> verifications;

	@ManyToOne
	@JoinColumn(name = "provider_organizationId")
	@JsonManagedReference
	private Organization provider;

	@ManyToOne
	@JoinColumn(name = "manufacturerId")
	private Manufacturer manufacturer;

	public Device() {
	}

	public Device(String number, Set<Verification> verifications, Manufacturer manufacturer) {
		this.number = number;
		this.verifications = verifications;
		this.manufacturer = manufacturer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
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

	public Set<Verification> getVerifications() {
		return verifications;
	}

	public void setVerifications(Set<Verification> verifications) {
		this.verifications = verifications;
	}

	public Organization getProvider() {
		return provider;
	}

	public void setProvider(Organization provider) {
		this.provider = provider;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("deviceName", deviceName)
				.append("deviceType", deviceType)
				.append("deviceSign", deviceSign)
				.append("number", number)
				.append("provider", provider)
				.append("manufacturer", manufacturer)
				.toString();
	}

	@Override
	public int hashCode(){
		return new HashCodeBuilder()
				.append(id)
				.append(deviceName)
				.append(deviceType)
				.append(deviceSign)
				.append(number)
				.append(provider)
				.append(manufacturer)
				.toHashCode();
	}

	@Override
	public boolean equals(final Object obj){
		if(obj instanceof Device){
			final Device other = (Device) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(deviceName, other.deviceName)
					.append(deviceType, other.deviceType)
					.append(deviceSign, other.deviceSign)
					.append(number, other.number)
					.append(provider, other.provider)
					.append(manufacturer, other.manufacturer)
					.isEquals();
		} else{
			return false;
		}
	}
}
