package com.softserve.edu.controller.calibrator.util;

import com.softserve.edu.dto.admin.CalibrationModuleDTO;
import com.softserve.edu.entity.device.CalibrationModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misha on 12/6/2015.
 */
public class CalibrationModuleDTOTransformer {

    public static List<CalibrationModuleDTO> toDtofromList(List<CalibrationModule> list) {
        List<CalibrationModuleDTO> resultList = new ArrayList<>();
        for (CalibrationModule calibrationModule : list) {
            resultList.add(new CalibrationModuleDTO(calibrationModule.getCondDesignation()
                    , calibrationModule.getModuleType()
                    , calibrationModule.getSerialNumber()));
        }
        return resultList;
    }





}
