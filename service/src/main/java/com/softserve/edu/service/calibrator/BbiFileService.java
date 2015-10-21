package com.softserve.edu.service.calibrator;

import com.softserve.edu.device.test.data.DeviceTestData;

import java.io.File;
import java.io.IOException;

/**
 * This service provides functionality of getting Bbi file and its content.
 */
public interface BbiFileService {

    File findBbiFileByFileName(String fileName);

    DeviceTestData findBbiFileContentByFileName(String fileName) throws IOException;

}
