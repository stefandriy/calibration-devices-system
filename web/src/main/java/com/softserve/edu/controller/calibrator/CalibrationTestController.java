package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.calibrator.util.CalibrationModuleDTOTransformer;
import com.softserve.edu.dto.*;
import com.softserve.edu.dto.admin.CalibrationModuleDTO;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestDataManual;
import com.softserve.edu.entity.verification.calibration.CalibrationTestManual;
import com.softserve.edu.exceptions.NotFoundException;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.service.admin.CalibrationModuleService;
import com.softserve.edu.service.calibrator.BBIFileServiceFacade;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestDataManualService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestManualService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/calibrator/calibrationTests/")
public class CalibrationTestController {

    @Autowired
    private CalibrationTestRepository testRepository;

    @Autowired
    private CalibrationTestDataRepository testDataRepository;

    @Autowired
    private CalibrationTestService testService;

    private final Logger logger = Logger.getLogger(CalibrationTestController.class);

    private static final String contentExtPattern = "^.*\\.(jpg|JPG|gif|GIF|png|PNG|tif|TIF|)$";

    private static final String contentDocExtPattern = "^.*\\.(PDF|pdf|jpg|JPG|gif|GIF|png|PNG|tif|TIF|)$";

    @Autowired
    private BBIFileServiceFacade bbiFileServiceFacade;

    @Autowired
    private CalibrationModuleService calibrationModuleService;

    @Autowired
    private CalibrationTestManualService calibrationTestManualService;


    @Autowired
    private CalibrationTestDataManualService calibrationTestDataManualService;

    /**
     * Returns calibration-test by ID
     *
     * @param testId
     * @return calibration-test
     */
    @RequestMapping(value = "getTest/{testId}", method = RequestMethod.GET)
    public CalibrationTestDTO getCalibrationTest(@PathVariable Long testId) {
        CalibrationTest foundTest = testService.findTestById(testId);
        CalibrationTestDTO testDTO = new CalibrationTestDTO(foundTest.getName(), foundTest.getCapacity(), foundTest.getSettingNumber(),
                foundTest.getLatitude(), foundTest.getLongitude(), foundTest.getConsumptionStatus(), foundTest.getTestResult());
        return testDTO;
    }


    /**
     * Finds all calibration-tests form database
     *
     * @return a list of calibration-tests
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAllCalibrationTests() {
        try {
            CalibrationTestList list = testService.findAllCalibrationTests();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (NotAvailableException exception) {
            throw new NotFoundException(exception);
        }
    }

    /**
     * Saves calibration-test and  it`s test-data to DB
     *
     * @param formdata
     * @param testId   Takes an object which contains 2 objects with data(Buy the way second object contains 6 objects  with  data).
     */
   /* @RequestMapping(value = "add/{testId}", method = RequestMethod.POST)
    public void createCalibrationTest(@RequestBody TestGenerallDTO formdata, @PathVariable Long testId) {
        CalibrationTestDTO testFormData = formdata.getTestForm();
        CalibrationTest calibrationTest = testService.createNewCalibrationTest(testId, testFormData.getName(), testFormData.getTemperature(), testFormData.getSettingNumber(),
                testFormData.getLatitude(), testFormData.getLongitude());
        List<CalibrationTestDataDTO> testDatas = formdata.getSmallForm();
        for (CalibrationTestDataDTO data : testDatas) {
            if (data == null) break;
            CalibrationTestData testData = new CalibrationTestData();
            testData.setGivenConsumption(data.getGivenConsumption());
            testData.setAcceptableError(data.getAcceptableError());
            testData.setVolumeOfStandard(data.getVolumeOfStandard());
            testData.setInitialValue(data.getInitialValue());
            testData.setEndValue(data.getEndValue());
            testData.setVolumeInDevice(data.getVolumeInDevice());
            testData.setActualConsumption(data.getActualConsumption());
            testData.setConsumptionStatus(data.getConsumptionStatus());
            testData.setCalibrationTest(calibrationTest);
            testService.createNewCalibrationTestData(testData);
        }
    }*/

    /**
     * Edit calibration-test in database
     *
     * @param testDTO           object with calibration-test data
     * @param calibrationTestId
     * @return a response body with http status {@literal OK} if calibration-test
     * successfully edited or else http status {@literal CONFLICT}
     */
    @RequestMapping(value = "edit/{calibrationTestId}", method = RequestMethod.POST)
    public ResponseEntity editCalibrationTest(@PathVariable Long calibrationTestId, @RequestBody CalibrationTestDTO testDTO) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            testService.editTest(calibrationTestId, testDTO.getName(), testDTO.getCapacity(),
                    testDTO.getSettingNumber(), testDTO.getLatitude(), testDTO.getLongitude(), testDTO.getConsumptionStatus(), testDTO.getTestResult());
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());
            logger.error(e); // for prevent critical issue "Either log or rethrow this exception"
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }

    /**
     * Deletes selected calibration-test by Id
     *
     * @param calibrationTestId
     * @return a response body with http status {@literal OK} if calibration-test
     * successfully deleted
     */
    @RequestMapping(value = "delete/{calibrationTestId}", method = RequestMethod.POST)
    public void deleteCalibrationTest(@PathVariable Long calibrationTestId) {
        testService.deleteTest(calibrationTestId);
    }


    /**
     * Finds all calibration-tests data form database
     *
     * @return a list of calibration-tests data
     */
    @RequestMapping(value = "/{calibrationTestId}/testData", method = RequestMethod.GET)
    public ResponseEntity findAllCalibrationTestData(@PathVariable Long calibrationTestId) {
        try {
            CalibrationTestDataList list = testService.findAllTestDataAsociatedWithTest(calibrationTestId);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (NotAvailableException exception) {
            logger.error("Not found " + exception);
            throw new com.softserve.edu.exceptions.NotFoundException(exception);
        }
    }

    /**
     * Uploads a photoes to chosen directory bu calibration-test ID
     *
     * @param file              chosen file oject
     * @param idCalibrationTest
     * @return httpStatus 200 OK if everything went well
     */
    @RequestMapping(value = "uploadPhotos", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFilePhoto(@RequestBody MultipartFile file, @RequestParam Long idCalibrationTest) {
        ResponseEntity<String> httpStatus = new ResponseEntity(HttpStatus.OK);
        try {
            String originalFileName = file.getOriginalFilename();
            String fileType = originalFileName.substring(originalFileName.lastIndexOf('.'));
            if (Pattern.compile(contentExtPattern, Pattern.CASE_INSENSITIVE).matcher(fileType).matches()) {
                testService.uploadPhotos(file.getInputStream(), idCalibrationTest, originalFileName);
            } else {
                logger.error("Failed to load file ");
                httpStatus = new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Failed to get protocol " + e.getMessage());
            logger.error(e); // for prevent critical issue "Either log or rethrow this exception"
            httpStatus = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return httpStatus;
    }


    /**
     * creates an empty test instead ID generating for test-datas
     *
     * @param verificationId
     */
   /* @RequestMapping(value = "createEmptyTest/{verificationId}", method = RequestMethod.GET)
    public Long createEmptyTest(@PathVariable String verificationId) {
        CalibrationTest test = testService.createEmptyTest(verificationId);
        return test.getId();
    }*/


    /**
     * get all calibration module for handmade protocol
     *
     * @return CalibrationModuleDTO
     */
    @RequestMapping(value = "getCalibrationModule", method = RequestMethod.GET)
    public List<CalibrationModuleDTO> getCalibrationModule() {
        List list = null;
        try {
            CalibrationModuleDTOTransformer calibrationModuleDTOTransformer = new CalibrationModuleDTOTransformer();
            list = calibrationModuleDTOTransformer.toDtofromList(calibrationModuleService.findAllModules());
        } catch (Exception e) {
            logger.error("Failed to get list of calibrationModule" + e.getMessage());
            logger.error(e);
        }
        return list;
    }


    /**
     * @param calibrationTestManualDTO
     * @return httpStatus 200 OK if everything went well
     */
    @RequestMapping(value = "createTestManual", method = RequestMethod.POST)
    public ResponseEntity createTestManual(@RequestBody CalibrationTestManualDTO calibrationTestManualDTO) {
        ResponseEntity<String> responseEntity = new ResponseEntity(HttpStatus.OK);
        try {
            CalibrationTestManual calibrationTestManual = calibrationTestManualService.createNewTestManual("d:/toPath", calibrationTestManualDTO.getNumberOfTest(),
                    calibrationTestManualDTO.getSerialNumber(), calibrationTestManualDTO.getDateOfTest());
            for (CalibrationTestDataManualDTO calibrationTDMDTO : calibrationTestManualDTO.getListOfCalibrationTestDataManual()) {
                calibrationTestDataManualService.createNewTestDataManual(calibrationTDMDTO.getStatusTestFirst()
                        , calibrationTDMDTO.getStatusTestSecond(), calibrationTDMDTO.getStatusTestThird()
                        , calibrationTDMDTO.getStatusCommon(), calibrationTDMDTO.getCounterId()
                        , calibrationTestManual, calibrationTDMDTO.getId());
            }
        } catch (Exception e) {
            logger.error(e);
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }


    /**
     * get protocol manual
     *
     * @param verificationId
     * @return protocol manual
     */
    @RequestMapping(value = "getProtocolManual/{verificationId}", method = RequestMethod.GET)
    public ResponseEntity<CalibrationTestDataManualDTO> getProtocolManual(@PathVariable String verificationId) {
        ResponseEntity<CalibrationTestDataManualDTO> responseEntity;
        try {
            CalibrationTestDataManual cTestDataManual = calibrationTestDataManualService.findByVerificationId(verificationId);
            CalibrationTestManual cTestManual = cTestDataManual.getCalibrationTestManual();
            CalibrationTestDataManualDTO cTestDataManualDTO = new CalibrationTestDataManualDTO(
                    cTestDataManual.getStatusTestFirst().toString()
                    , cTestDataManual.getStatusTestSecond().toString()
                    , cTestDataManual.getStatusTestThird().toString()
                    , cTestDataManual.getStatusCommon().toString(), new CalibrationTestManualDTO(
                    cTestManual.getCalibrationModule().getSerialNumber()
                    , cTestManual.getNumberOfTest()
                    , cTestManual.getDateTest()
                    , cTestManual.getGenerateNumberTest()));
            responseEntity = new ResponseEntity(cTestDataManualDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to get manual protocol" + e.getMessage());
            logger.error(e);
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    /**
     * Edit calibration test manual
     *
     * @param cTestManualDTO object with calibration-test-manual
     * @param verificationId
     * @return httpStatus 200 OK if everything went well
     */
    @RequestMapping(value = "editTestManual/{verificationId}", method = RequestMethod.POST)
    public ResponseEntity editTestManual(@PathVariable String verificationId, @RequestBody CalibrationTestManualDTO cTestManualDTO) {
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        try {
            CalibrationTestDataManual cTestDataManual = calibrationTestDataManualService.findByVerificationId(verificationId);
            CalibrationTestManual cTestManual = cTestDataManual.getCalibrationTestManual();
            calibrationTestManualService.editTestManual(cTestManualDTO.getDateOfTest(), cTestManualDTO.getNumberOfTest()
                    , cTestManualDTO.getSerialNumber(), cTestManual);
            CalibrationTestDataManualDTO cTestDataManualDTO = cTestManualDTO.getListOfCalibrationTestDataManual().get(0);
            calibrationTestDataManualService.editTestDataManual(cTestDataManualDTO.getStatusTestFirst()
                    , cTestDataManualDTO.getStatusTestSecond(), cTestDataManualDTO.getStatusTestThird()
                    , cTestDataManualDTO.getStatusCommon(), cTestDataManual);
        } catch (Exception e) {
            logger.error("faild to edit calibration test manual" + e.getMessage());
            logger.error(e); //
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    /**
     * uploads a scanDoc from chosen directory
     *
     * @param file chosen file object
     * @return httpStatus 200 OK if everything went well
     */
    @RequestMapping(value = "uploadScanDoc", method = RequestMethod.POST)
    public ResponseEntity<String> uploadScanDoc(@RequestBody MultipartFile file) {
        ResponseEntity<String> responseEntity;
        try {
            String originalFileName = file.getOriginalFilename();
            String fileType = originalFileName.substring(originalFileName.lastIndexOf('.'));
            if (Pattern.compile(contentDocExtPattern, Pattern.CASE_INSENSITIVE).matcher(fileType).matches()) {
                String uriOfscanDoc = calibrationTestManualService.uploadScanDoc(file.getInputStream(), originalFileName);
                responseEntity = new ResponseEntity(uriOfscanDoc, HttpStatus.OK);
            } else {
                logger.error("Failed to load file ");
                responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Failed to uploadScanDoc " + e.getMessage());
            logger.error(e);
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }


    /**
     * get protocol
     *
     * @param verificationId
     * @return protocol
     */
    @RequestMapping(value = "getProtocol/{verificationId}", method = RequestMethod.GET)
    public ResponseEntity getProtocol(@PathVariable String verificationId) {
        ResponseEntity<String> responseEntity;
        try {
            CalibrationTest calibrationTest = testService.findByVerificationId(verificationId);
            responseEntity = new ResponseEntity((new CalibrationTestFileDataDTO(calibrationTest, testService, verificationId)), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to get protocol" + e.getMessage());
            logger.error(e);
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    /**
     * update protocol
     *
     * @param calibrationTestFileDataDTO
     * @return httpStatus 200 OK if everything went well
     */
    @RequestMapping(value = "updateProtocol/{verificationId}", method = RequestMethod.POST)
    public ResponseEntity updateProtocol(@RequestBody CalibrationTestFileDataDTO calibrationTestFileDataDTO, @PathVariable String verificationId) {
        ResponseEntity<String> responseEntity = new ResponseEntity(HttpStatus.OK);
        try {
            CalibrationTest calibrationTest = testService.findByVerificationId(verificationId);
            calibrationTest.setCounterNumber(calibrationTestFileDataDTO.getCounterNumber());
            calibrationTest.setTestResult(calibrationTestFileDataDTO.getTestResult());
            calibrationTest.setCapacity(calibrationTestFileDataDTO.getAccumulatedVolume());
            calibrationTest.setCounterProductionYear(calibrationTestFileDataDTO.getCounterProductionYear());
            Set<CalibrationTestData> setOfTestDate = testService.getLatestTests(calibrationTest.getCalibrationTestDataList());
            List<CalibrationTestData> listOfTestDate = new ArrayList<>(setOfTestDate);
            CalibrationTestData calibrationTestData;
            for (int x = 0; x < listOfTestDate.size(); x++) {
                calibrationTestData = listOfTestDate.get(x);
                CalibrationTestDataDTO calibrationTestDataDTO = calibrationTestFileDataDTO.getListTestData().get(x);
                calibrationTestData.setInitialValue(calibrationTestDataDTO.getInitialValue());
                calibrationTestData.setEndValue(calibrationTestDataDTO.getEndValue());
                calibrationTestData.setCalculationError(calibrationTestDataDTO.getCalculationError());
                calibrationTestData.setTestResult(calibrationTestDataDTO.getTestResult());
                testDataRepository.save(calibrationTestData);
            }
            testRepository.save(calibrationTest);
            testService.updateTest(verificationId, calibrationTestFileDataDTO.getStatus());
        } catch (Exception e) {
            logger.error(e);
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }


}

