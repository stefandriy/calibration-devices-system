package com.softserve.edu.service;


import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.entity.util.CalibrationTestResult;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public interface CalibrationTestService {

     CalibrationTest findTestById(Long testId);

     CalibrationTest findByVerificationId(String verifId);

     CalibrationTestList findAllCalibrationTests();

     Page<CalibrationTest> getCalibrationTestsBySearchAndPagination(int pageNumber, int itemsPerPage, String search);

     void createNewTest(CalibrationTest calibrationTest, Date date, String verificationId);

     CalibrationTest editTest(Long testId, String name, Integer temperature, Integer settingNumber,
                                    Double latitude, Double longitude, String consumptionStatus, CalibrationTestResult testResult);

     void deleteTest(Long testId);

     void createTestData(Long testId, CalibrationTestData testData);

     CalibrationTestDataList findAllTestDataAsociatedWithTest(Long calibrationTestId);

     void uploadPhotos(InputStream file, Long idCalibrationTest, String originalFileFullName) throws IOException;

     CalibrationTest createEmptyTest(String verificationId);

     void createNewCalibrationTestData(CalibrationTestData calibrationTestData);

     CalibrationTest createNewCalibrationTest(Long testId, String name, Integer temperature, Integer settingNumber,
                                                    Double latitude, Double longitude);
}
