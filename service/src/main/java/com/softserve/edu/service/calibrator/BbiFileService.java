package com.softserve.edu.service.calibrator;

import com.softserve.edu.device.test.data.DeviceTestData;

import java.io.File;

/**
 * Created by Taras on 07.10.2015.
 */
// TODO Add description of the service
public interface BbiFileService {

    File findBbiFileByFileName(String fileName);

    DeviceTestData findBbiFileContentByFileName(String fileName);

}
