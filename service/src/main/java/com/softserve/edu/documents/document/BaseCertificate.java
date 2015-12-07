package com.softserve.edu.documents.document;

import com.softserve.edu.common.Constants;
import com.softserve.edu.documents.document.meta.Placeholder;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.ClientData;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a base certificate and consists of common columns and methods
 * of all certificates.
 * All certificates extend this class.
 */
public abstract class BaseCertificate implements Document {
    /**
     * Verification that is used for getting information for this document.
     */
    private Verification verification;
    /**
     * One of calibration test that is assigned to the verification.
     */
    private CalibrationTest calibrationTest;

    public BaseCertificate () {}

    /**
     * Constructor.
     *
     * @param documentType    documentType file to be used for writing this document.
     * @param verification    entity to get document's data from.
     * @param calibrationTest one of calibration test that is assigned to the verification
     */
    public BaseCertificate(Verification verification, CalibrationTest calibrationTest) {
        super();

        setVerification(verification);
        setCalibrationTest(calibrationTest);
    }

    /**
     * @return the calibrator company's name.
     */
    @Placeholder(name = "CALIBRATOR_COMPANY_NAME")
    public String getCalibratorCompanyName() {
        return getVerification().getCalibrator().getName();
    }

    /**
     * @return the state verificator company's name.
     */
    @Placeholder(name = "VERIFICATOR_COMPANY_NAME")
    public String getStateVerificatorCompanyName() {
        return getVerification().getStateVerificator().getName();
    }

    /**
     * @return the calibrator's address.
     */
    @Placeholder(name = "CALIBRATOR_COMPANY_ADDRESS")
    public String getCalibratorCompanyAddress() {
        Address address = getVerification().getCalibrator().getAddress();

        String addresToReturn = address.getLocality() + ", " +
                address.getStreet() + ", " +
                address.getBuilding();

        return addresToReturn;
    }

    /**
     * @return the calibrator company's certificate identification number.
     */
    @Placeholder(name = "CALIBRATOR_ACC_CERT_NAME")
    public String getCalibratorCompanyAccreditationCertificateNumber() {
        return getVerification().getCalibrator().getCertificateNumber();
    }

    /**
     * @return the date when the calibrator company received the certificate, that allows
     * it to.
     */
    @Placeholder(name = "CALIBRATOR_ACC_CERT_DATE_GRANTED")
    public String getCalibratorCompanyAccreditationCertificateGrantedDate() {
        Date certificateGrantedDate = getVerification().getCalibrator().getCertificateGrantedDate();
        String dateFormated = new SimpleDateFormat(Constants.YEAR_MONTH_DAY).format(certificateGrantedDate);
        return dateFormated;
    }

    /**
     * @return Returns the identification number of the accreditation certificate,
     * that the calibrator's company owns.
     */
    @Placeholder(name = "VERIFICATION_CERTIFICATE_NUMBER")
    public String getVerificationCertificateNumber() {
        String teamId = String.valueOf(getVerification().getTask().getTeam().getId());
        String testId = String.valueOf(getCalibrationTest().getId());
        return teamId + "-" + testId;
    }

    /**
     * @return the device's name
     */
    @Placeholder(name = "DEV_NAME")
    public String getDeviceName() {
        return getVerification().getCounter().getCounterType().getName();
    }

    /**
     * @return the device's sign
     */
    @Placeholder(name = "DEV_SIGN")
    public String getDeviceSign() {
        return getVerification().getCounter().getCounterType().getSymbol();
    }

    /**
     * @return the device's manufacturer serial number
     */
    @Placeholder(name = "DEV_MAN_SER")
    public String getDeviceManufacturerSerial() {
        return getVerification().getCounter().getNumberCounter();
    }

    /**
     * @return the device's manufacturer name
     */
    @Placeholder(name = "MAN_NAME")
    public String getManufacturerName() {
        return getVerification().getCounter().getCounterType().getManufacturer();
    }

    /**
     * @return the owner's full name - surName + name + middleName
     */
    @Placeholder(name = "OWNER_NAME")
    public String getOwnerFullName() {
        ClientData ownerData = getVerification().getClientData();

        String fullName = ownerData.getLastName() + " " +
                ownerData.getFirstName() + " " +
                ownerData.getMiddleName();

        return fullName;
    }

    /**
     * @return the state verificator's name in Surname N.M., where N - first letter of name,
     * M - first letter of middle name.
     */
    @Placeholder(name = "VERIFICATOR_SHORT_NAME")
    public String getStateVerificatorShortName() {
        User stateVerificatorEmployee = getVerification().getStateVerificatorEmployee();

        String fullName = stateVerificatorEmployee.getLastName() + " "
                + stateVerificatorEmployee.getFirstName();

        return fullName;
    }

    /**
     * @return the date until this verification certificate is effective.
     */
    @Placeholder(name = "EFF_DATE")
    public String getVerificationCertificateEffectiveUntilDate() {
        return getVerification().getExpirationDate().toString();
    }

    /**
     * @return the date of CalibrationTest.
     */
    @Placeholder(name = "PROTOCOL_DATE")
    public String getCalibrationTestDate () {
        return new SimpleDateFormat(Constants.YEAR_MONTH_DAY).format(getCalibrationTest().getDateTest());
    }

    private void setVerification(Verification verification) {
        this.verification = verification;
    }

    protected Verification getVerification() {
        return verification;
    }

    public CalibrationTest getCalibrationTest() {
        return calibrationTest;
    }

    protected void setCalibrationTest(CalibrationTest calibrationTest) {
        this.calibrationTest = calibrationTest;
    }
}
