package com.softserve.edu.dto.admin;

/**
 * Created by Sonka on 25.11.2015.
 */
public class DevicesDTO {
    private Long id;
    private String name;

    protected DevicesDTO() {
    }

    public DevicesDTO(Long id, String name) {
        this.id = id;
        this.name = name;

    }

}
