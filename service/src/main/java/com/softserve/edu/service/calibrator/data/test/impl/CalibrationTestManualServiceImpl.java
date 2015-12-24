package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.verification.calibration.CalibrationTestManual;
import com.softserve.edu.repository.CalibrationModuleRepository;
import com.softserve.edu.repository.CalibrationTestManualRepository;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestDataManualService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestManualService;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Misha on 12/13/2015.
 */

@Setter
@Service
public class CalibrationTestManualServiceImpl implements CalibrationTestManualService {

    @Value("${document.storage.local}")
    private String localStorage;

    @Autowired
    private CalibrationTestManualRepository calibrationTestManualRepository;

    @Autowired
    private CalibrationModuleRepository calibrationModuleRepository;

    @Autowired
    private CalibrationTestDataManualService calibrationTestDataManualService;

    private Logger logger = Logger.getLogger(CalibrationTestManualServiceImpl.class);


    @Override
    public CalibrationTestManual findTestManual(Long id) {
        return null;
    }

    @Override
    public CalibrationTestManual deleteTestManual(Long id) {
        return null;
    }

    @Override
    @Transactional
    public CalibrationTestManual createNewTestManual(String pathToScan, Integer numberOfTest, String serialNumber, Date dateTest) {
        CalibrationModule calibrationModule = calibrationModuleRepository.findBySerialNumber(serialNumber);
        CalibrationTestManual calibrationTestManual = new CalibrationTestManual(pathToScan, numberOfTest
                , generateNumber(dateTest, calibrationModule.getModuleNumber(), numberOfTest), dateTest, calibrationModule);
        return calibrationTestManualRepository.save(calibrationTestManual);
    }

    @Override
    public String uploadScanDoc(InputStream file, String originalFileFullName) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        UUID uuid = UUID.randomUUID();
        String uri = null;
        try {
            uri = uuid.toString();
            Path path = Paths.get(localStorage + uuid);

            Files.createDirectories(path);
            is = new BufferedInputStream(file);
            os = new BufferedOutputStream(new FileOutputStream(path.toString() + File.separator + originalFileFullName));
            int value;
            while ((value = is.read()) != -1) {
                os.write(value);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error(e);
            throw new Exception(e);
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
        return uri;
    }

    @Override
    @Transactional
    public void editTestManual(String pathToScanDoc, Date dateOfTest, Integer numberOfTest, String serialNumber, CalibrationTestManual calibrationTestManual) {
        CalibrationModule calibrationModule = calibrationModuleRepository.findBySerialNumber(serialNumber);
        calibrationTestManual.setPathToScan(pathToScanDoc);
        calibrationTestManual.setDateTest(dateOfTest);
        calibrationTestManual.setNumberOfTest(numberOfTest);
        calibrationTestManual.setCalibrationModule(calibrationModule);
        calibrationTestManual.setGenerateNumberTest(generateNumber(dateOfTest, calibrationModule.getModuleNumber(), numberOfTest));
        calibrationTestManualRepository.save(calibrationTestManual);
    }

    @Override
    public void deleteScanDoc(String uri) throws IOException {
        Path dirToDel = Paths.get(localStorage + uri);
        Files.walkFileTree(dirToDel, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    logger.error("Exception while iterating directory");
                    throw e;
                }
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }


    @Override
    public byte[] getScanDoc(String uri) throws IOException {
        byte[] scanDoc = null;
        Path scanDocPath = Paths.get(localStorage + uri);
        DirectoryStream<Path> pathDirectoryStream = Files.newDirectoryStream(scanDocPath);
        Iterator<Path> iterator = pathDirectoryStream.iterator();
        scanDoc = Files.readAllBytes(iterator.next());
        return scanDoc;
    }


    public Long generateNumber(Date dateOfTest, String moduleNumber, Integer numberOfTest) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String[] data = dateFormat.format(dateOfTest).split("/");
        String year = data[0].substring(2, 4);
        String month = data[1];
        String day = data[2].substring(0, 2);
        StringBuffer number = new StringBuffer(moduleNumber).append(day).append(month).append(year).append(numberOfTest);
        return Long.valueOf(number.toString());
    }


}
