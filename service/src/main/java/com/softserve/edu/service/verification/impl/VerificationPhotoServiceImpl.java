package com.softserve.edu.service.verification.impl;

import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import com.softserve.edu.service.storage.FileOperations;
import com.softserve.edu.service.verification.VerificationPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.FileSystems;

@Service
public class VerificationPhotoServiceImpl implements VerificationPhotoService {

    @Autowired
    private CalibrationTestService calibrationTestService;

    @Autowired
    private FileOperations fileOperations;

    private String sep = FileSystems.getDefault().getSeparator();

    @Override
    public boolean putResource(long testId, InputStream stream, String fileType) {
        CalibrationTest test = calibrationTestService.findTestById(testId);
        String verId = test.getVerification().getId();
        String relFolder = verId + sep + testId + sep;
        test.setPhotoPath(fileOperations.putResourse(stream, relFolder, fileType));
        return true;
    }
}