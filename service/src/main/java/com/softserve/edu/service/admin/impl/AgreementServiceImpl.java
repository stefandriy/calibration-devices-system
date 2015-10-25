package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.repository.AgreementRepository;
import com.softserve.edu.service.admin.AgreementService;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.utils.AgreementQueryConstructor;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class AgreementServiceImpl implements AgreementService {

    private final Logger logger = Logger.getLogger(AgreementServiceImpl.class);

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private AgreementRepository agreementRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Agreement add(Agreement agreement) {
        if (agreement == null) {
            throw new NullPointerException("Agreement is null");
        }

        return agreementRepository.save(agreement);
    }

    @Override
    @Transactional
    public Agreement add(Organization customer, Organization executor, String number, Long deviceCount, Date date, Device.DeviceType deviceType) {
        Agreement agreement = new Agreement(customer, executor, number, deviceCount, date, deviceType);
        return agreementRepository.save(agreement);
    }

    @Override
    @Transactional
    public Agreement add(Long customerId, Long executorId, String number, Long deviceCount, Date date, Device.DeviceType deviceType) {
        Organization customer = organizationService.getOrganizationById(customerId);
        Organization executor = organizationService.getOrganizationById(executorId);
        if (customer == null || executor == null) {
            logger.error("Organization not found");
            throw new NullPointerException("Organization not found");
        }
        Agreement agreement = new Agreement(customer, executor, number, deviceCount, date, deviceType);
        return agreementRepository.save(agreement);
    }

    @Override
    @Transactional(readOnly = true)
    public Agreement findAgreementById(Long id) {
        return agreementRepository.findOne(id);
    }

    @Override
    public Set<Agreement> findAll() {
        return agreementRepository.findAll();
    }

    @Override
    @Transactional
    public void update(Long agreementId, Long customerId, Long executorId, String number, Long deviceCount, Date date, Device.DeviceType deviceType) {
        Agreement agreement = agreementRepository.findOne(agreementId);
        Organization customer = organizationService.getOrganizationById(customerId);
        Organization executor = organizationService.getOrganizationById(executorId);

        agreement.setCustomer(customer);
        agreement.setExecutor(executor);
        agreement.setNumber(number);
        agreement.setDate(date);
        agreement.setDeviceCount(deviceCount);
        agreement.setDeviceType(deviceType);

        agreementRepository.save(agreement);
    }

    @Override
    @Transactional
    public ListToPageTransformer<Agreement> getCategoryDevicesBySearchAndPagination(int pageNumber, int itemsPerPage, String customer, String executor, String number,
                                                                                    String deviceCount, String deviceType, String startDateToSearch, String endDateToSearch,
                                                                                    String isActive, String sortCriteria, String sortOrder) {
        CriteriaQuery<Agreement> criteriaQuery = AgreementQueryConstructor
                .buildSearchQuery(customer, executor, number, deviceCount, startDateToSearch, endDateToSearch, deviceType, isActive, sortCriteria, sortOrder, entityManager);

        Long count = entityManager.createQuery(AgreementQueryConstructor
                .buildCountQuery(customer, executor, number, deviceCount, startDateToSearch, endDateToSearch, deviceType, isActive, entityManager)).getSingleResult();

        TypedQuery<Agreement> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Agreement> AgreementList = typedQuery.getResultList();

        ListToPageTransformer<Agreement> result = new ListToPageTransformer<>();
        result.setContent(AgreementList);
        result.setTotalItems(count);
        return result;
    }

    @Override
    @Transactional
    public void disableAgreement(Long agreementId) {
        Agreement agreement = agreementRepository.findOne(agreementId);
        agreement.setIsAvailable(false);
        agreementRepository.save(agreement);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Agreement> findByCustomerIdAndDeviceType(Long customerId, Device.DeviceType deviceType) {
        return agreementRepository.findByCustomerIdAndDeviceType(customerId, deviceType);
    }

    @Override
    public java.sql.Date getEarliestDateAvailableAgreement() {
        return agreementRepository.findEarliestDateAvalibleAgreement();
    }
}
