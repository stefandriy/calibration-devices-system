package com.softserve.edu.service;

import com.softserve.edu.entity.CalibrationTestData;

public interface CalibrationTestDataService {

    CalibrationTestData findTestData(Long id);

    CalibrationTestData deleteTestData(Long id);

    CalibrationTestData editTestData(Long testDataId, CalibrationTestData testData);
}
