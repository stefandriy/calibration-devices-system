package com.softserve.edu.service;

import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.entity.CalibrationTestIMG;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.util.CalibrationTestResult;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestIMGRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CalibrationTestService {

    @Value("${photo.storage.local}")
    private String localStorage;

    @Autowired
    private CalibrationTestRepository testRepository;

    @Autowired
    private CalibrationTestIMGRepository testIMGRepository;

    @Autowired
    private CalibrationTestDataRepository dataRepository;

    @Autowired
    private VerificationRepository verificationRepository;

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
        CalibrationTestList foundTests = new CalibrationTestList(list);
        return foundTests;
    }

    @Transactional

    public Page<CalibrationTest> getCalibrationTestsBySearchAndPagination(int pageNumber, int itemsPerPage, String search) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return search.equalsIgnoreCase("null") ? testRepository.findAll(pageRequest) : testRepository.findByNameLikeIgnoreCase("%" + search + "%", pageRequest);
    }

    @Transactional
    public void createNewTest(CalibrationTest calibrationTest, Date date, String verificationId) {
        Verification verification = verificationRepository.findOne(verificationId);
        calibrationTest.setVerification(verification);
        calibrationTest.setDateTest(date);
        testRepository.save(calibrationTest);
    }

    @Transactional
    public CalibrationTest editTest(Long testId, String name, Integer temperature, Integer settingNumber,
                                    Double latitude, Double longitude, String consumptionStatus, CalibrationTestResult testResult) {
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testResult = CalibrationTestResult.SUCCESS;
        calibrationTest.setName(name);
        calibrationTest.setTemperature(temperature);
        calibrationTest.setSettingNumber(settingNumber);
        calibrationTest.setLatitude(latitude);
        calibrationTest.setLongitude(longitude);
        calibrationTest.setConsumptionStatus(consumptionStatus);
        calibrationTest.setTestResult(testResult);
        return calibrationTest;
    }

    @Transactional
    public void deleteTest(Long testId) {
        testRepository.delete(testId);
    }

    @Transactional
    public void createTestData(Long testId, CalibrationTestData testData) {
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testData.setCalibrationTest(calibrationTest);
        dataRepository.save(testData);
    }

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

    @Transactional
    public void uploadPhotos(InputStream file, Long idCalibrationTest, String originalFileFullName) throws IOException {
        String fileType = originalFileFullName.substring(originalFileFullName.lastIndexOf('.') + 1);
        byte[] bytesOfImages = IOUtils.toByteArray(file);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytesOfImages));
        ImageIO.write(bufferedImage, fileType, new File(localStorage, originalFileFullName));

        CalibrationTest calibrationTest = testRepository.findOne(idCalibrationTest);
        CalibrationTestIMG calibrationTestIMG = new CalibrationTestIMG(calibrationTest, originalFileFullName);
        testIMGRepository.save(calibrationTestIMG);
    }


    @Transactional
    public void createEmptyTest(String verificationId) {
        Verification verification = verificationRepository.findOne(verificationId);
        CalibrationTest calibrationTest = new CalibrationTest();
        calibrationTest.setVerification(verification);
        testRepository.save(calibrationTest);
    }

    @Transactional
    public void createNewCalibrationTestData(CalibrationTestData calibrationTestData){
        dataRepository.save(calibrationTestData);
    }

    @Transactional
    public void createNewCalibrationTest(CalibrationTest calibrationTest){
        testRepository.save(calibrationTest);
    }

}