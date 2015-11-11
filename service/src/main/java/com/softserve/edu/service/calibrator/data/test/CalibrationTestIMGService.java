package com.softserve.edu.service.calibrator.data.test;


import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestIMG;

public interface CalibrationTestIMGService {
    void saveCalibrationTestIMGPhotoFromParser(DeviceTestData deviceTestData,CalibrationTest calibrationTest);
    void saveCalibrationTestIMGBeginPhotoFromParser(DeviceTestData deviceTestData,CalibrationTest calibrationTest, int testDataId);
    void saveCalibrationTestIMGEndPhotoFromParser(DeviceTestData deviceTestData, CalibrationTest calibrationTest, int testDataId);

}
