package com.softserve.edu.service.calibrator;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.BbiProtocol;

/**
 * Created by Taras on 07.10.2015.
 */
public interface BbiFileService {

    byte[] findBbiFileBytesByFileName(String fileName);

    DeviceTestData findBbiFileContentByFileName(String fileName);

}
