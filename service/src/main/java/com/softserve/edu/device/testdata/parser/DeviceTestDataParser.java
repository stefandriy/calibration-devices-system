package com.softserve.edu.device.testdata.parser;

import java.io.InputStream;
import com.softserve.edu.device.test.data.DeviceTestData;

/**
 * Created by Taras on 15.09.2015.
 */
public interface DeviceTestDataParser {
    DeviceTestData parse(InputStream deviceTestData);
}
