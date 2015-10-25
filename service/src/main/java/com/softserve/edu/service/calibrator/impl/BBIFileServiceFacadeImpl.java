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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.*;
import java.util.*;

@Service
public class BBIFileServiceFacadeImpl implements BBIFileServiceFacade {
    private static final String[] bbiExtensions = {"bbi", "BBI"};
    private static final String[] dbfExtensions = {"db", "dbf", "DB", "DBF"};


    @Autowired
    private BbiFileService bbiFileService;

    @Autowired
    private CalibratorService calibratorService;

    @Override
    public DeviceTestData parseAndSaveBBIFile(File BBIfile, String verificationID, String originalFileName) throws IOException {
        DeviceTestData deviceTestData;
        try(InputStream inputStream = FileUtils.openInputStream(BBIfile)){
            deviceTestData = parseAndSaveBBIFile(inputStream, verificationID, originalFileName);
        }
        return deviceTestData;
    }


    public DeviceTestData parseAndSaveBBIFile(MultipartFile BBIfile, String verificationID, String originalFileName) throws IOException {
        DeviceTestData deviceTestData = parseAndSaveBBIFile(BBIfile.getInputStream(), verificationID, originalFileName);
        return deviceTestData;
    }

    @Transactional
    public DeviceTestData parseAndSaveBBIFile(InputStream inputStream, String verificationID, String originalFileName) throws IOException {
            DeviceTestData deviceTestData = bbiFileService.parseBbiFile(inputStream, originalFileName);
            calibratorService.uploadBbi(inputStream, verificationID, deviceTestData.getInstallmentNumber(), originalFileName);
        return deviceTestData;
    }

    public Map<String, Boolean> parseAndSaveArchiveOfBBIfiles(File archive, String originalFileName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        try(InputStream inputStream = FileUtils.openInputStream(archive)){
            return parseAndSaveArchiveOfBBIfiles(inputStream, originalFileName);
        }
    }


    public Map<String, Boolean> parseAndSaveArchiveOfBBIfiles(MultipartFile archiveFile, String originalFileName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        Map<String, Boolean> resultsOfBBIProcessing = parseAndSaveArchiveOfBBIfiles(archiveFile.getInputStream(), originalFileName);
        return resultsOfBBIProcessing;
    }

    @Transactional
    public Map<String, Boolean> parseAndSaveArchiveOfBBIfiles(InputStream archiveStream, String originalFileName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        File directoryWithUnpackedFiles = unpackArchive(archiveStream, originalFileName);
        Map<String, String> bbiFileNamesToVerificationMap = getBBIfileNamesToVerificationMap(directoryWithUnpackedFiles);
        List<File> listOfBBIfiles = new ArrayList<>(FileUtils.listFiles(directoryWithUnpackedFiles, bbiExtensions, true));
        Map<String, Boolean> resultsOfBBIProcessing = processListOfBBIFiles(bbiFileNamesToVerificationMap, listOfBBIfiles);
        return resultsOfBBIProcessing;
    }

    /**
     *
     * @param bbiFileNamesToVerificationMap Map of BBI files names to their corresponding verifications
     * @param listOfBBIfiles List with BBI files extracted from the archive
     * @return Map of BBI filenames to the result of parsing (true/false)
     */
    private Map<String, Boolean> processListOfBBIFiles(Map<String, String> bbiFileNamesToVerificationMap, List<File> listOfBBIfiles){
        Map<String, Boolean> resultsOfBBIProcessing = new LinkedHashMap<>();
        for (File bbiFile : listOfBBIfiles) {
            String correspondingVerification = bbiFileNamesToVerificationMap.getOrDefault(bbiFile.getName(), null);
            if (correspondingVerification != null) {
                try {
                    parseAndSaveBBIFile(bbiFile, correspondingVerification, bbiFile.getName());
                } catch (IOException e) {
                    resultsOfBBIProcessing.put(bbiFile.getName(), false);
                }
                resultsOfBBIProcessing.put(bbiFile.getName(), true);
            }
        }
        return resultsOfBBIProcessing;
    }

    /**
     * Unpacks file into temporary directory
     * @param inputStream InputStream representing archive file
     * @param originalFileName Name of the archive
     * @return Directory to which the archive was unpacked
     * @throws IOException
     * @throws ZipException
     */
    private File unpackArchive(InputStream inputStream, String originalFileName) throws IOException, ZipException {
        String randomDirectoryName = RandomStringUtils.random(8);
        File directoryForUnpacking = FileUtils.getFile(FileUtils.getTempDirectoryPath(), randomDirectoryName);
        FileUtils.forceMkdir(directoryForUnpacking);
        File zipFileDownloaded = FileUtils.getFile(FileUtils.getTempDirectoryPath(), originalFileName);

        try (OutputStream os = new FileOutputStream(zipFileDownloaded)) {
            IOUtils.copy(inputStream, os);
        }

        ZipFile zipFile = new ZipFile(zipFileDownloaded);
        zipFile.extractAll(directoryForUnpacking.toString());
        return directoryForUnpacking;
    }

    /**
     *
     * @param directoryWithUnpackedFiles Directory with unpacked files (should include BBIs and DBF)
     * @return Map of BBI files names to their corresponding verifications
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @implNote Uses sqlite to open DBF
     */
    private Map<String, String> getBBIfileNamesToVerificationMap(File directoryWithUnpackedFiles) throws SQLException, ClassNotFoundException, FileNotFoundException {
        Map<String, String> bbiFilesToVerification = new LinkedHashMap<>();
        Optional<File> foundDBFile = FileUtils.listFiles(directoryWithUnpackedFiles, dbfExtensions, true).stream().findFirst();
        File dbFile = foundDBFile.orElseThrow(() -> new FileNotFoundException("DBF not found"));

        Class.forName("org.sqlite.JDBC");

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT FileNumber, Id_pc FROM Results");
            while (rs.next()) {
                String fileNumber = rs.getString("FileNumber");
                String verificationID = rs.getString("Id_pc");
                bbiFilesToVerification.put(fileNumber, verificationID);
            }
        }
        return bbiFilesToVerification;
    }
}
