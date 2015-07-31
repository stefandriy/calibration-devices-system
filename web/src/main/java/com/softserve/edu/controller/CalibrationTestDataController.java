package com.softserve.edu.controller;


import com.softserve.edu.controller.calibrator.CalibrationTestController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.softserve.edu.dto.CalibrationTestDataDTO;
import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.service.CalibrationTestDataService;

@Controller
@RequestMapping("/calibrationTestData/")
public class CalibrationTestDataController {

    @Autowired
    private CalibrationTestDataService service;
    
    private final Logger logger = Logger.getLogger(CalibrationTestController.class);

    @RequestMapping(value = "{testDataId}", method = RequestMethod.GET)
    public ResponseEntity getTestData(@PathVariable Long testDataId) {
        CalibrationTestData foundtestData = service.findTestData(testDataId);
        if (foundtestData != null) {
            return new ResponseEntity<>(foundtestData, HttpStatus.OK);
        } else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "edit/{testDataId}", method = RequestMethod.POST)
    public ResponseEntity editTestData(@PathVariable Long testDataId, @RequestBody CalibrationTestDataDTO testDataDTO) {
    	HttpStatus httpStatus = HttpStatus.OK;
    	try {
    		CalibrationTestData updatedTestData = service.editTestData(testDataId, testDataDTO.saveTestData());
		} catch (Exception e) {
			logger.error("GOT EXCEPTION " + e.getMessage());
			httpStatus = HttpStatus.CONFLICT;
		}
    	return new ResponseEntity<>(httpStatus);
    }
    
    @RequestMapping(value = "delete/{testDataId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTestData(@PathVariable Long testDataId) {
        CalibrationTestData testData = service.deleteTestData(testDataId);
        return new ResponseEntity<>(testData, HttpStatus.OK);
    }


}
