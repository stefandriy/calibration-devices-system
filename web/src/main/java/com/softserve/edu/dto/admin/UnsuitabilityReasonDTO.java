package com.softserve.edu.dto.admin;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Sonka on 23.11.2015.
 */
@Getter
@Setter
public class UnsuitabilityReasonDTO {
    private Long id;
    private String deviceName;
    private String name;
    private Long deviceId;

    public UnsuitabilityReasonDTO() {}

    public UnsuitabilityReasonDTO(Long id, String name, Long deviceId,String deviceName) {
        this.id = id;
        this.name = name;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }
}
