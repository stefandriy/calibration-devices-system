package com.softserve.edu.dto.admin;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Sonka on 25.11.2015.
 */
@Getter
@Setter
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
