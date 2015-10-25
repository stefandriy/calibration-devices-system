package com.softserve.edu.dto;

import java.util.Date;

/**
 * Created by Vasyl on 20.10.2015.
 */
public class AdditionalInfoDTO {

    private int entrance;
    private int doorCode;
    private int floor;
    private Date dateOfVerif;
    private String time;
    private boolean serviceability;
    private Date noWaterToDate;
    private String notes;
    private String verificationId;

    public AdditionalInfoDTO(int entrance, int doorCode, int floor, Date dateOfVerif, String time, boolean serviceability, Date noWaterToDate, String notes, String verificationId) {
        this.entrance = entrance;
        this.doorCode = doorCode;
        this.floor = floor;
        this.dateOfVerif = dateOfVerif;
        this.time = time;
        this.serviceability = serviceability;
        this.noWaterToDate = noWaterToDate;
        this.notes = notes;
        this.verificationId = verificationId;
    }

    public int getEntrance() {
        return entrance;
    }

    public void setEntrance(int entrance) {
        this.entrance = entrance;
    }

    public int getDoorCode() {
        return doorCode;
    }

    public void setDoorCode(int doorCode) {
        this.doorCode = doorCode;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Date getDateOfVerif() {
        return dateOfVerif;
    }

    public void setDateOfVerif(Date dateOfVerif) {
        this.dateOfVerif = dateOfVerif;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isServiceability() {
        return serviceability;
    }

    public void setServiceability(boolean serviceability) {
        this.serviceability = serviceability;
    }

    public Date getNoWaterToDate() {
        return noWaterToDate;
    }

    public void setNoWaterToDate(Date noWaterToDate) {
        this.noWaterToDate = noWaterToDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }
}
