package com.softserve.edu.service.calibrator;

import com.softserve.edu.device.test.data.DeviceTestData;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface BBIFileServiceFacade {
    DeviceTestData parseAndSaveBBIFile(MultipartFile BBIfile, String verificationID) throws IOException;
    DeviceTestData parseAndSaveBBIFile(File BBIfile, String verificationID) throws IOException;
    DeviceTestData parseAndSaveBBIFile(InputStream inputStream, String verificationID, String originalFileName) throws IOException;
    Map<Boolean, String> parseAndSaveArchiveOfBBIfiles(MultipartFile archiveStream, String originalFileFullName) throws IOException, ZipException, SQLException, ClassNotFoundException;
    Map<Boolean, String> parseAndSaveArchiveOfBBIfiles(File archive, String originalFileFullName) throws IOException, ZipException, SQLException, ClassNotFoundException;
    Map<Boolean, String> parseAndSaveArchiveOfBBIfiles(InputStream archiveStream, String originalFileFullName) throws IOException, ZipException, SQLException, ClassNotFoundException;
}
