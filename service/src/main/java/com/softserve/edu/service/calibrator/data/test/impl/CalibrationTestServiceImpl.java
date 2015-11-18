package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestDataService;
import org.apache.commons.codec.binary.Base64;
import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;


@Service
public class CalibrationTestServiceImpl implements CalibrationTestService {

    @Value("${photo.storage.local}")
    private String localStorage;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CalibrationTestRepository testRepository;
    @Autowired
    private CalibrationTestDataService testDataService;
    @Autowired
    private CalibrationTestDataRepository dataRepository;
    @Autowired
    private VerificationRepository verificationRepository;

    private Logger logger = Logger.getLogger(CalibrationTestServiceImpl.class);

    public long createNewTest(DeviceTestData deviceTestData, String verificationId) throws IOException {
        Verification verification = verificationRepository.findOne(verificationId);
        CalibrationTest calibrationTest = new CalibrationTest(deviceTestData.getFileName(), deviceTestData.getInstallmentNumber(), deviceTestData.getLatitude(),  deviceTestData.getLongitude(),
                deviceTestData.getUnixTime(),deviceTestData.getCurrentCounterNumber(),Verification.ConsumptionStatus.IN_THE_AREA,
                Verification.CalibrationTestResult.SUCCESS, verification, deviceTestData.getInitialCapacity());
       testRepository.save(calibrationTest);
        String photo = deviceTestData.getTestPhoto();
        byte[] bytesOfImage = Base64.decodeBase64(photo);
        BufferedImage buffered = ImageIO.read(new ByteArrayInputStream(bytesOfImage));
        String testPhoto = "mainPhoto" + calibrationTest.getId() + verificationId + ".jpg";
        ImageIO.write(buffered, "jpg", new File(localStorage + "//" + testPhoto));
        calibrationTest.setPhotoPath(testPhoto);
        testRepository.save(calibrationTest);
        CalibrationTestData сalibrationTestData;
        for (int testDataId = 1; testDataId <= 6; testDataId++) {
             /*if there is no photo there is now test data */
            if (deviceTestData.getBeginPhoto(testDataId).equals("")) {
               continue;
            } else {
                сalibrationTestData = testDataService.createNewTestData(calibrationTest.getId(), deviceTestData, testDataId);
                if (сalibrationTestData.getTestResult() == Verification.CalibrationTestResult.FAILED) {
                    calibrationTest.setTestResult(Verification.CalibrationTestResult.FAILED);
                    testRepository.save(calibrationTest);
                }
                if (сalibrationTestData.getConsumptionStatus() == Verification.ConsumptionStatus.NOT_IN_THE_AREA) {
                    calibrationTest.setConsumptionStatus(Verification.ConsumptionStatus.NOT_IN_THE_AREA);
                    testRepository.save(calibrationTest);
                }

            }
        }
        verification.setStatus(Status.TEST_COMPLETED);
        verificationRepository.save(verification);
        return calibrationTest.getId();
    }


    @Transactional
    public CalibrationTest findTestById(Long testId) {
        return testRepository.findOne(testId);
    }

    @Transactional
    public CalibrationTest findByVerificationId(String verifId) {
        return testRepository.findByVerificationId(verifId);
    }

    @Transactional
    public CalibrationTestList findAllCalibrationTests() {
        List<CalibrationTest> list = (ArrayList<CalibrationTest>) testRepository.findAll();
        return new CalibrationTestList(list);
    }

    @Override
    @Transactional
    public Page<CalibrationTest> getCalibrationTestsBySearchAndPagination(int pageNumber, int itemsPerPage, String search) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return search.equalsIgnoreCase("null") ? testRepository.findAll(pageRequest) : testRepository.findByNameLikeIgnoreCase("%" + search + "%", pageRequest);
    }

    @Override
    @Transactional
    public CalibrationTest editTest(Long testId, String name, Integer capacity, Integer settingNumber,
                                    Double latitude, Double longitude, Verification.ConsumptionStatus consumptionStatus, Verification.CalibrationTestResult testResult) {
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testResult = Verification.CalibrationTestResult.SUCCESS;
        calibrationTest.setName(name);
        calibrationTest.setCapacity(capacity);
        calibrationTest.setSettingNumber(settingNumber);
        calibrationTest.setLatitude(latitude);
        calibrationTest.setLongitude(longitude);
        calibrationTest.setConsumptionStatus(consumptionStatus);
        calibrationTest.setTestResult(testResult);
        return calibrationTest;
    }

    @Override
    @Transactional
    public void deleteTest(Long testId) {
        testRepository.delete(testId);
    }

    @Override
    @Transactional
    public void createTestData(Long testId, CalibrationTestData testData) {
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testData.setCalibrationTest(calibrationTest);
        dataRepository.save(testData);
    }

    @Override
    @Transactional
    public CalibrationTestDataList findAllTestDataAsociatedWithTest(Long calibrationTestId) {
        CalibrationTest calibrationTest = testRepository.findOne(calibrationTestId);
        if (calibrationTest == null) {
            throw new NotAvailableException("Тесту з таким ID не існує!");
        } else {
            return new CalibrationTestDataList(calibrationTestId
                    , dataRepository.findByCalibrationTestId(calibrationTestId));
        }
    }

    public String getPhotoAsString(String photoPath) {
        String photo = null;
        InputStream reader = null;
        BufferedImage image = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            reader = new FileInputStream("/Metrology/img/" + photoPath);
            bufferedInputStream = new BufferedInputStream(reader);
            image = ImageIO.read(bufferedInputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] bytesOfImages = Base64.encodeBase64(baos.toByteArray());
            photo = new String(bytesOfImages);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                bufferedInputStream.close();
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return photo;
    }

    @Override
    @Transactional
    public void uploadPhotos(InputStream file, Long idCalibrationTest, String originalFileFullName) throws IOException {
        String fileType = originalFileFullName.substring(originalFileFullName.lastIndexOf('.') + 1);
        byte[] bytesOfImages = IOUtils.toByteArray(file);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytesOfImages));
        ImageIO.write(bufferedImage, fileType, new File(localStorage, originalFileFullName));

        CalibrationTest calibrationTest = testRepository.findOne(idCalibrationTest);
//        CalibrationTestIMG calibrationTestIMG = new CalibrationTestIMG(calibrationTest, originalFileFullName);
//        testIMGRepository.save(calibrationTestIMG);
    }

   /* @Override
    @Transactional
    public CalibrationTest createEmptyTest(String verificationId) {
        Verification verification = verificationRepository.findOne(verificationId);
        CalibrationTest calibrationTest = new CalibrationTest();
        calibrationTest.setVerification(verification);
        testRepository.save(calibrationTest);
        return calibrationTest;
    }*/

    @Override
    @Transactional
    public void createNewCalibrationTestData(CalibrationTestData calibrationTestData) {
        dataRepository.save(calibrationTestData);
    }

    @Override
    @Transactional
    public CalibrationTest createNewCalibrationTest(Long testId, String name, Integer capacity, Integer settingNumber,
                                                    Double latitude, Double longitude) {
        Verification.CalibrationTestResult testResult;
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testResult = Verification.CalibrationTestResult.SUCCESS;
        Date initial = new Date();
        calibrationTest.setName(name);
        calibrationTest.setDateTest(initial);
        calibrationTest.setCapacity(capacity);
        calibrationTest.setSettingNumber(settingNumber);
        calibrationTest.setLatitude(latitude);
        calibrationTest.setLongitude(longitude);
        calibrationTest.setConsumptionStatus(Verification.ConsumptionStatus.IN_THE_AREA);
        calibrationTest.setTestResult(testResult);
        return calibrationTest;
    }


}