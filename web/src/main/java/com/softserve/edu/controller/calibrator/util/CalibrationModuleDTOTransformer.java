package com.softserve.edu.controller.calibrator.util;

import com.softserve.edu.dto.admin.CalibrationModuleDTOLight;
import com.softserve.edu.entity.device.CalibrationModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misha on 12/6/2015.
 */
public class CalibrationModuleDTOTransformer {

    public static List<CalibrationModuleDTOLight> toDtofromList(List<CalibrationModule> list) {
        List<CalibrationModuleDTOLight> resultList = new ArrayList<>();
        for (CalibrationModule calibrationModule : list) {
            resultList.add(new CalibrationModuleDTOLight(calibrationModule.getCondDesignation()
                    , calibrationModule.getModuleType()
                    , calibrationModule.getSerialNumber()));
        }
        return resultList;
    }





}
