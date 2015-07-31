package com.softserve.edu.controller;

import com.softserve.edu.dto.CalibrationTestPageItem;
import com.softserve.edu.dto.PageDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.softserve.edu.dto.CalibrationTestDTO;
import com.softserve.edu.dto.CalibrationTestDataDTO;
import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.service.CalibrationTestService;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;

@Controller
@RequestMapping("/calibrationTests/")
public class CalibrationTestController {

	@Autowired
    private CalibrationTestService testService;
    
    private final Logger logger = Logger.getLogger(CalibrationTestController.class);

    /**
     * Responds a page according to input data and search value
     *
     * @param pageNumber
     *            current page number
     * @param itemsPerPage
     *            count of elements per one page
     * @param search
     *            keyword for looking entities by CalibrationTest.name
     * @return a page of CalibrationTests with their total amount
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{search}", method = RequestMethod.GET)
    public PageDTO<CalibrationTestPageItem> pageCalibrationTestWithSearch(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage, @PathVariable String search) {

        Page<CalibrationTestPageItem> page = testService
                .getCalibrationTestsBySearchAndPagination(pageNumber, itemsPerPage, search)
                .map(calibrationTest -> new CalibrationTestPageItem(calibrationTest.getId(), calibrationTest.getName(),calibrationTest.getDateTest().toString(),
                        calibrationTest.getTemperature().toString(), calibrationTest.getSettingNumber().toString(), calibrationTest.getLatitude().toString(),
                        calibrationTest.getLongitude().toString(), calibrationTest.getConsumptionStatus(), calibrationTest.getPhotoPath()));
        return new PageDTO<>(page.getTotalElements(), page.getContent());
    }

    @RequestMapping(value = "getTest/{calibrationTestId}", method = RequestMethod.GET)
    public ResponseEntity getCalibrationTest(@PathVariable Long calibrationTestId) {
        CalibrationTest foundTest = testService.findTestById(calibrationTestId);
        if (foundTest == null) {
        	 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundTest, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAllCalibrationTests() {
    	try {
    		CalibrationTestList list = testService.findAllCalibrationTests();			
    		return new ResponseEntity<>(list, HttpStatus.OK);
    	 } catch (NotAvailableException exception) {
             throw new com.softserve.edu.exceptions.NotFoundException(exception);
         }
    }
    

//    @RequestMapping(value = "add", method = RequestMethod.POST)
//    public ResponseEntity createCalibrationTest( @RequestBody CalibrationTestDTO testDTO) {
//
//    	HttpStatus httpStatus = HttpStatus.CREATED;
//    	try {
//			CalibrationTest createdTest = testDTO.saveCalibrationTest();
//			testService.createTest(createdTest);
//		} catch (Exception e) {
//			logger.error("GOT EXCEPTION " + e.getMessage());
//			httpStatus = HttpStatus.CONFLICT;
//		}
//    	return new ResponseEntity<>(httpStatus);
//    }

    //IN PROGRESS!
    @RequestMapping(value = "add/{verificationId}", method = RequestMethod.POST)
    public ResponseEntity createCalibrationTest( @RequestBody CalibrationTestDTO testDTO, @PathVariable String verificationId) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            CalibrationTest createdTest = new CalibrationTest(testDTO.getName(), testDTO.getDateTest(), testDTO.getTemperature(),
                    testDTO.getSettingNumber(), testDTO.getLatitude(), testDTO.getLongitude(), testDTO.getConsumptionStatus(), testDTO.getTestResult());
            testService.createNewTest(createdTest, verificationId);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }
    
    @RequestMapping(value = "edit/{calibrationTestId}", method = RequestMethod.POST)
    public ResponseEntity editCalibrationTest(@PathVariable Long calibrationTestId,  @RequestBody CalibrationTestDTO testDTO) {
    	HttpStatus httpStatus = HttpStatus.OK;
    	try {
			testService.editTest(calibrationTestId, testDTO.getName(), testDTO.getDateTest(), testDTO.getTemperature(),
					testDTO.getSettingNumber(), testDTO.getLatitude(), testDTO.getLongitude(), testDTO.getConsumptionStatus());
		} catch (Exception e) {
			logger.error("GOT EXCEPTION " + e.getMessage());
			httpStatus = HttpStatus.CONFLICT;
		}
    	return new ResponseEntity<>(httpStatus);
    }
    
    @RequestMapping(value = "delete/{calibrationTestId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCalibrationTest(@PathVariable Long calibrationTestId) {
        CalibrationTest calibrationTest = testService.deleteTest(calibrationTestId);
        return new ResponseEntity<>(calibrationTest, HttpStatus.OK);
    }


    @RequestMapping(value = "/{calibrationTestId}/testData", method = RequestMethod.POST)
    public ResponseEntity createTestData(@PathVariable Long calibrationTestId, @RequestBody CalibrationTestDataDTO testDataDTO) {
        CalibrationTestData createdTestData;
        try {
            createdTestData = testService.createTestData(calibrationTestId, testDataDTO.saveTestData());
            return new ResponseEntity<>(createdTestData, HttpStatus.CREATED);
        } catch (NotAvailableException e) {
            throw new com.softserve.edu.exceptions.NotFoundException(e);
        }
    }

    @RequestMapping(value = "/{calibrationTestId}/testData")
    public ResponseEntity findAllCalibrationTestData(@PathVariable Long calibrationTestId) {
        try {
            CalibrationTestDataList list = testService.findAllTestDataAsociatedWithTest(calibrationTestId);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (NotAvailableException exception) {
            throw new com.softserve.edu.exceptions.NotFoundException(exception);
        }
    }
}
