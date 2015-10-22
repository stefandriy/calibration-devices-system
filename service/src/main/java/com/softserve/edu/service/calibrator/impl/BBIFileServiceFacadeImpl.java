package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.service.calibrator.BBIFileServiceFacade;
import com.softserve.edu.service.calibrator.BbiFileService;
import com.softserve.edu.service.calibrator.CalibratorService;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.*;
import java.util.*;

public class BBIFileServiceFacadeImpl implements BBIFileServiceFacade {
    private static final String[] bbiExtensions = {"bbi", "BBI"};
    private static final String[] dbfExtensions = {"db", "dbf", "DB", "DBF"};


    @Autowired
    private BbiFileService bbiFileService;

    @Autowired
    private CalibratorService calibratorService;

    @Override
    public DeviceTestData parseAndSaveBBIFile(File BBIfile, String verificationID) throws IOException {
        DeviceTestData deviceTestData;
        try(InputStream inputStream = FileUtils.openInputStream(BBIfile)){
            deviceTestData = parseAndSaveBBIFile(inputStream, verificationID, BBIfile.getName());
        }
        return deviceTestData;
    }

    @Override
    public DeviceTestData parseAndSaveBBIFile(MultipartFile BBIfile, String verificationID) throws IOException {
        DeviceTestData deviceTestData = parseAndSaveBBIFile(BBIfile.getInputStream(), verificationID, BBIfile.getName());
        return deviceTestData;
    }

    public DeviceTestData parseAndSaveBBIFile(InputStream inputStream, String verificationID, String originalFileName) throws IOException {
            DeviceTestData deviceTestData = bbiFileService.parseBbiFile(inputStream, originalFileName);
            calibratorService.uploadBbi(inputStream, verificationID, deviceTestData.getInstallmentNumber(), originalFileName);
        return deviceTestData;
    }

    public Map<Boolean, String> parseAndSaveArchiveOfBBIfiles(File archive, String originalFileFullName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        try(InputStream inputStream = FileUtils.openInputStream(archive)){
            return parseAndSaveArchiveOfBBIfiles(inputStream, originalFileFullName);
        }
    }

    @Override
    public Map<Boolean, String> parseAndSaveArchiveOfBBIfiles(MultipartFile archiveStream, String originalFileFullName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        Map<Boolean, String> resultsOfBBIProcessing = parseAndSaveArchiveOfBBIfiles(archiveStream, originalFileFullName);
        return resultsOfBBIProcessing;
    }

    @Transactional
    public Map<Boolean, String> parseAndSaveArchiveOfBBIfiles(InputStream archiveStream, String originalFileFullName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        File directoryWithUnpackedFiles = unpackArchive(archiveStream, originalFileFullName);
        Map<String, String> bbiFileNamesToVerificationMap = getBBIfileNamesToVerificationMap(directoryWithUnpackedFiles);
        List<File> listOfBBIfiles = new ArrayList<>(FileUtils.listFiles(directoryWithUnpackedFiles, bbiExtensions, true));
        Map<Boolean, String> resultsOfBBIProcessing = processListOfBBIFiles(bbiFileNamesToVerificationMap, listOfBBIfiles);
        return resultsOfBBIProcessing;
    }

    private Map<Boolean, String> processListOfBBIFiles(Map<String, String> bbiFileNamesToVerificationMap, List<File> listOfBBIfiles){
        Map<Boolean, String> resultsOfBBIProcessing = new LinkedHashMap<>();
        for (File bbiFile : listOfBBIfiles) {
            String correspondingVerification = bbiFileNamesToVerificationMap.getOrDefault(bbiFile.getName(), null);
            if (correspondingVerification != null) {
                try {
                    parseAndSaveBBIFile(bbiFile, correspondingVerification);
                } catch (IOException e) {
                    resultsOfBBIProcessing.put(false, bbiFile.getName());
                }
                resultsOfBBIProcessing.put(true, bbiFile.getName());
            }
        }
        return resultsOfBBIProcessing;
    }

    private File unpackArchive(InputStream inputStream, String originalFileFullName) throws IOException, ZipException {
        String randomDirectoryName = RandomStringUtils.random(8);
        File directoryForUnpacking = FileUtils.getFile(FileUtils.getTempDirectoryPath(), randomDirectoryName);
        FileUtils.forceMkdir(directoryForUnpacking);
        File zipFileDownloaded = FileUtils.getFile(FileUtils.getTempDirectoryPath(), originalFileFullName);

        try (OutputStream os = new FileOutputStream(zipFileDownloaded)) {
            IOUtils.copy(inputStream, os);
        }

        ZipFile zipFile = new ZipFile(zipFileDownloaded);
        zipFile.extractAll(directoryForUnpacking.toString());
        return directoryForUnpacking;
    }

    private Map<String, String> getBBIfileNamesToVerificationMap(File directoryWithUnpackedFiles) throws SQLException, ClassNotFoundException, FileNotFoundException {
        Map<String, String> bbiFilesToVerification = new LinkedHashMap<>();
        Optional<File> foundDBFile = FileUtils.listFiles(directoryWithUnpackedFiles, dbfExtensions, true).stream().findFirst();
        File dbFile = foundDBFile.orElseThrow(() -> new FileNotFoundException("DBF not found"));

        Class.forName("org.sqlite.JDBC");

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT FileNumber, Id_pc FROM Results");
            while (rs.next()) {
                String verificationID = rs.getString("Id_pc");
                String fileNumber = rs.getString("FileNumber");
                bbiFilesToVerification.put(fileNumber, verificationID);
            }
        }
        return bbiFilesToVerification;
    }
}
