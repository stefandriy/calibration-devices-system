package com.softserve.edu.entity;

import com.softserve.edu.entity.util.CalibrationTestResult;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.Set;

/**
 * Calibration Test entity.
 * Contains data about measurement device calibration test.
 */
@Entity
@Table(name = "`CALIBRATION_TEST`")
public class CalibrationTest {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTest;
    private Integer temperature;
    private Integer settingNumber;
    private Double latitude;
    private Double longitude;
    private String consumptionStatus;
    @Enumerated(EnumType.STRING)
    private CalibrationTestResult testResult;
    private String photoPath;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name",
                    column = @Column(name = "document_name")),
            @AttributeOverride(name = "sign",
                    column = @Column(name = "document_sign"))
    })
    private MeteorologicalDocument meteorologicalDocument;

    @ManyToOne
    private Verification verification;

    @OneToMany(mappedBy = "calibrationTest")
    private Set<CalibrationTestData> calibrationTestDatas;

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

    public Date getDateTest() {
        return dateTest;
    }

    public void setDateTest(Date dateTest) {
        this.dateTest = dateTest;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getSettingNumber() {
        return settingNumber;
    }

    public void setSettingNumber(Integer settingNumber) {
        this.settingNumber = settingNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(String consumptionStatus) {
        this.consumptionStatus = consumptionStatus;
    }

    public CalibrationTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(CalibrationTestResult testResult) {
        this.testResult = testResult;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    public Set<CalibrationTestData> getCalibrationTestDatas() {
        return calibrationTestDatas;
    }

    public void setCalibrationTestDatas(Set<CalibrationTestData> calibrationTestDatas) {
        this.calibrationTestDatas = calibrationTestDatas;
    }

    public MeteorologicalDocument getMeteorologicalDocument() {
        return meteorologicalDocument;
    }

    public void setMeteorologicalDocument(
            MeteorologicalDocument meteorologicalDocument) {
        this.meteorologicalDocument = meteorologicalDocument;
    }
  
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("dateTest", dateTest)
                .append("temperature", temperature)
                .append("settingNumber", settingNumber)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("consumptionStatus", consumptionStatus)
                .append("testResult", testResult)
                .append("photoPath", photoPath)
                .toString();
    }
    
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
        		.append( id)
                .append(name)
                .append(dateTest)
                .append(temperature)
                .append(settingNumber)
                .append(latitude)
                .append(longitude)
                .append(consumptionStatus)
                .append(testResult)
                .append(photoPath)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof CalibrationTest){
            final CalibrationTest other = (CalibrationTest) obj;
            return new EqualsBuilder()
            		.append(id, other.id)
                    .append(name, other.name)
                    .append(dateTest, other.dateTest)
                    .append(temperature, other.temperature)
                    .append(settingNumber, other.settingNumber)
                    .append(latitude, other.latitude)
                    .append(longitude, other.longitude)
                    .append(consumptionStatus, other.consumptionStatus)
                    .append(testResult, other.testResult)
                    .append(photoPath, other.photoPath)
                    .isEquals();
        } else{
            return false;
        }
    }
}
