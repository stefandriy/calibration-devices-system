package com.softserve.edu.dto;


public class FieldDTO {
    private String field;
    private String type;

    public FieldDTO() {
    }

    public FieldDTO(String field, String type) {
        this.field = field;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
