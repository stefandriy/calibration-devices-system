package com.softserve.edu.service.calibrator.data.test.impl;

import org.apache.commons.codec.binary.Base64;
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
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public CalibrationTest editTest(Long testId, String name, Integer temperature, Integer settingNumber,
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
    public void createNewCalibrationTestData(CalibrationTestData calibrationTestData) {
        dataRepository.save(calibrationTestData);
    }

    @Override
    @Transactional
    public CalibrationTest createNewCalibrationTest(Long testId, String name, Integer temperature, Integer settingNumber,
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


    public long createNewTest(DeviceTestData deviceTestData, String verificationId) throws IOException {
        Verification verification = verificationRepository.findOne(verificationId);
        CalibrationTest calibrationTest = new CalibrationTest(
                deviceTestData.getFileName(),
                deviceTestData.getTemperature(),
                (int) deviceTestData.getInstallmentNumber(), //settingNumber
                deviceTestData.getLatitude(),
                deviceTestData.getLongitude()
                );
        calibrationTest.setVerification(verification);
        testRepository.save(calibrationTest);

        String photo = deviceTestData.getTestPhoto();
        byte[] bytesOfImage = Base64.decodeBase64(photo);
        BufferedImage buffered = ImageIO.read(new ByteArrayInputStream(bytesOfImage));
        String testPhoto = "photo" + calibrationTest.getId()+"ver"+verificationId+  ".jpg";
        ImageIO.write(buffered, "jpg", new File(localStorage + "//" + testPhoto));
        calibrationTest.setPhotoPath(testPhoto);


        Set<CalibrationTestData> calibrationTestDataSet = new HashSet<>();
        Set<CalibrationTestIMG> calibrationTestIMGSet = new HashSet<>();
        CalibrationTestData сalibrationTestData;
        deviceTestData.getTestCounter();
        testRepository.save(calibrationTest);
        for (int testDataId = 1; testDataId <= 3; testDataId++) {
            Double volumeInDevice = round(deviceTestData.getTestTerminalCounterValue(testDataId) - deviceTestData.getTestInitialCounterValue(testDataId), 2);
            Double actualConsumption = convertImpulsesPerSecToCubicMetersPerHour(
                    deviceTestData.getTestCorrectedCurrentConsumption(testDataId),
                    deviceTestData.getImpulsePricePerLitre());

            сalibrationTestData = new CalibrationTestData(
                    convertImpulsesPerSecToCubicMetersPerHour(deviceTestData.getTestSpecifiedConsumption(testDataId),
                            deviceTestData.getImpulsePricePerLitre()), //givenConsumption
                    deviceTestData.getTestAllowableError(testDataId), //acceptableError
                    deviceTestData.getTestSpecifiedImpulsesAmount(testDataId) * 1.0, //volumeOfStandard
                    deviceTestData.getTestInitialCounterValue(testDataId), //initialValue
                    deviceTestData.getTestTerminalCounterValue(testDataId), //endValue
                    volumeInDevice,
                    actualConsumption,
                    countCalculationError(volumeInDevice, deviceTestData.getTestSpecifiedImpulsesAmount(testDataId) * 1.0), //calculationError
                    calibrationTest);
            calibrationTestDataSet.add(сalibrationTestData);
            dataRepository.save(сalibrationTestData);

            String beginPhoto = deviceTestData.getBeginPhoto(testDataId);
            byte[] bytesOfImages = Base64.decodeBase64(beginPhoto);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytesOfImages));
            String imageNameBegin = "beginPhoto" + calibrationTest.getId() + testDataId + ".jpg";
            ImageIO.write(bufferedImage, "jpg", new File(localStorage +"//"+ imageNameBegin));
            CalibrationTestIMG calibrationTestIMGBegin = new CalibrationTestIMG(calibrationTest, imageNameBegin + ".jpg");

            String endPhoto = deviceTestData.getEndPhoto(testDataId);
            bytesOfImages = Base64.decodeBase64(endPhoto);
            bufferedImage = ImageIO.read(new ByteArrayInputStream(bytesOfImages));
            String imageNameEnd = "endPhoto" + calibrationTest.getId() + testDataId + ".jpg";
            ImageIO.write(bufferedImage, "jpg", new File(localStorage + "//"+ imageNameEnd));
            CalibrationTestIMG calibrationTestIMGEnd = new CalibrationTestIMG(calibrationTest, imageNameEnd + ".jpg");

            calibrationTestIMGSet.add(calibrationTestIMGBegin);
            calibrationTestIMGSet.add(calibrationTestIMGEnd);
            testIMGRepository.save(calibrationTestIMGBegin);
            testIMGRepository.save(calibrationTestIMGEnd);

        }
        calibrationTest.setCalibrationTestDataSet(calibrationTestDataSet);
        testRepository.save(calibrationTest);
        return calibrationTest.getId();


    }

    private double round(double val, int scale) {
        return new BigDecimal(val).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    private double convertImpulsesPerSecToCubicMetersPerHour(double impulses, long impLitPrice) {
        return round(3.6 * impulses / impLitPrice, 3);
    }

    private double countCalculationError(double counterVolume, double standardVolume) {
        if (standardVolume < 0.0001) {
            return 0.0;
        }
        double result = (counterVolume - standardVolume) / standardVolume * 100;
        return round(result, 2);
    }
}