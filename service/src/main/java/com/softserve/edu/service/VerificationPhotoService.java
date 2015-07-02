package com.softserve.edu.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.storage.impl.FileOperationImpl;

import java.nio.file.FileSystems;

@Service
public class VerificationPhotoService {

    @Autowired
    private CalibrationTestService calibrationTestService;

    @Autowired
    private FileOperationImpl fileOperationImpl;

    private String sep = FileSystems.getDefault().getSeparator();

    public boolean putResourse(long testId, InputStream stream, String fileType) {
        CalibrationTest test = calibrationTestService.findTest(testId);
        String verId = test.getVerification().getId();
        String relFolder = verId + sep + testId + sep;
        test.setPhotoPath(fileOperationImpl.putResourse(stream, relFolder, fileType));
        return true;
    }
}