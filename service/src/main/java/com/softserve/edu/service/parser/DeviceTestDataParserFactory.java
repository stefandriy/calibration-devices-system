package com.softserve.edu.service.parser;

import java.io.File;

/**
 * Created by Taras on 15.09.2015.
 */
public class DeviceTestDataParserFactory {
    public DeviceTestDataParser getParser(File f) {
        return getParser(f.getName());
    }

    public DeviceTestDataParser getParser(String fileName) {
        System.out.println("factory: " + fileName);
        System.out.println(fileName.endsWith(".bbi"));
        if (fileName.endsWith(".bbi")) {
            return new BbiDeviceTestDataParser();
        }
        throw new IllegalArgumentException("DeviceTestDataParser for specified file's format was not found.");
    }
}
