package com.softserve.edu.controller.calibrator;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.dto.CalibrationTestFileDataDTO;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.service.calibrator.BbiFileService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestDataService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.softserve.edu.dto.CalibrationTestDataDTO;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;

import java.io.IOException;


@Controller
@RequestMapping("/calibrator/calibrationTestData/")
public class CalibrationTestDataController {

    @Autowired
    private CalibrationTestDataService service;

    @Autowired
    private CalibrationTestService calibrationTestServiceImpl;

    @Autowired
    private BbiFileService bbiFileService;

    private final Logger logger = Logger.getLogger(CalibrationTestDataController.class);

    @RequestMapping(value = "{testDataId}", method = RequestMethod.GET)
    public ResponseEntity getTestData(@PathVariable Long testDataId) {
        CalibrationTestData foundTestData = service.findTestData(testDataId);
        if (foundTestData != null) {
            return new ResponseEntity<>(foundTestData, HttpStatus.OK);
        } else {
            logger.error("Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Saves calibration-test in database
     *
     * @param testDTO object with calibration-test data
     * @param verificationId String of verification ID for saving calibration-test
     * @return a response body with http status {@literal CREATED} if everything
     *         calibration-test successfully created or else http
     *         status {@literal CONFLICT}
     */
   /* @RequestMapping(value = "addTestData/{testId}", method = RequestMethod.POST)
    public ResponseEntity createCalibrationTestData(@RequestBody CalibrationTestDataDTO testDataDTO, @PathVariable Long testId) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            CalibrationTest foundTest = calibrationTestServiceImpl.findTestById(testId);
            CalibrationTestData calibrationTestData = new CalibrationTestData(testDataDTO.getGivenConsumption(), testDataDTO.getAcceptableError(),
                    testDataDTO.getVolumeOfStandard(), testDataDTO.getInitialValue(), testDataDTO.getEndValue(), testDataDTO.getVolumeInDevice(),
                    testDataDTO.getActualConsumption(), testDataDTO.getConsumptionStatus(), testDataDTO.getCalculationError(), testDataDTO.getTestResult(), foundTest);
            calibrationTestServiceImpl.createTestData(testId, calibrationTestData);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());
            logger.error(e); // for prevent critical issue "Either log or rethrow this exception"
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }

*/

    @RequestMapping(value = "edit/{testDataId}", method = RequestMethod.POST)
    public ResponseEntity editTestData(@PathVariable Long testDataId, @RequestBody CalibrationTestDataDTO testDataDTO) {
    	HttpStatus httpStatus = HttpStatus.OK;
    	try {
    		CalibrationTestData updatedTestData = service.editTestData(testDataId, testDataDTO.saveTestData());
		} catch (Exception e) {
			logger.error("GOT EXCEPTION " + e.getMessage());
            logger.error(e); // for prevent critical issue "Either log or rethrow this exception"
			httpStatus = HttpStatus.CONFLICT;
		}
    	return new ResponseEntity<>(httpStatus);
    }
    
    @RequestMapping(value = "delete/{testDataId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTestData(@PathVariable Long testDataId) {
        CalibrationTestData testData = service.deleteTestData(testDataId);
        return new ResponseEntity<>(testData, HttpStatus.OK);
    }

//    @RequestMapping(value = "parseBbi/{fileName}/{extension}", method = RequestMethod.GET)
//    public ResponseEntity parseBbiData(@PathVariable String fileName, @PathVariable String extension) {
//        ResponseEntity responseEntity;
//        fileName = fileName.concat(".").concat(extension);
//        DeviceTestData deviceTestData;
//        try {
//            deviceTestData = bbiFileService.findBbiFileContentByFileName(fileName);
//            responseEntity = new ResponseEntity(new CalibrationTestFileDataDTO(deviceTestData), HttpStatus.OK);
//        } catch (IOException e) {
//            logger.error("Unable to parse file " + fileName, e);
//            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//        return responseEntity;
//    }

}
