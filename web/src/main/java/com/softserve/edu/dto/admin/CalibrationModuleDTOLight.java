package com.softserve.edu.dto.admin;

import com.softserve.edu.entity.device.CalibrationModule;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Misha on 12/12/2015.
 */

@Setter
@Getter
public class CalibrationModuleDTOLight {


    private String condDesignation;
    private CalibrationModule.ModuleType moduleType;
    private String serialNumber;


    public CalibrationModuleDTOLight() {
    }

    public CalibrationModuleDTOLight(String condDesignation, CalibrationModule.ModuleType moduleType, String serialNumber) {
        this.condDesignation = condDesignation;
        this.moduleType = moduleType;
        this.serialNumber = serialNumber;

    }


}
