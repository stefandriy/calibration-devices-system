package com.softserve.edu.service.verification.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Counter;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.ClientData;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.repository.*;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.*;
import com.softserve.edu.service.verification.VerificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class VerificationServiceImpl implements VerificationService {

    private Logger logger = Logger.getLogger(VerificationServiceImpl.class);

    @Autowired
    private CalibrationPlanningTaskRepository taskRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private CalibrationTestRepository calibrationTestRepository;

    @Autowired
    private CounterTypeRepository counterTypeRepository;

    @Autowired
    private CounterRepository counterRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveVerification(Verification verification) {
        verificationRepository.save(verification);
    }

    @Transactional(readOnly = true)
    public Verification findById(String code) {
        return verificationRepository.findOne(code);
    }

    /**
     * Returns requested number(page) of Verification entities(itemsPerPage
     * parameter) that belongs to specific organization. Note: pagination starts
     * from 1 at client side, but Spring Data JPA from 0.
     *
     * @param pageNumber   Number of partial data that will be returned.
     * @param itemsPerPage Number of Verification-s that will be present in one page(unit
     *                     of partial data).
     * @return Requested page of Verification-s that belong to specific
     * organization.
     */
    @Transactional(readOnly = true)
    public Page<Verification> findPageOfAllVerificationsByProviderId(Long providerId, int pageNumber,
                                                                     int itemsPerPage) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return verificationRepository.findByProviderId(providerId, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Verification> findPageOfAllVerificationsByCalibratorId(Long calibratorId, int pageNumber,
                                                                       int itemsPerPage) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return verificationRepository.findByCalibratorId(calibratorId, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Verification> findPageOfAllVerificationsByStateVerificatorId(Long stateVerificatorId, int pageNumber,
                                                                             int itemsPerPage) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return verificationRepository.findByStateVerificatorId(stateVerificatorId, pageRequest);
    }

    /**
     * Finds count of rows in database for verifications assigned to Calibrator with Read Status = 'UNREAD'.
     * Method is used for notification about unwatched verifications
     *
     * @param calibratorId
     * @return
     */
    @Transactional(readOnly = true)
    public Long findCountOfNewVerificationsByCalibratorId(Long calibratorId) {
        return verificationRepository.countByCalibratorIdAndStatusAndReadStatus(calibratorId, Status.IN_PROGRESS, Verification.ReadStatus.UNREAD);
    }

    /**
     * Finds count of rows in database for verifications assigned to Provider with Read Status = 'UNREAD'.
     * Method is used for notification about unwatched verifications
     *
     * @param providerId
     * @return
     */
    @Transactional(readOnly = true)
    public Long findCountOfNewVerificationsByProviderId(Long providerId) {
        return verificationRepository.countByProviderIdAndStatusAndReadStatus(providerId, Status.SENT, Verification.ReadStatus.UNREAD);
    }

    /**
     * Finds count of rows in database for verifications assigned to State Verificator with Read Status = 'UNREAD'.
     * Method is used for notification about unwatched verifications
     *
     * @param stateVerificatorId
     * @return
     */
    @Transactional(readOnly = true)
    public Long findCountOfNewVerificationsByStateVerificatorId(Long stateVerificatorId) {
        return verificationRepository.countByStateVerificatorIdAndStatusAndReadStatus(stateVerificatorId, Status.SENT_TO_VERIFICATOR, Verification.ReadStatus.UNREAD);
    }

    @Transactional(readOnly = true)
    public Page<Verification> findPageOfSentVerificationsByProviderId(Long providerId, int pageNumber, int itemsPerPage) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return verificationRepository.findByProviderIdAndStatusOrderByInitialDateDesc(providerId, Status.SENT, pageRequest);
    }

    /**
     * Returns requested number(page) of Verification entities(itemsPerPage
     * parameter) that belongs to specific calibrator and have status received.
     * Note: pagination starts from 1 at client side, but Spring Data JPA from
     * 0.
     *
     * @param calibratorId id of calibrator.
     * @param pageNumber   Number of partial data that will be returned.
     * @param itemsPerPage Number of Verification-s that will be present in one page(unit
     *                     of partial data).
     * @return Requested page of Verification-s that belong to specific
     * organization.
     */
    @Transactional(readOnly = true)
    public Page<Verification> findPageOfSentVerificationsByCalibratorId(Long calibratorId, int pageNumber, int itemsPerPage) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return verificationRepository.findByCalibratorIdAndStatusOrderByInitialDateDesc(calibratorId, Status.IN_PROGRESS, pageRequest);
    }


    @Transactional(readOnly = true)
    public Page<Verification> findPageOfSentVerificationsByStateVerificatorId(Long stateVerificatorId, int pageNumber, int itemsPerPage) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return verificationRepository.findByStateVerificatorIdAndStatus(stateVerificatorId, Status.SENT_TO_VERIFICATOR, pageRequest);
    }


    /**
     * Find page of new verifications for provider with search parameters specified
     *
     * @param providerId        ID of organization
     * @param pageNumber        number of page requested by user
     * @param itemsPerPage      desired number of rows to be displayed on page
     * @param startDateToSearch search by initial date of verification
     * @param endDateToSearch   end date
     * @param idToSearch        search by verification ID
     * @param streetToSearch    search by street where client lives
     * @param providerEmployee  restrict query by provider employee user name. Allows restrict query so that simple employee user
     *                          can only see verifications assigned to him and free verifications (not yet assigned)
     * @return ListToPageTransformer<Verification>
     */
    @Transactional(readOnly = true)
    public ListToPageTransformer<Verification> findPageOfSentVerificationsByProviderIdAndCriteriaSearch(Long providerId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                        String streetToSearch, String region, String district, String locality, String status, String employeeName, String sortCriteria, String sortOrder, User providerEmployee) {

        CriteriaQuery<Verification> criteriaQuery = NewVerificationsQueryConstructorProvider.buildSearchQuery(providerId, startDateToSearch, endDateToSearch,
                idToSearch, fullNameToSearch, streetToSearch, region, district,
                locality, status,
                providerEmployee, sortCriteria,
                sortOrder, employeeName, em);

        Long count = em.createQuery(NewVerificationsQueryConstructorProvider.buildCountQuery(providerId, startDateToSearch, endDateToSearch, idToSearch, fullNameToSearch, streetToSearch, region, district, locality, status, providerEmployee, employeeName, em)).getSingleResult();

        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Verification> verificationList = typedQuery.getResultList();

        ListToPageTransformer<Verification> result = new ListToPageTransformer<Verification>();
        result.setContent(verificationList);
        result.setTotalItems(count);
        return result;
    }


    @Transactional(readOnly = true)
    public ListToPageTransformer<Verification> findPageOfArchiveVerificationsByProviderId(Long organizationId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                          String streetToSearch, String region, String district, String locality, String status, String employeeName, String sortCriteria, String sortOrder, User providerEmployee) {

        CriteriaQuery<Verification> criteriaQuery = ArchivalVerificationsQueryConstructorProvider.buildSearchQuery(organizationId, startDateToSearch, endDateToSearch, idToSearch, fullNameToSearch, streetToSearch, region, district, locality, status, employeeName, sortCriteria, sortOrder, providerEmployee, em);

        Long count = em.createQuery(ArchivalVerificationsQueryConstructorProvider.buildCountQuery(organizationId, startDateToSearch, endDateToSearch, idToSearch, fullNameToSearch, streetToSearch, region, district, locality, status, employeeName, providerEmployee, em)).getSingleResult();

        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Verification> verificationList = typedQuery.getResultList();

        ListToPageTransformer<Verification> result = new ListToPageTransformer<>();
        result.setContent(verificationList);
        result.setTotalItems(count);
        return result;
    }

    @Transactional(readOnly = true)
    public ListToPageTransformer<Verification> findPageOfArchiveVerificationsByProviderIdOnMainPanel(Long organizationId, int pageNumber, int itemsPerPage, String initialDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                     String streetToSearch, String region, String district, String locality, String status, String employeeName, User providerEmployee) {

        CriteriaQuery<Verification> criteriaQuery = ArchivalVerificationsQueryConstructorProvider.buildSearchQuery(organizationId, initialDateToSearch, null, idToSearch, fullNameToSearch, streetToSearch, region, district, locality, "SENT", employeeName, null, null, providerEmployee, em);

        Long count = em.createQuery(ArchivalVerificationsQueryConstructorProvider.buildCountQuery(organizationId, initialDateToSearch, null, idToSearch, fullNameToSearch, streetToSearch, region, district, locality, "SENT", employeeName, providerEmployee, em)).getSingleResult();

        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Verification> verificationList = typedQuery.getResultList();

        ListToPageTransformer<Verification> result = new ListToPageTransformer<>();
        result.setContent(verificationList);
        result.setTotalItems(count);
        return result;
    }
    
    @Transactional(readOnly = true)
    public ListToPageTransformer<Verification> findPageOfArchiveVerificationsByCalibratorIdOnMainPanel(Long organizationId, int pageNumber, int itemsPerPage, String initialDateToSearch, String idToSearch, String fullNameToSearch,
            																							String streetToSearch, String region, String district, String locality, String status, String employeeName, User calibratorEmployee) {
        CriteriaQuery<Verification> criteriaQuery = ArchivalVerificationsQueryConstructorCalibrator.buildSearchQuery(organizationId, initialDateToSearch, null, idToSearch, fullNameToSearch, streetToSearch, "IN_PROGRESS", employeeName, null, null, null, null, null, null, null, em);

        Long count = em.createQuery(ArchivalVerificationsQueryConstructorCalibrator.buildCountQuery(organizationId, initialDateToSearch, null, idToSearch, fullNameToSearch, streetToSearch, "IN_PROGRESS", employeeName, null, null, null, null, null, em)).getSingleResult();

        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Verification> verificationList = typedQuery.getResultList();

        ListToPageTransformer<Verification> result = new ListToPageTransformer<>();
        result.setContent(verificationList);
        result.setTotalItems(count);
        return result;
    }

    @Transactional(readOnly = true)
    public ListToPageTransformer<Verification> findPageOfArchiveVerificationsByVerificatorIdOnMainPanel(Long organizationId, int pageNumber, int itemsPerPage, String initialDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                        String streetToSearch, String region, String district, String locality, String status, String employeeName, User stateVerificatorEmployee) {
        CriteriaQuery<Verification> criteriaQuery = ArchivalVerificationsQueryConstructorVerificator.buildSearchQuery(organizationId, initialDateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, employeeName, null, null, null, em);

        Long count = em.createQuery(ArchivalVerificationsQueryConstructorVerificator.buildCountQuery(organizationId, initialDateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, employeeName, null, em)).getSingleResult();

        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Verification> verificationList = typedQuery.getResultList();

        ListToPageTransformer<Verification> result = new ListToPageTransformer<>();
        result.setContent(verificationList);
        result.setTotalItems(count);
        return result;
    }

    //TODO: refactor methods of other guys (not only provider) to include endDateToSearch and name
    @Transactional(readOnly = true)
    public ListToPageTransformer<Verification> findPageOfVerificationsByCalibratorIdAndCriteriaSearch(Long calibratorId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                      String streetToSearch, String region, String district, String locality, String status, String employeeName,String standardSize, String symbol, String nameProvider, String realiseYear, String dismantled, String building, String sortCriteria, String sortOrder, User calibratorEmployee,List<Map<String,String>> globalSearchParams) {


        CriteriaQuery<Verification> criteriaQuery = NewVerificationsQueryConstructorCalibrator.buildSearchQuery(calibratorId, startDateToSearch, endDateToSearch, idToSearch, fullNameToSearch, streetToSearch, region, district, locality, status, calibratorEmployee, standardSize, symbol, nameProvider, realiseYear, dismantled, building, sortCriteria, sortOrder, employeeName, em,globalSearchParams);


        Long count = em.createQuery(NewVerificationsQueryConstructorCalibrator.buildCountQuery(calibratorId, startDateToSearch, endDateToSearch, idToSearch, fullNameToSearch, streetToSearch, region, district, locality, status, calibratorEmployee, standardSize, symbol, nameProvider, realiseYear, dismantled,  building, employeeName, em)).getSingleResult();

        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Verification> verificationList = typedQuery.getResultList();
        ListToPageTransformer<Verification> result = new ListToPageTransformer<Verification>();
        result.setContent(verificationList);
        result.setTotalItems(count);
        return result;
    }


    @Transactional(readOnly = true)
    public ListToPageTransformer<Verification> findPageOfArchiveVerificationsByCalibratorId(Long organizationId, int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String idToSearch, String fullNameToSearch,
                                                                                            String streetToSearch, String status, String employeeName, Long protocolId, String protocolStatus, Long measurementDeviceId, String measurementDeviceType, String sortCriteria, String sortOrder, User calibratorEmployee) {

        CriteriaQuery<Verification> criteriaQuery = ArchivalVerificationsQueryConstructorCalibrator.buildSearchQuery(organizationId, startDateToSearch, endDateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, employeeName, protocolId, protocolStatus, measurementDeviceId, measurementDeviceType, sortCriteria, sortOrder, calibratorEmployee, em);
        Long count = em.createQuery(ArchivalVerificationsQueryConstructorCalibrator.buildCountQuery(organizationId, startDateToSearch, endDateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, employeeName, protocolId, protocolStatus, measurementDeviceId, measurementDeviceType, calibratorEmployee, em)).getSingleResult();

        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Verification> verificationList = typedQuery.getResultList();

        ListToPageTransformer<Verification> result = new ListToPageTransformer<Verification>();
        result.setContent(verificationList);
        result.setTotalItems(count);
        return result;
    }

    @Transactional(readOnly = true)
    public ListToPageTransformer<Verification> findPageOfVerificationsByVerificatorIdAndCriteriaSearch(Long verificatorId, int pageNumber, int itemsPerPage, String dateToSearch, String idToSearch, String fullNameToSearch,
                                                                                                       String streetToSearch, String status, String employeeName, String nameProvider, String nameCalibrator, String lastName, String firstName, String middleName, String district, String building, String flat, String sortCriteria, String sortOrder, User verificatorEmployee) {

        CriteriaQuery<Verification> criteriaQuery = NewVerificationsQueryConstructorVerificator.buildSearchQuery(verificatorId, dateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, verificatorEmployee, nameProvider, nameCalibrator, lastName, firstName, middleName, district,  building, flat, sortCriteria, sortOrder, employeeName, em);

        Long count = em.createQuery(NewVerificationsQueryConstructorVerificator.buildCountQuery(verificatorId, dateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, verificatorEmployee, nameProvider, nameCalibrator, lastName, firstName, middleName, district, building, flat, employeeName, em)).getSingleResult();

        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Verification> verificationList = typedQuery.getResultList();

        ListToPageTransformer<Verification> result = new ListToPageTransformer<Verification>();
        result.setContent(verificationList);
        result.setTotalItems(count);
        return result;
    }

    @Transactional(readOnly = true)
    public ListToPageTransformer<Verification> findPageOfArchiveVerificationsByVerificatorId(Long organizationId, int pageNumber, int itemsPerPage, String dateToSearch, String idToSearch, String fullNameToSearch,
                                                                                             String streetToSearch, String status, String employeeName, String sortCriteria, String sortOrder, User verificatorEmployee) {

        CriteriaQuery<Verification> criteriaQuery = ArchivalVerificationsQueryConstructorVerificator.buildSearchQuery(organizationId, dateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, employeeName, sortCriteria, sortOrder, verificatorEmployee, em);

        Long count = em.createQuery(ArchivalVerificationsQueryConstructorVerificator.buildCountQuery(organizationId, dateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, employeeName, verificatorEmployee, em)).getSingleResult();

        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Verification> verificationList = typedQuery.getResultList();

        ListToPageTransformer<Verification> result = new ListToPageTransformer<>();
        result.setContent(verificationList);
        result.setTotalItems(count);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ListToPageTransformer<CalibrationTest> findPageOfCalibrationTestsByVerificationId(int pageNumber, int itemsPerPage, String startDateToSearch, String endDateToSearch, String name, String region, String district, String locality, String streetToSearch, String idToSearch,
                                                                                             String fullNameToSearch, Integer settingNumber, String consumptionStatus, Long protocolId, String testResult, Long measurementDeviceId, String measurementDeviceType, String sortCriteria, String sortOrder) {

        CriteriaQuery<CalibrationTest> criteriaQuery = CalibrationTestQueryConstructorCalibrator.buildSearchQuery( startDateToSearch, endDateToSearch, name, region, district, locality, streetToSearch,
                idToSearch, fullNameToSearch, settingNumber, consumptionStatus, protocolId, testResult, measurementDeviceId, measurementDeviceType, sortCriteria, sortOrder, em);

        Long count = em.createQuery(CalibrationTestQueryConstructorCalibrator.buildCountQuery(startDateToSearch, endDateToSearch, name, region, district, locality, streetToSearch,
                idToSearch, fullNameToSearch, settingNumber, consumptionStatus, protocolId, testResult, measurementDeviceId, measurementDeviceType, em)).getSingleResult();

        TypedQuery<CalibrationTest> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<CalibrationTest> calibrationTestList = typedQuery.getResultList();

        ListToPageTransformer<CalibrationTest> result = new ListToPageTransformer<>();
        result.setContent(calibrationTestList);
        result.setTotalItems(count);
        return result;
    }



    @Transactional(readOnly = true)
    public Verification findByIdAndProviderId(String id, Long providerId) {
        Verification verification = verificationRepository.findByIdAndProviderId(id, providerId);
        if (verification == null) {
            throw new AccessDeniedException("You have not permission to get this data.");
        }
        return verification;
    }

    /**
     * Returns requested number(page) of Verification entities(itemsPerPage
     * parameter) that belongs to specific calibrator and have status received.
     * Note: pagination starts from 1 at client side, but Spring Data JPA from
     * 0.
     *
     * @param id           Id of the verification
     * @param calibratorId Number id of provider assigned to this verification
     * @return Verification that belong to specific calibrator
     */
    @Transactional(readOnly = true)
    public Verification findByIdAndCalibratorId(String id, Long calibratorId) {
        Verification verification = verificationRepository.findByIdAndCalibratorId(id, calibratorId);
        if (verification == null) {
            throw new AccessDeniedException("You have not permission to get this data.");
        }
        return verification;
    }

    @Transactional(readOnly = true)
    public Verification findByIdAndStateVerificatorId(String id, Long stateVerificatorId) {
        Verification verification = verificationRepository.findByIdAndStateVerificatorId(id, stateVerificatorId);
        if (verification == null) {
            throw new AccessDeniedException("You have not permission to get this data.");
        }
        return verification;
    }

    /**
     * Changes verification read status to 'READ' when Provider or Calibrator or State Verificator reads it
     *
     * @param verificationId
     * @param readStatus
     */
    @Transactional
    public void updateVerificationReadStatus(String verificationId, String readStatus) {
        Verification verification = verificationRepository.findOne(verificationId);
        if (verification == null) {
            logger.error("verification haven't found");
            return;
        }
        verification.setReadStatus(Verification.ReadStatus.READ);
        verificationRepository.save(verification);
    }


    @Transactional
    public void updateVerificationStatus(String verificationId, Status status) {
        Verification verification = verificationRepository.findOne(verificationId);
        if (verification == null) {
            logger.error("verification haven't found");
            return;
        }
        verification.setStatus(status);
        verification.setReadStatus(Verification.ReadStatus.READ);
        verification.setExpirationDate(new Date());
        verificationRepository.save(verification);
    }

    @Transactional
    public void sendVerificationTo(String verificationId, Organization oraganization, Status status) {
        Verification verification = verificationRepository.findOne(verificationId);
        if (verification == null) {
            logger.error("verification haven't found");
            return;
        }
        if (status.equals(Status.IN_PROGRESS)) {
            verification.setCalibrator(oraganization);
            verification.setSentToCalibratorDate(new Date());
        } else if (status.equals(Status.SENT_TO_VERIFICATOR)) {
            verification.setStateVerificator(oraganization);
        } else if ((status.equals(Status.TEST_OK)) || (status.equals(Status.TEST_NOK))) {
            verification.setProvider(oraganization);
        }
        verification.setStatus(status);
        verification.setReadStatus(Verification.ReadStatus.UNREAD);
        verification.setExpirationDate(new Date());
        verificationRepository.save(verification);
    }


    /**
     * Find verification, add complete status to stateVerificator, add
     * stateVerificator to verification save verification
     */
    @Transactional
    public void updateVerification(String verificationId, Organization stateVerificator) {
        Verification verification = verificationRepository.findOne(verificationId);
        if (verification == null) {
            logger.error("verification haven't found");
            return;
        }
        verification.setStatus(Status.TEST_OK);
        verification.setStateVerificator(stateVerificator);
        verificationRepository.save(verification);
    }

    @Transactional
    public void updateVerificationData(String id, ClientData clientData, Organization provider) {
        Verification verificationToEdit = verificationRepository.findOne(id);
        verificationToEdit.setInitialDate(new Date());
        verificationToEdit.setClientData(clientData);
        verificationToEdit.setProvider(provider);
        verificationToEdit.setStatus(Status.SENT);
        verificationToEdit.setReadStatus(Verification.ReadStatus.UNREAD);
        verificationRepository.save(verificationToEdit);
    }

    /**
     * Returns calibration test assigned to verification
     *
     * @param verificationId Id of the verification
     * @param data           all data filled by calibrator in test
     * @return test data with assigned verification that belong to specific
     * calibrator
     * @throws NotAvailableException if there is no verification with such id
     */

    @Transactional
    public CalibrationTest createCalibrationTest(String verificationId, CalibrationTest data) {
        Verification updatedVerification = verificationRepository.findOne(verificationId);
        if (updatedVerification == null) {
            throw new NotAvailableException("Повірки з таким ID не існує");
        }
        CalibrationTest testData = calibrationTestRepository.save(data);
        testData.setVerification(updatedVerification);
        return testData;
    }

    @Transactional(readOnly = true)
    public CalibrationTest findByCalibrationTestId(Long id) {
        CalibrationTest calibrationTest = calibrationTestRepository.findById(id);
        if (calibrationTest == null) {
            throw new AccessDeniedException("You have not permission to get this data");
        }
        return calibrationTest;
    }

    @Transactional
    public int findCountOfAllSentVerifications(Organization organization) {
        return verificationRepository.getCountOfAllSentVerifications(organization);
    }

    @Transactional
    public int findCountOfAllAcceptedVerification(Organization organization) {
        return verificationRepository.getCountOfAllAcceptedVerifications(organization);
    }
    
    @Transactional
    public int findCountOfAllCalibratorVerificationWithoutEmployee (Organization organization) {
        return verificationRepository.findCountOfAllCalibratorVerificationWithoutEmployee (organization);
    }
    
    @Transactional
    public int findCountOfAllCalibratorVerificationWithEmployee (Organization organization) {
        return verificationRepository.findCountOfAllCalibratorVerificationWithEmployee (organization);
    }

    @Transactional
    public int findCountOfAllVerificatorVerificationWithoutEmployee (Organization organization) {
        return verificationRepository.findCountOfAllVerificatorVerificationWithoutEmployee(organization);
    }

    @Transactional
    public int findCountOfAllVerificatorVerificationWithEmployee (Organization organization) {
        return verificationRepository.findCountOfAllVerificatorVerificationWithEmployee (organization);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getProcessTimeProvider() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = cb.createQuery(Object[].class);
        Root<Verification> root = q.from(Verification.class);
        Join<Verification, Organization> provider = root.join("provider");
        q.select(cb.array(root.get("expirationDate"), root.get("id"), provider.get("maxProcessTime"), provider.get("email")));
        Predicate statusPredicate = cb.or(cb.equal(root.get("status"), Status.valueOf("SENT")), cb.equal(root.get("status"), Status.valueOf("ACCEPTED")));
        q.where(statusPredicate);

        return em.createQuery(q).getResultList();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getProcessTimeCalibrator() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = cb.createQuery(Object[].class);
        Root<Verification> root = q.from(Verification.class);
        Join<Verification, Organization> provider = root.join("calibrator");
        q.select(cb.array(root.get("expirationDate"), root.get("id"), provider.get("maxProcessTime"), provider.get("email")));
        Predicate statusPredicate = cb.or(cb.equal(root.get("status"), Status.valueOf("IN_PROGRESS")), cb.equal(root.get("status"), Status.valueOf("TEST_PLACE_DETERMINED")),
                cb.equal(root.get("status"), Status.valueOf("SENT_TO_TEST_DEVICE")), cb.equal(root.get("status"), Status.valueOf("TEST_COMPLETED")));
        q.where(statusPredicate);

        return em.createQuery(q).getResultList();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getProcessTimeVerificator() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = cb.createQuery(Object[].class);
        Root<Verification> root = q.from(Verification.class);
        Join<Verification, Organization> provider = root.join("stateVerificator");
        q.select(cb.array(root.get("expirationDate"), root.get("id"), provider.get("maxProcessTime"), provider.get("email")));
        Predicate statusPredicate = cb.or(cb.equal(root.get("status"), Status.valueOf("SENT_TO_VERIFICATOR")), cb.equal(root.get("status"), Status.valueOf("TEST_OK")), cb.equal(root.get("status"), Status.valueOf("TEST_NOK")));
        q.where(statusPredicate);

        return em.createQuery(q).getResultList();
    }

    @Transactional(readOnly = true)
    public java.sql.Date getNewVerificationEarliestDateByProvider(Organization organization) {
        return verificationRepository.getEarliestDateOfAllAcceptedOrSentVerificationsByProvider(organization);
    }

    @Transactional(readOnly = true)
    public java.sql.Date getArchivalVerificationEarliestDateByProvider(Organization organization) {
        return verificationRepository.getEarliestDateOfArchivalVerificationsByProvider(organization);
    }

    @Transactional(readOnly = true)
    public java.sql.Date getNewVerificationEarliestDateByCalibrator(Organization organization) {
        return verificationRepository.getEarliestDateOfAllNewVerificationsByCalibrator(organization);
    }

    @Transactional(readOnly = true)
    public java.sql.Date getArchivalVerificationEarliestDateByCalibrator(Organization organization) {
        return verificationRepository.getEarliestDateOfArchivalVerificationsByCalibrator(organization);
    }

    @Transactional(readOnly = true)
    public java.sql.Date getEarliestPlanningTaskDate(Organization organization) {
        return verificationRepository.getEarliestPlanningTaskDate(organization);
    }

    @Transactional
    public Page<Verification> getVerificationsByTaskID(Long taskID, Pageable pageable) {
        return verificationRepository.findByTask_Id(taskID, pageable);
    }

    @Override
    public String getNewVerificationDailyId(Date date) {
        return String.format("%04d", verificationRepository.findByInitialDate(date).size()+1);
    }

    @Transactional
    public Verification[] getVerificationsByTaskID(Long taskID) {
        return verificationRepository.findByTask_Id(taskID);
    }

    @Override
    public void removeVerificationFromTask(String verificationId) {
        Verification verification = verificationRepository.findOne(verificationId);
        if (verification == null) {
            logger.error("verification wasn't found");
            throw new IllegalArgumentException();
        }
        verification.setStatus(Status.IN_PROGRESS);
        verification.setTaskStatus(Status.PLANNING_TASK);
        verification.setTask(null);
        verificationRepository.save(verification);
    }

    @Override
    public void editCounter(String verificationId, String deviceName, Boolean dismantled, Boolean sealPresence, Long dateOfDismantled,
                            Long dateOfMounted, String numberCounter, String releaseYear, String symbol, String standardSize,
                            String comment) {
        Verification verification = verificationRepository.findOne(verificationId);

        verification.setCounterStatus(dismantled);
        verification.setSealPresence(sealPresence);
        verification.setComment(comment);

        Counter counter = verification.getCounter();
        counter.setDateOfDismantled(dateOfDismantled);
        counter.setDateOfMounted(dateOfMounted);
        counter.setNumberCounter(numberCounter);
        counter.setReleaseYear(releaseYear);

        CounterType counterType = counterTypeRepository.findOneBySymbolAndStandardSize(symbol, standardSize);
        counter.setCounterType(counterType);
        counterRepository.save(counter);

        verificationRepository.save(verification);

    }

}
