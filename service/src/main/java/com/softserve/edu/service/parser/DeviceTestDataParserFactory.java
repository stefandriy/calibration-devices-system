package com.softserve.edu.service.parser;

import java.io.File;

/**
 * Created by Taras on 15.09.2015.
 */
public class DeviceTestDataParserFactory {
    public DeviceTestDataParser getParser(File f) {
        return getParser(f.getName());
    }

    /***
     * Finds appropriate parser for file
     * @param fileName
     * @return
     */
    public DeviceTestDataParser getParser(String fileName) {
        if (fileName.endsWith(".bbi")) {
            return new BbiDeviceTestDataParser();
        }
        throw new IllegalArgumentException("DeviceTestDataParser for specified file's format was not found.");
    }

}
