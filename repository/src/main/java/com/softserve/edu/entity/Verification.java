package com.softserve.edu.entity;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Status;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Verification entity. Contains data about whole business process of
 * verification.
 */
@Entity
@Table(name = "`VERIFICATION`")
public class Verification {

	@Id
	private String id;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Enumerated(EnumType.STRING)
	private ReadStatus readStatus;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	@OneToMany
	@JoinColumn(name = "verification_id")
	private Set<CalibrationTest> calibrationTests;

	@ManyToOne
	private Organization provider;

	@ManyToOne
	private User providerEmployee;

	@ManyToOne
	private Organization calibrator;

	@ManyToOne(fetch = FetchType.LAZY)
	private User calibratorEmployee;

	@ManyToOne(fetch = FetchType.LAZY)
	private Organization stateVerificator;
	@ManyToOne
	private User stateVerificatorEmployee;

	@Embedded
	private ClientData clientData;

	@Temporal(TemporalType.DATE)
	private Date initialDate;

	@Temporal(TemporalType.DATE)
	private Date expirationDate;

	@OneToOne
	BbiProtocol bbiProtocol;

	public Verification() {
	}

	public Verification(Date initialDate, ClientData clientData, Organization provider, Device device, Status status,
			ReadStatus readStatus) {
		this(initialDate, clientData, provider, device, status, readStatus, null);
	}

	public Verification(Date initialDate, ClientData clientData, Organization provider,Device device, Status status,
			ReadStatus readStatus, Organization calibrator) {
		this.id = UUID.randomUUID().toString();
		this.initialDate = initialDate;
		this.clientData = clientData;
		this.provider = provider;
		this.device = device;
		this.status = status;
		this.readStatus = readStatus;
		this.calibrator = calibrator;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ReadStatus getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(ReadStatus readStatus) {
		this.readStatus = readStatus;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Set<CalibrationTest> getCalibrationTests() {
		return calibrationTests;
	}

	public void setCalibrationTests(Set<CalibrationTest> calibrationTests) {
		this.calibrationTests = calibrationTests;
	}

	public Organization getProvider() {
		return provider;
	}

	public void setProvider(Organization provider) {
		this.provider = provider;
	}

	public User getProviderEmployee() {
		return providerEmployee;
	}

	public void setProviderEmployee(User providerEmployee) {
		this.providerEmployee = providerEmployee;
	}

	public Organization getCalibrator() {
		return calibrator;
	}

	public void setCalibrator(Organization calibrator) {
		this.calibrator = calibrator;
	}

	public User getCalibratorEmployee() {
		return calibratorEmployee;
	}

	public void setCalibratorEmployee(User calibratorEmployee) {
		this.calibratorEmployee = calibratorEmployee;
	}

	public Organization getStateVerificator() {
		return stateVerificator;
	}

	public void setStateVerificator(Organization stateVerificator) {
		this.stateVerificator = stateVerificator;
	}

	public User getStateVerificatorEmployee() {
		return stateVerificatorEmployee;
	}

	public void setStateVerificatorEmployee(User stateVerificatorEmployee) {
		this.stateVerificatorEmployee = stateVerificatorEmployee;
	}

	public ClientData getClientData() {
		return clientData;
	}

	public void setClientData(ClientData clientData) {
		this.clientData = clientData;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BbiProtocol getBbiProtocol() {
		return bbiProtocol;
	}

	public void setBbiProtocol(BbiProtocol bbiProtocol) {
		this.bbiProtocol = bbiProtocol;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("initialDate", initialDate)
                .append("expirationDate", expirationDate)
                .append("clientData", clientData)
                .append("provider", provider)
                .append("device", device)
                .append("status", status)
                .append("readStatus", readStatus)
                .append("calibrator", calibrator)
                .toString();
    }
    
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
        		.append(id)
                .append(initialDate)
                .append(expirationDate)
                .append(clientData)
                .append(provider)
                .append(device)
                .append(status)
                .append(readStatus)
                .append(calibrator)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Verification){
            final Verification other = (Verification) obj;
            return new EqualsBuilder()
            		.append(id, other.id)
                    .append(initialDate, other.initialDate)
                    .append(expirationDate, other.expirationDate)
                    .append(clientData, other.clientData)
                    .append(provider, other.provider)
                    .append(device, other.device)
                    .append(status, other.status)
                    .append(readStatus, other.readStatus)
                    .append(calibrator, other.calibrator)
                    .isEquals();
        } else{
            return false;
        }
    }
}
