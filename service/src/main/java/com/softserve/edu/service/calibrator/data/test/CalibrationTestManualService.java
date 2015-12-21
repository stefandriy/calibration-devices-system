package com.softserve.edu.service.calibrator.data.test;


import com.softserve.edu.entity.verification.calibration.CalibrationTestManual;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


/**
 * Created by Misha on 12/13/2015.
 */
public interface CalibrationTestManualService {

    CalibrationTestManual findTestManual(Long id);

    CalibrationTestManual deleteTestManual(Long id);

    CalibrationTestManual createNewTestManual(String pathToScan, Integer numberOfTest, String serialNumber, Date dateTest);

    String uploadScanDoc(InputStream file, String originalFileFullName) throws Exception;

    void editTestManual(String pathToScanDoc, Date dateOfTest, Integer numberOfTest, String serialNumber, CalibrationTestManual calibrationTestManual);

    void deleteScanDoc(String uri) throws IOException;

    byte[] getScanDoc(String uri) throws IOException;


}
