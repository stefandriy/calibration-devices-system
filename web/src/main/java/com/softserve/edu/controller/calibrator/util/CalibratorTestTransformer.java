package com.softserve.edu.controller.calibrator.util;

import com.softserve.edu.dto.CalibrationTestFileDataDTO;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;

/**
 * Created by Sonka on 13.11.2015.
 */
public class CalibratorTestTransformer {
    public static CalibrationTestFileDataDTO toDTO(CalibrationTest calibrationTest ) {
        CalibrationTestFileDataDTO result= new CalibrationTestFileDataDTO(calibrationTest.getName(),calibrationTest.getDateTest(),
                calibrationTest.getTemperature(),calibrationTest.getSettingNumber(),calibrationTest.getLatitude(),
                calibrationTest.getLongitude(),calibrationTest.getPhotoPath());

        return result;
    }
}
