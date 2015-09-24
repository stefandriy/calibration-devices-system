package com.softserve.edu.device.testdata.parser;

import java.io.File;

/**
 * Created by Taras on 15.09.2015.
 */
public class DeviceTestDataParserFactory {
    public DeviceTestDataParser getParser(File f) {
        String fileExtension = f.getName().substring(f.getName().lastIndexOf('.'));
        if (fileExtension.equals(".bbi")) {
            return new BbiDeviceTestDataParser();
        }
        throw new IllegalArgumentException("DeviceTestDataParser for specified file's format was not found.");
    }
}
