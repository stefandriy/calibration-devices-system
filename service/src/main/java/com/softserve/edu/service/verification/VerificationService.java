package com.softserve.edu.service.verification;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.ClientData;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.service.utils.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface VerificationService {

    void saveVerification(Verification verification);

    Verification findById(String code);

    Page<Verification> findPageOfAllVerificationsByProviderId(Long providerId, int pageNumber,
                                                              int itemsPerPage);

    Page<Verification> findPageOfAllVerificationsByCalibratorId(Long calibratorId, int pageNumber,
                                                                int itemsPerPage);

    Page<Verification> findPageOfAllVerificationsByStateVerificatorId(Long stateVerificatorId, int pageNumber,
                                                                      int itemsPerPage);

    Long findCountOfNewVerificationsByCalibratorId(Long calibratorId);

    Long findCountOfNewVerificationsByProviderId(Long providerId);

    Long findCountOfNewVerificationsByStateVerificatorId(Long stateVerificatorId);

    Page<Verification> findPageOfSentVerificationsByProviderId(Long providerId, int pageNumber, int itemsPerPage);

    Page<Verification> findPageOfSentVerificationsByCalibratorId(Long calibratorId, int pageNumber, int itemsPerPage);

    Page<Verification> findPageOfSentVerificationsByStateVerificatorId(Long stateVerificatorId, int pageNumber, int itemsPerPage);

    ListToPageTransformer<Verification> findPageOfSentVerificationsByProviderIdAndCriteriaSearch(Long providerId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                 String streetToSearch, String region, String district, String locality, String status, String employeeName, String sortCriteria, String sortOrder, User providerEmployee);

    ListToPageTransformer<Verification> findPageOfArchiveVerificationsByProviderId(Long organizationId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                   String streetToSearch, String region, String district, String locality, String status, String employeeName, String sortCriteria, String sortOrder, User providerEmployee);

    ListToPageTransformer<Verification> findPageOfArchiveVerificationsByProviderIdOnMainPanel(Long organizationId, int pageNumber, int itemsPerPage, String initialDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                              String streetToSearch, String region, String district, String locality, String status, String employeeName, User providerEmployee);

    ListToPageTransformer<Verification> findPageOfArchiveVerificationsByCalibratorIdOnMainPanel(Long organizationId, int pageNumber, int itemsPerPage, String initialDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                String streetToSearch, String region, String district, String locality, String status, String employeeName, User providerEmployee);

    ListToPageTransformer<Verification> findPageOfArchiveVerificationsByVerificatorIdOnMainPanel(Long organizationId, int pageNumber, int itemsPerPage, String initialDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                 String streetToSearch, String region, String district, String locality, String status, String employeeName, User stateVerificatorEmployee);


    ListToPageTransformer<Verification> findPageOfVerificationsByCalibratorIdAndCriteriaSearch(Long calibratorId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                               String streetToSearch, String region, String district, String locality, String status, String employeeName, String standardSize, String symbol, String nameProvider, String realiseYear, String dismantled, String building, String sortCriteria, String sortOrder, User calibratorEmployee, List<Map<String, String>> globalSearchParams);


    ListToPageTransformer<Verification> findPageOfArchiveVerificationsByCalibratorId(Long organizationId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                     String streetToSearch, String status, String employeeName, Long protocolId, String protocolStatus, Long measurementDeviceId, String measurementDeviceType, String sortCriteria, String sortOrder, User calibratorEmployee);


    ListToPageTransformer<Verification> findPageOfVerificationsByVerificatorIdAndCriteriaSearch(Long verificatorId, int pageNumber, int itemsPerPage, String dateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                String streetToSearch, String status, String employeeName, String nameProvider, String nameCalibrator, String lastName, String firstName, String middleName, String district, String building, String flat, String sortCriteria, String sortOrder, User verificatorEmployee);


    ListToPageTransformer<Verification> findPageOfArchiveVerificationsByVerificatorId(Long organizationId, int pageNumber, int itemsPerPage, String dateToSearch, String idToSearch, String fullNameToSearch,
                                                                                      String streetToSearch, String status, String employeeName, String sortCriteria, String sortOrder, User verificatorEmployee);

    ListToPageTransformer<CalibrationTest> findPageOfCalibrationTestsByVerificationId(int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String name, String region, String district, String locality, String streetToSearch, String idToSearch, String fullNameToSearch, Integer settingNumber, String consumptionStatus,
                                                                                      Long protocolId, String testResult, Long measurementDeviceId, String measurementDeviceType, String sortCriteria, String sortOrder);


    Verification findByIdAndProviderId(String id, Long providerId);

    Verification findByIdAndCalibratorId(String id, Long calibratorId);

    Verification findByIdAndStateVerificatorId(String id, Long stateVerificatorId);

    void updateVerificationReadStatus(String verificationId, String readStatus);

    void updateVerificationStatus(String verificationId, Status status);

    void sendVerificationTo(String verificationId, Organization oraganization, Status status);

    void updateVerification(String verificationId, Organization stateVerificator);

    void updateVerificationData(String id, ClientData clientData, Organization provider);

    CalibrationTest createCalibrationTest(String verificationId, CalibrationTest data);

    CalibrationTest findByCalibrationTestId(Long id);

    int findCountOfAllSentVerifications(Organization organization);

    int findCountOfAllAcceptedVerification(Organization organization);

    int findCountOfAllCalibratorVerificationWithoutEmployee(Organization organization);

    int findCountOfAllCalibratorVerificationWithEmployee(Organization organization);

    int findCountOfAllVerificatorVerificationWithoutEmployee(Organization organization);

    int findCountOfAllVerificatorVerificationWithEmployee(Organization organization);

    List<Object[]> getProcessTimeProvider();

    List<Object[]> getProcessTimeCalibrator();

    List<Object[]> getProcessTimeVerificator();

    java.sql.Date getNewVerificationEarliestDateByProvider(Organization organization);

    java.sql.Date getArchivalVerificationEarliestDateByProvider(Organization organization);

    java.sql.Date getNewVerificationEarliestDateByCalibrator(Organization organization);

    java.sql.Date getArchivalVerificationEarliestDateByCalibrator(Organization organization);

    java.sql.Date getEarliestPlanningTaskDate(Organization organization);

    Page<Verification> getVerificationsByTaskID(Long taskID, Pageable pageable);

    void removeVerificationFromTask(String verificationId);

    List<Verification> findPageOfVerificationsByCalibratorEmployeeAndStatus(User employee, int pageNumber,
                                                                            int itemsPerPage, Status status);
    Long countByCalibratorEmployeeUsernameAndStatus(User calibratorEmployee, Status status);

    List<Verification> findPageOfVerificationsByProviderIdAndStatus(Organization provider, int pageNumber,
                                                                  int itemsPerPage, Status status);
    Long countByProviderAndStatus(Organization provider,Status status);

    void returnVerificationToCalibratorFromProvider(String verificationId, String rejectMessage);
    
    void editCounter(String verificationId, String deviceName, Boolean dismantled, Boolean sealPresence, Long dateOfDismantled,
                     Long dateOfMounted, String numberCounter, String releaseYear, String symbol, String standardSize,
                     String comment, Long deviceId);

    void editAddInfo(int entrance, int doorCode, int floor, Long dateOfVerif, String timeFrom, String timeTo, Boolean serviceability,
                     Long noWaterToDate, String notes, String verificationId);

    void editClientInfo(String verificationDTO, ClientData clientData);

    Set<String> findAllSymbols(Long deviceId);

    Set<String> findStandardSizesBySymbolAndDeviceId(String symbol, Long deviceId);

    CounterType findOneBySymbolAndStandardSizeAndDeviceId(String symbol, String standardSize, Long deviceId);

    String getNewVerificationDailyIdByDeviceType(Date date, Device.DeviceType deviceType);

    Long findCountOfNewNotStandardVerificationsByCalibratorId(Long calibratorId);

    Long findCountOfNotStandardNewVerificationsByProviderId(Long providerId);

    Set<String> findSymbolsByDeviceType (String deviceType);

    Set<String> findStandardSizesBySymbolAndDeviceType(String symbol, String deviceType);
}
