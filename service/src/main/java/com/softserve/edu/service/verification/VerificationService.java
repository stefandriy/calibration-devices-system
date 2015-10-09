package com.softserve.edu.service.verification;

import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.ClientData;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.service.utils.*;
import org.springframework.data.domain.Page;

import java.util.List;

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


     ListToPageTransformer<Verification> findPageOfVerificationsByCalibratorIdAndCriteriaSearch(Long calibratorId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                      String streetToSearch, String region, String district, String locality, String status, String employeeName, String sortCriteria, String sortOrder, User calibratorEmployee);


     ListToPageTransformer<Verification> findPageOfArchiveVerificationsByCalibratorId(Long organizationId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                            String streetToSearch, String status, String employeeName, Long protocolId, String protocolStatus, Long measurementDeviceId, String measurementDeviceType, String sortCriteria, String sortOrder, User calibratorEmployee);


     ListToPageTransformer<Verification> findPageOfVerificationsByVerificatorIdAndCriteriaSearch(Long verificatorId, int pageNumber, int itemsPerPage, String dateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                       String streetToSearch, String status, String employeeName, String sortCriteria, String sortOrder, User verificatorEmployee);


     ListToPageTransformer<Verification> findPageOfArchiveVerificationsByVerificatorId(Long organizationId, int pageNumber, int itemsPerPage, String dateToSearch, String idToSearch, String fullNameToSearch,
                                                                                             String streetToSearch, String status, String employeeName, String sortCriteria, String sortOrder, User verificatorEmployee);

     ListToPageTransformer<Verification> findPageOfCalibrarionTestsByVerificationId(Long organizationId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                     String streetToSearch, String status, String employeeName, Long protocolId, String protocolStatus, Long measurementDeviceId, String measurementDeviceType, String sortCriteria, String sortOrder, User calibratorEmployee);



    public Verification findByIdAndProviderId(String id, Long providerId);

    public Verification findByIdAndCalibratorId(String id, Long calibratorId);

    public Verification findByIdAndStateVerificatorId(String id, Long stateVerificatorId);

    void updateVerificationReadStatus(String verificationId, String readStatus);

    void updateVerificationStatus(String verificationId, Status status);

    void sendVerificationTo(String verificationId, Organization oraganization, Status status);

    void updateVerification(String verificationId, Organization stateVerificator);

    void updateVerificationData(String id, ClientData clientData, Organization provider);

    CalibrationTest createCalibrationTest(String verificationId, CalibrationTest data);

    CalibrationTest findByCalibrationTestId(Long id);

    int findCountOfAllSentVerifications(Organization organization);

    int findCountOfAllAcceptedVerification(Organization organization);

    List<Object[]> getProcessTimeProvider();

    List<Object[]> getProcessTimeCalibrator();

    List<Object[]> getProcessTimeVerificator();

    java.sql.Date getNewVerificationEarliestDateByProvider(Organization organization);

    java.sql.Date getArchivalVerificationEarliestDateByProvider(Organization organization);

    java.sql.Date getNewVerificationEarliestDateByCalibrator(Organization organization);

    java.sql.Date getArchivalVerificationEarliestDateByCalibrator(Organization organization);
}
