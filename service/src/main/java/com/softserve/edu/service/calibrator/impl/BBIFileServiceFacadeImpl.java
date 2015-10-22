package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.service.calibrator.BBIFileServiceFacade;
import com.softserve.edu.service.calibrator.BbiFileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BBIFileServiceFacadeImpl implements BBIFileServiceFacade {
    @Autowired
    private BbiFileService bbiFileService;

    @Override
    public void parseAndSaveBBIFile(File BBIfile, String verificationID) throws IOException {
        try(InputStream inputStream = FileUtils.openInputStream(BBIfile)){

        }
    }
}
