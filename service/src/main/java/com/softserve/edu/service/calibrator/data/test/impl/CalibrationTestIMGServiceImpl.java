package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestIMG;
import com.softserve.edu.repository.CalibrationTestIMGRepository;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestIMGService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sonka on 17.11.2015.
 */
@Service
public class CalibrationTestIMGServiceImpl implements CalibrationTestIMGService {

    static final String IMAGE_TYPE = "jpg";
    @Value("${photo.storage.local}")
    private String localStorage;
    @Autowired
    private CalibrationTestIMGRepository testIMGRepository;

    @Override
    public void createTestDataIMGCalibrationTestIMGs(int testDataId, DeviceTestData deviceTestData,
                                                     CalibrationTestData calibrationTestData) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(
                deviceTestData.getBeginPhoto(testDataId))));
        String photoBegin = "beginPhoto" + deviceTestData.getTestNumber(testDataId) + "." + IMAGE_TYPE;
        String absolutePath = localStorage + calibrationTestData.getCalibrationTest().getVerification().getId() + "//";
        ImageIO.write(bufferedImage, IMAGE_TYPE, new File(absolutePath + photoBegin));
        CalibrationTestIMG calibrationTestIMGBegin = new CalibrationTestIMG(calibrationTestData, photoBegin);
        testIMGRepository.save(calibrationTestIMGBegin);

        bufferedImage = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(
                deviceTestData.getEndPhoto(testDataId))));
        String photoEnd = "endPhoto" +deviceTestData.getTestNumber(testDataId) + "." + IMAGE_TYPE;
        ImageIO.write(bufferedImage, IMAGE_TYPE, new File(absolutePath + photoEnd));
        CalibrationTestIMG calibrationTestIMGEnd = new CalibrationTestIMG(calibrationTestData, photoEnd);
        testIMGRepository.save(calibrationTestIMGEnd);
    }
}
