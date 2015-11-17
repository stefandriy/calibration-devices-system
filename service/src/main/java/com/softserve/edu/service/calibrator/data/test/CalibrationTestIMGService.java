package com.softserve.edu.service.calibrator.data.test;


import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestIMG;

import java.io.IOException;

public interface CalibrationTestIMGService {
    CalibrationTestIMG createBeginPhotoCalibrationTestIMGs(int testDataId, DeviceTestData deviceTestData, CalibrationTestData calibrationTestData) throws IOException;
    CalibrationTestIMG createEndPhotoCalibrationTestIMGs(int testDataId, DeviceTestData deviceTestData,  CalibrationTestData calibrationTestData) throws IOException;
}
