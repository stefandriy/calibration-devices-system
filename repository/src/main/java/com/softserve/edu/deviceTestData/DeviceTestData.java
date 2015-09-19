package com.softserve.edu.deviceTestData;

/**
 * Created by Taras on 15.09.2015.
 */
public interface DeviceTestData {
    String getStringValue(String key);
    Long getLongValue(String key);
    Integer getIntegerValue(String key);

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
    Long getLatitude();
    Long getLongitude();
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
    Long getTestInitialCounterValue(int testIndex);
    Long getTestTerminalCounterValue(int testIndex);
    Long getTestUnixBeginTime(int testIndex);
    Long getTestUnixEndTime(int testIndex);
    Long getTestDuration(int testIndex);
    Long getTestCorrectionFactor(int testIndex);
    Long getTestMinConsumptionLimit(int testIndex);
    Long getTestMaxConsumptionLimit(int testIndex);
    Long getTestNumber(int testIndex);

    String getTestPhoto();
    String getBeginPhoto(int testIndex);
    String getEndPhoto(int testIndex);
}
