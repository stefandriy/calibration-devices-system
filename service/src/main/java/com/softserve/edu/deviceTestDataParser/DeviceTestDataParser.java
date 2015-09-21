package com.softserve.edu.deviceTestDataParser;

import java.io.InputStream;
import com.softserve.edu.deviceTestData.DeviceTestData;

/**
 * Created by Taras on 15.09.2015.
 */
public interface DeviceTestDataParser {
    DeviceTestData parse(InputStream deviceTestData);
}
