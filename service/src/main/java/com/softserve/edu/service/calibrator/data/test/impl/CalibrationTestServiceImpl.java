package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestIMG;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestIMGRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CalibrationTestServiceImpl implements CalibrationTestService {

    @Value("${photo.storage.local}")
    private String localStorage;

    @PersistenceContext
    private EntityManager em;

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
    public void createNewTest(CalibrationTest calibrationTest, Date date, String verificationId) {
        Verification verification = verificationRepository.findOne(verificationId);
        calibrationTest.setVerification(verification);
        calibrationTest.setDateTest(date);
        testRepository.save(calibrationTest);
    }

    @Override
    @Transactional
    public CalibrationTest editTest(Long testId, String name, Integer temperature, Long settingNumber,
                                    Double latitude, Double longitude, Verification.ConsumptionStatus consumptionStatus, Verification.CalibrationTestResult testResult) {
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testResult = Verification.CalibrationTestResult.SUCCESS;
        calibrationTest.setName(name);
        calibrationTest.setTemperature(temperature);
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

    @Override
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

    @Override
    @Transactional
    public CalibrationTest createEmptyTest(String verificationId) {
        Verification verification = verificationRepository.findOne(verificationId);
        CalibrationTest calibrationTest = new CalibrationTest();
        calibrationTest.setVerification(verification);
        testRepository.save(calibrationTest);
        return calibrationTest;
    }

    @Override
    @Transactional
    public void createNewCalibrationTestData(CalibrationTestData calibrationTestData){
        dataRepository.save(calibrationTestData);
    }

    @Override
    @Transactional
    public CalibrationTest createNewCalibrationTest(Long testId, String name, Integer temperature, Long settingNumber,
                                    Double latitude, Double longitude) {
        Verification.CalibrationTestResult testResult;
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testResult = Verification.CalibrationTestResult.SUCCESS;
        Date initial = new Date();
        calibrationTest.setName(name);
        calibrationTest.setDateTest(initial);
        calibrationTest.setTemperature(temperature);
        calibrationTest.setSettingNumber(settingNumber);
        calibrationTest.setLatitude(latitude);
        calibrationTest.setLongitude(longitude);
        calibrationTest.setConsumptionStatus(Verification.ConsumptionStatus.IN_THE_AREA);
        calibrationTest.setTestResult(testResult);
        return calibrationTest;
    }

    @Override
    @Transactional
    public void saveCalibrationTestFromParser(DeviceTestData deviceTestData) {
        CalibrationTest calibrationTest = new CalibrationTest();
        calibrationTest.setName(deviceTestData.getFileName());
        calibrationTest.setTemperature(deviceTestData.getTemperature());
        calibrationTest.setSettingNumber(deviceTestData.getInstallmentNumber());
        calibrationTest.setLatitude(deviceTestData.getLatitude());
        calibrationTest.setLongitude(deviceTestData.getLongitude());
        calibrationTest.setConsumptionStatus(Verification.ConsumptionStatus.IN_THE_AREA);
        calibrationTest.setTestResult(Verification.CalibrationTestResult.SUCCESS);
       /* CalibrationTest calibrationTest = new CalibrationTest(
                deviceTestData.getFileName(), //name
                deviceTestData.getTemperature(),//temperature
                deviceTestData.getInstallmentNumber(), //settingNumber
                deviceTestData.getLatitude(),//latitude
                deviceTestData.getLongitude(), //longitude
                Verification.ConsumptionStatus.IN_THE_AREA,
                Verification.CalibrationTestResult.SUCCESS
        );*/
        testRepository.save(calibrationTest);
    }

}