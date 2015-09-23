package com.softserve.edu.service.calibrator.data.test;

import com.softserve.edu.entity.CalibrationTestData;

public interface CalibrationTestDataService {

    CalibrationTestData findTestData(Long id);

    CalibrationTestData deleteTestData(Long id);

    CalibrationTestData editTestData(Long testDataId, CalibrationTestData testData);
}
