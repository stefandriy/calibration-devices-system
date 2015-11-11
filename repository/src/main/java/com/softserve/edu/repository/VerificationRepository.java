package com.softserve.edu.repository;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.verification.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VerificationRepository extends PagingAndSortingRepository<Verification, String> {
    Page<Verification> findByProviderId(Long providerId, Pageable pageable);
    Page<Verification> findByCalibratorId(Long calibratorId, Pageable pageable);
    
    Page<Verification> findByProviderIdAndStatusOrderByInitialDateDesc(Long providerId, Status status, Pageable pageable);
    Page<Verification> findByCalibratorIdAndStatusOrderByInitialDateDesc(Long calibratorId, Status status, Pageable pageable);
    Page<Verification> findByStateVerificatorIdAndStatusOrderByInitialDateDesc(Long stateVerificatorId, Status status, Pageable pageable);

    Page<Verification> findByStateVerificatorId(Long stateVerificatorId, Pageable pageable);
    
    Page<Verification> findByProviderIdAndStatus(Long providerId, Status status, Pageable pageable);
    Page<Verification> findByCalibratorIdAndStatus(Long calibratorId, Status status, Pageable pageable);
    Page<Verification> findByStateVerificatorIdAndStatus(Long stateVerificatorId, Status status, Pageable pageable);

    //search methods for calibrator
    Page<Verification> findByCalibratorIdAndStatusAndIdLikeIgnoreCase(Long calibratorId, Status status, String search, Pageable pageable);
    Page<Verification> findByCalibratorIdAndStatusAndInitialDateLike(Long calibratorId, Status status, Date date, Pageable pageable);
    Page<Verification> findByCalibratorIdAndStatusAndClientData_lastNameLikeIgnoreCase(Long calibratorId, Status status, String search, Pageable pageable);
    Page<Verification> findByCalibratorIdAndStatusAndClientDataClientAddressStreetLikeIgnoreCase(Long calibratorId, Status status, String search, Pageable pageable);
    
    // search methods for provider
    Page<Verification> findByProviderIdAndStatusAndIdLikeIgnoreCase(Long providerId, Status status, String search, Pageable pageable);
    Page<Verification> findByProviderIdAndStatusAndInitialDate(Long providerId, Status status, Date date, Pageable pageable);
    Page<Verification> findByProviderIdAndStatusAndClientData_lastNameLikeIgnoreCase(Long providerId, Status status, String search, Pageable pageable);
    Page<Verification> findByProviderIdAndStatusAndClientDataClientAddressStreetLikeIgnoreCase(Long providerId, Status status, String search, Pageable pageable);
    // search methods for verificator
    Page<Verification> findByStateVerificatorIdAndStatusAndIdLikeIgnoreCase(Long stateVerificatorId, Status status, String search, Pageable pageable);
    Page<Verification> findByStateVerificatorIdAndStatusAndInitialDateLike(Long stateVerificatorId, Status status, Date date, Pageable pageable);
    Page<Verification> findByStateVerificatorIdAndStatusAndClientData_lastNameLikeIgnoreCase(Long stateVerificatorId, Status status, String search, Pageable pageable);
    Page<Verification> findByStateVerificatorIdAndStatusAndClientDataClientAddressStreetLikeIgnoreCase(Long stateVerificatorId, Status status, String search, Pageable pageable);

    /**
     * This method serves for security purpose. When provider employee(or admin) makes GET request
     * for any verification he can only get it if id of organization and provider employee matches.
     * Otherwise(if returned null) AccessDeniedException will be thrown.
     *
     * @param id         Id of verification.
     * @param providerId Provider organization id.
     * @return Verification object that match provided query or null if no matches found.
     */
    Verification findByIdAndProviderId(String id, Long providerId);
    Verification findByIdAndCalibratorId(String id, Long providerId);
    Verification findByIdAndStateVerificatorId(String id, Long stateVerificatorId);
    
    Verification findOne(String id);
    
    Long countByProviderEmployee_usernameAndStatus(String providerEmployee_username, Status status);
    Long countByCalibratorEmployee_usernameAndStatus(String providerEmployee_username, Status status);
    Long countByStateVerificatorEmployee_usernameAndStatus(String providerEmployee_username, Status status);
    Long countByProviderIdAndStatusAndReadStatus(Long providerId, Status status, Verification.ReadStatus readStatus);
    Long countByCalibratorIdAndStatusAndReadStatus(Long providerId, Status status, Verification.ReadStatus readStatus);
    Long countByStateVerificatorIdAndStatusAndReadStatus(Long stateVerificatorId, Status status, Verification.ReadStatus readStatus);

    @Query("select u.providerEmployee from Verification u where u.id = :id")
    User getProviderEmployeeById(@Param("id") String id);

    List<Verification> findByProviderEmployeeUsernameAndStatus(String providerEmployee,Status status);

    List<Verification> findByCalibratorEmployeeUsernameAndStatus(String calibratorEmployee,Status status);

    List<Verification> findByStateVerificatorEmployeeUsernameAndStatus(String calibratorEmployee,Status status);

    List<Verification> findByProviderEmployeeIsNotNullAndProviderAndSentToCalibratorDateBetween(Organization organization,Date dateFrom,Date DateTo);
    
    List<Verification> findByCalibratorAndInitialDateBetween(Organization organization,Date dateFrom,Date DateTo);

    List<Verification> findByProviderAndInitialDateBetween(Organization organization,Date dateFrom,Date DateTo);

    @Query("SELECT COUNT(u.id) FROM Verification u WHERE u.status = 'SENT' and u.provider = :provider")
    int getCountOfAllSentVerifications(@Param("provider") Organization provider);

    @Query("SELECT COUNT(u.id) FROM Verification u WHERE u.status = 'ACCEPTED' and u.provider = :provider")
    int getCountOfAllAcceptedVerifications(@Param("provider") Organization provider);
    
    @Query("SELECT COUNT(u.id) FROM Verification u WHERE u.status = 'IN_PROGRESS' and u.calibratorEmployee IS NULL and u.calibrator = :provider")
    int findCountOfAllCalibratorVerificationWithoutEmployee(@Param("provider") Organization provider);
    
    @Query("SELECT COUNT(u.id) FROM Verification u WHERE u.status IN ('IN_PROGRESS', 'PLANNING_TASK', 'TEST_PLACE_DETERMINED', 'SENT_TO_TEST_DEVICE', 'TEST_COMPLETED') and u.calibratorEmployee IS NOT NULL and u.calibrator = :provider")
    int findCountOfAllCalibratorVerificationWithEmployee(@Param("provider") Organization provider);


    @Query("SELECT MIN(u.initialDate) FROM Verification u WHERE (u.status = 'ACCEPTED' or u.status = 'SENT') and u.provider = :provider")
    java.sql.Date getEarliestDateOfAllAcceptedOrSentVerificationsByProvider(@Param("provider") Organization provider);

    @Query("SELECT MIN(u.initialDate) FROM Verification u WHERE  u.status NOT IN ('ACCEPTED', 'SENT') and u.provider = :provider")
    java.sql.Date getEarliestDateOfArchivalVerificationsByProvider(@Param("provider") Organization provider);


    @Query("SELECT MIN(u.initialDate) FROM Verification u WHERE u.status IN ('IN_PROGRESS', 'TEST_PLACE_DETERMINED', 'SENT_TO_TEST_DEVICE', 'TEST_COMPLETED') and u.calibrator = :calibrator")
    java.sql.Date getEarliestDateOfAllNewVerificationsByCalibrator(@Param("calibrator") Organization calibrator);

    @Query("SELECT MIN(u.initialDate) FROM Verification u WHERE u.status NOT IN ('ACCEPTED', 'SENT', 'IN_PROGRESS') and u.calibrator = :calibrator")
    java.sql.Date getEarliestDateOfArchivalVerificationsByCalibrator(@Param("calibrator") Organization calibrator);

    List<Verification> findByCalibratorEmployeeUsernameAndTaskStatus(String userName, Status status);

    List<Verification> findByTaskStatusAndCalibratorId(Status status, Long id);

    Page<Verification> findByTaskStatusAndCalibratorId(Status status, Long id, Pageable pageable);

    Page<Verification> findByCalibratorEmployeeUsernameAndTaskStatus(String userName, Status status, Pageable pageable);
}


