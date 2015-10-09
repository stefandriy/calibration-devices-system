package com.softserve.edu.device.test.data;

/**
 * Created by Taras on 15.09.2015.
 */
public interface DeviceTestData {
    String getStringValue(String key);
    Long getLongValue(String key);
    Integer getIntegerValue(String key);
    Double getDoubleValue(String key);

    Integer getDay();
    Integer getMonth();
    Integer getYear();
    Integer getMinute();
    Integer getSecond();
    Integer getDayOfWeek();
    Long getUnixTime();
    Integer getTemperature();
    Long getBatteryCharge();
    String getFileName();
    Integer getIntegrationTime();
    Integer getTestCounter();
    Long getInstallmentNumber();
    String getCurrentCounterNumber();
    Double getLatitude();
    Double getLongitude();
    Long getImpulsePricePerLitre();
    Long getInitialCapacity();
    String getCounterType1();
    Long getTestimony();
    Integer getCounterProductionYear();
    String getCounterType2();

    Long getTestSpecifiedConsumption(int testIndex);
    Long getTestLowerConsumptionLimit(int testIndex);
    Long getTestUpperConsumptionLimit(int testIndex);
    Long getTestAllowableError(int testIndex);
    Long getTestSpecifiedImpulsesAmount(int testIndex);
    Long getTestCorrectedCumulativeImpulsesValue(int testIndex);
    Long getTestCorrectedCurrentConsumption(int testIndex);
    Long getTestCumulativeImpulsesValueWithoutCorrection(int testIndex);
    Long getTestCurrentConsumptionWithoutCorrection(int testIndex);
    Long getTestEstimatedError(int testIndex);
    Double getTestInitialCounterValue(int testIndex);
    Double getTestTerminalCounterValue(int testIndex);
    Long getTestUnixBeginTime(int testIndex);
    Long getTestUnixEndTime(int testIndex);

    /**
     * Returns test duration in seconds
     * @param testIndex
     * @return
     */
    Double getTestDuration(int testIndex);
    Long getTestCorrectionFactor(int testIndex);
    Long getTestMinConsumptionLimit(int testIndex);
    Long getTestMaxConsumptionLimit(int testIndex);
    Integer getTestNumber(int testIndex);

    String getTestPhoto();
    String getBeginPhoto(int testIndex);
    String getEndPhoto(int testIndex);
}
