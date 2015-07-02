package com.softserve.edu.repository;

import java.util.Date;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VerificationRepository extends PagingAndSortingRepository<Verification, String> {
    Page<Verification> findByProviderId(Long providerId, Pageable pageable);
    Page<Verification> findByCalibratorId(Long calibratorId, Pageable pageable);
    
    Page<Verification> findByProviderIdAndStatusOrderByInitialDateDesc(Long providerId, Status status, Pageable pageable);
    Page<Verification> findByCalibratorIdAndStatusOrderByInitialDateDesc(Long calibratorId, Status status, Pageable pageable);

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



    Long countByProviderEmployee_usernameAndStatus(String providerEmployee_username, Status status);
    Verification findByIdAndStateVerificatorId(String id, Long stateVerificatorId);

    long countByProviderIdAndStatusAndReadStatus(Long providerId, Status status, ReadStatus readStatus);
    long countByCalibratorIdAndStatusAndReadStatus(Long providerId, Status status, ReadStatus readStatus);

}
