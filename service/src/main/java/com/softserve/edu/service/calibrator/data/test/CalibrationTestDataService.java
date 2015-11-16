package com.softserve.edu.service.calibrator.data.test;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.service.tool.DeviceService;

public interface CalibrationTestDataService {

    CalibrationTestData findTestData(Long id);

    CalibrationTestData deleteTestData(Long id);

    CalibrationTestData editTestData(Long testDataId, CalibrationTestData testData);
    CalibrationTestData createNewTestData(Long testId, DeviceTestData deviceTestData);
}

