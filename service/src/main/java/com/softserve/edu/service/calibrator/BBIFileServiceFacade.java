package com.softserve.edu.service.calibrator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface BBIFileServiceFacade {
    void parseAndSaveBBIFile(File BBIfile, String verificationID) throws IOException;
}
