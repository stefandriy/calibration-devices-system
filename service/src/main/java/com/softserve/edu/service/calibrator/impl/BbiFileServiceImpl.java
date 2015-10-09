package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.device.test.data.BbiDeviceTestData;
import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.repository.UploadBbiRepository;
import com.softserve.edu.service.calibrator.BbiFileService;
import com.softserve.edu.service.parser.DeviceTestDataParser;
import com.softserve.edu.service.parser.DeviceTestDataParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class BbiFileServiceImpl implements BbiFileService {
    @Autowired
    private UploadBbiRepository uploadBbiRepository;

    private DeviceTestDataParserFactory testDataParserFactory = new DeviceTestDataParserFactory();

    @Override
    @Transactional
    public byte[] findBbiFileBytesByFileName(String fileName) {
        final String BBI_EXTENSION = ".bbi";
        String fileNameWithoutExtension = fileName.endsWith(BBI_EXTENSION) ?
                fileName.substring(0, fileName.length() - BBI_EXTENSION.length()) :
                fileName;
        System.out.println(fileNameWithoutExtension);
        byte[] result = uploadBbiRepository.findFileBytesByFileName(fileNameWithoutExtension);
        System.out.println(result);
        return result;
    }

    public DeviceTestData findBbiFileContentByFileName(String fileName) {
        DeviceTestDataParser parser = testDataParserFactory.getParser(fileName);
        InputStream inputStream = new ByteArrayInputStream(findBbiFileBytesByFileName(fileName));
        return parser.parse(inputStream);
    }
}
