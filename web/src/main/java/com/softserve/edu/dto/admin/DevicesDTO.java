package com.softserve.edu.dto.admin;

import lombok.Getter;
import lombok.Setter;

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
