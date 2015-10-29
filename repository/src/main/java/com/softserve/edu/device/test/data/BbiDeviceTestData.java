package com.softserve.edu.device.test.data;

import java.util.Map;

/**
 * Created by Taras on 15.09.2015.
 */
public class BbiDeviceTestData implements DeviceTestData {
    private Map<String, Object> testData;

    public BbiDeviceTestData(Map<String, Object> bbiTestData)
    {
        this.testData = bbiTestData;
    }

    @Override
    public String getStringValue(String key) {
        return testData.get(key).toString();
    }

    @Override
    public Long getLongValue(String key) {
        return (Long)testData.get(key);
    }

    @Override
    public Integer getIntegerValue(String key) {
        return getLongValue(key).intValue();
    }

    @Override
    public Double getDoubleValue(String key) {
        return (Double)testData.get(key);
    }

    @Override
    public Integer getDay() {
        return getIntegerValue("day");
    }

    @Override
    public Integer getMonth() {
        return getIntegerValue("month");
    }

    @Override
    public Integer getYear() {
        return getIntegerValue("year");
    }

    @Override
    public Integer getMinute() {
        return getIntegerValue("minute");
    }

    @Override
    public Integer getSecond() {
        return getIntegerValue("second");
    }

    @Override
    public Integer getDayOfWeek() {
        return getIntegerValue("dayOfWeek");
    }

    @Override
    public Long getUnixTime() {
        return getLongValue("unixTime");
    }

    @Override
    public Integer getTemperature() {
        return getIntegerValue("temperature");
    }

    @Override
    public Long getBatteryCharge() {
        return getLongValue("batteryCharge");
    }

    @Override
    public String getFileName() {
        return getStringValue("fileName").concat(".bbi");
    }

    @Override
    public Integer getIntegrationTime() {
        return getIntegerValue("integrationTime");
    }

    @Override
    public Integer getTestCounter() {
        return getIntegerValue("testCounter");
    }

    @Override
    public Long getInstallmentNumber() {
        return getLongValue("installmentNumber");
    }

    @Override
    public String getCurrentCounterNumber() {
        return getStringValue("currentCounterNumber");
    }

    @Override
    public Double getLatitude() {
        return getDoubleValue("latitude");
    }

    @Override
    public Double getLongitude() {
        return getDoubleValue("longitude");
    }

    @Override
    public Long getImpulsePricePerLitre() {
        return getLongValue("impulsePricePerLitre");
    }

    @Override
    public Long getInitialCapacity() {
        return getLongValue("initialCapacity");
    }

    @Override
    public String getCounterType1() {
        return getStringValue("counterType1");
    }

    @Override
    public Long getTestimony() {
        return getLongValue("testimony");
    }

    @Override
    public Integer getCounterProductionYear() {
        return getIntegerValue("counterProductionYear");
    }

    @Override
    public String getCounterType2() {
        return getStringValue("counterType2");
    }

    @Override
    public Long getTestSpecifiedConsumption(int testIndex) {
        return getLongValue("test" + testIndex + "specifiedConsumption");
    }

    @Override
    public Long getTestLowerConsumptionLimit(int testIndex) {
        return getLongValue("test" + testIndex + "lowerConsumptionLimit");
    }

    @Override
    public Long getTestUpperConsumptionLimit(int testIndex) {
        return getLongValue("test" + testIndex + "upperConsumptionLimit");
    }

    @Override
    public Long getTestAllowableError(int testIndex) {
        return getLongValue("test" + testIndex + "allowableError");
    }

    @Override
    public Long getTestSpecifiedImpulsesAmount(int testIndex) {
        return getLongValue("test" + testIndex + "specifiedImpulsesAmount");
    }

    @Override
    public Double getTestCorrectedCumulativeImpulsesValue(int testIndex) {
        return getDoubleValue("test" + testIndex + "correctedCumulativeImpulsesValue");
    }

    @Override
    public Double getTestCorrectedCurrentConsumption(int testIndex) {
        return getDoubleValue("test" + testIndex + "correctedCurrentConsumption");
    }

    @Override
    public Double getTestCumulativeImpulsesValueWithoutCorrection(int testIndex) {
        return getDoubleValue("test" + testIndex + "cumulativeImpulsesValueWithoutCorrection");
    }

    @Override
    public Double getTestCurrentConsumptionWithoutCorrection(int testIndex) {
        return getDoubleValue("test" + testIndex + "currentConsumptionWithoutCorrection");
    }

    @Override
    public Long getTestEstimatedError(int testIndex) {
        return getLongValue("test" + testIndex + "estimatedError");
    }

    @Override
    public Double getTestInitialCounterValue(int testIndex) {
        return getDoubleValue("test" + testIndex + "initialCounterValue");
    }

    @Override
    public Double getTestTerminalCounterValue(int testIndex) {
        return getDoubleValue("test" + testIndex + "terminalCounterValue");
    }

    @Override
    public Long getTestUnixBeginTime(int testIndex) {
        return getLongValue("test" + testIndex + "unixTestBeginTime");
    }

    @Override
    public Long getTestUnixEndTime(int testIndex) {
        return getLongValue("test" + testIndex + "unixTestEndTime");
    }

    @Override
    public Double getTestDuration(int testIndex) {
        return getDoubleValue("test" + testIndex + "testDuration");
    }

    @Override
    public Long getTestCorrectionFactor(int testIndex) {
        return getLongValue("test" + testIndex + "correctionFactor");
    }

    @Override
    public Long getTestMinConsumptionLimit(int testIndex) {
        return getLongValue("test" + testIndex + "minConsumptionLimit");
    }

    @Override
    public Long getTestMaxConsumptionLimit(int testIndex) {
        return getLongValue("test" + testIndex + "maxConsumptionLimit");
    }

    @Override
    public Integer getTestNumber(int testIndex) {
        return getIntegerValue("test" + testIndex + "testNumber");
    }

    /**
     *
     * @return Test photo in hex.
     */
    @Override
    public String getTestPhoto() {
        return getStringValue("testPhoto");
    }

    /**
     *
     * @param testIndex Index of test.
     * @return Test begin photo in base64.
     */
    @Override
    public String getBeginPhoto(int testIndex) {
        return getStringValue("test" + testIndex + "beginPhoto");
    }

    /**
     *
     * @param testIndex Index of test.
     * @return Test end photo in base64.
     */
    @Override
    public String getEndPhoto(int testIndex) {
        return getStringValue("test" + testIndex + "endPhoto");
    }


}
