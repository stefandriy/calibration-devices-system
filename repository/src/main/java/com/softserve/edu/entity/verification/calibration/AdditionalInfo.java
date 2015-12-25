package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.verification.Verification;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Created by Vasyl on 20.10.2015.
 */
@Entity
@Table(name = "ADDITIONAL_INFO")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class AdditionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int entrance;
    private int doorCode;
    private int floor;

    @Temporal(TemporalType.DATE)
    private Date dateOfVerif;

    private LocalTime timeFrom;

    private LocalTime timeTo;

    private boolean serviceability;

    @Temporal(TemporalType.DATE)
    private Date noWaterToDate;

    private String notes;

    @OneToOne
    private Verification verification;

    public AdditionalInfo(int entrance, int  doorCode, int floor, Date dateOfVerif, LocalTime timeFrom,
                          LocalTime timeTo, boolean serviceability, Date noWaterToDate, String notes, Verification verification){
        this.entrance = entrance;
        this.doorCode = doorCode;
        this.floor = floor;
        this.dateOfVerif = dateOfVerif;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.serviceability = serviceability;
        this.noWaterToDate = noWaterToDate;
        this.notes = notes;
        this.verification = verification;
    }

    public AdditionalInfo(String entrance,String doorCode, String floor, Long dateOfVerif, boolean serviceability,
                        Long noWaterToDate, String notes, String timeFrom, String timeTo) {

        this.entrance = (entrance != null && !entrance.equals("")) ? Integer.parseInt(entrance) : 0;
        this.doorCode = (doorCode != null && !doorCode.equals("")) ? Integer.parseInt(doorCode): 0;
        this.floor = (floor != null && !floor.equals("")) ? Integer.parseInt(floor) : 0;
        this.dateOfVerif = (dateOfVerif != null) ? new Date(dateOfVerif) : null;
        this.serviceability = serviceability;
        this.noWaterToDate = (noWaterToDate != null) ? new Date(noWaterToDate) : null;
        this.notes = notes;
        this.timeFrom = (timeFrom != null) ? LocalTime.parse(timeFrom) : null;
        this.timeTo = (timeTo != null) ? LocalTime.parse(timeTo) : null;
    }

    public AdditionalInfo(int entrance,int doorCode, int floor, Long dateOfVerif, boolean serviceability,
                          Long noWaterToDate, String notes, String timeFrom, String timeTo) {
        this.entrance = entrance;
        this.doorCode = doorCode;
        this.floor = floor;
        this.dateOfVerif = (dateOfVerif != null) ? new Date(dateOfVerif) : null;
        this.serviceability = serviceability;
        this.noWaterToDate = (noWaterToDate != null) ? new Date(noWaterToDate) : null;
        this.notes = notes;
        this.timeFrom = (timeFrom != null) ? LocalTime.parse(timeFrom) : null;
        this.timeTo = (timeTo != null) ? LocalTime.parse(timeTo) : null;
    }

    public Boolean getServiceability() {
        return this.serviceability;
    }

    public boolean isServiceability() {
        return serviceability;
    }

    public void setDateOfVerif(Long dateOfVerif) {
        this.dateOfVerif = (dateOfVerif != null) ? new Date(dateOfVerif) : null;
    }

    public void setNoWaterToDate(Long noWaterToDate) {
        this.noWaterToDate = (noWaterToDate != null) ? new Date(noWaterToDate) : null;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = (timeFrom != null) ? LocalTime.parse(timeFrom) : null;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = (timeTo != null) ? LocalTime.parse(timeTo) : null;
    }
}
