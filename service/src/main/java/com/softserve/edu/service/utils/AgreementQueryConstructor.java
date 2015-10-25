package com.softserve.edu.service.utils;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import com.softserve.edu.entity.organization.Organization;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class AgreementQueryConstructor {
    static Logger logger = Logger.getLogger(AgreementQueryConstructor.class);

    /**
     * @param customer
     * @param executor
     * @param number
     * @param deviceCount
     * @param deviceType
     * @param sortCriteria
     * @param sortOrder
     * @param em
     * @return
     */
    public static CriteriaQuery<Agreement> buildSearchQuery(String customer, String executor, String number,
                                                            String deviceCount, String startDateToSearch, String endDateToSearch, String deviceType, String isActive,
                                                            String sortCriteria, String sortOrder, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Agreement> criteriaQuery = cb.createQuery(Agreement.class);
        Root<Agreement> root = criteriaQuery.from(Agreement.class);

        Predicate predicate = AgreementQueryConstructor.buildPredicate(customer, executor, number, deviceCount,
                startDateToSearch, endDateToSearch, deviceType, isActive, root, cb);
        if ((sortCriteria != null) && (sortOrder != null)) {
            criteriaQuery.orderBy(SortCriteriaAgreement.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, cb, sortOrder));
        } else {
            criteriaQuery.orderBy(cb.desc(root.get("number")));
        }
        criteriaQuery.select(root);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }

    /**
     * @param customer
     * @param executor
     * @param number
     * @param deviceCount

     * @param deviceType
     * @param em
     * @return
     */
    public static CriteriaQuery<Long> buildCountQuery(String customer, String executor, String number,
                                                      String deviceCount, String startDateToSearch, String endDateToSearch,
                                                      String deviceType, String isActive, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Agreement> root = countQuery.from(Agreement.class);


        Predicate predicate = AgreementQueryConstructor.buildPredicate(customer, executor, number, deviceCount,
                startDateToSearch, endDateToSearch, deviceType, isActive, root, cb);
        countQuery.select(cb.count(root));
        countQuery.where(predicate);

        return countQuery;
    }

    /**
     * Builds predicate for query
     *
     * @param customer
     * @param executor
     * @param number
     * @param deviceCount

     * @param deviceType
     * @param root
     * @param cb
     * @return predicate for query
     */
    private static Predicate buildPredicate(String customer, String executor, String number,
                                            String deviceCount, String startDateToSearch, String endDateToSearch, String deviceType, String isActive,
                                            Root<Agreement> root, CriteriaBuilder cb) {
        Join<Agreement, Organization> customerJoin = root.join("customer");
        Join<Agreement, Organization> executorJoin = root.join("executor");

        Predicate queryPredicate = cb.conjunction();
        if ((customer != null) && (customer.length() > 0)) {
            queryPredicate = cb.and(cb.like(customerJoin.get("name"), "%" + customer + "%"), queryPredicate);
        }
        if ((executor != null) && (executor.length() > 0)) {
            queryPredicate = cb.and(cb.like(executorJoin.get("name"), "%" + executor + "%"), queryPredicate);
        }
        if ((number != null) && (number.length() > 0)) {
            queryPredicate = cb.and(cb.like(root.get("number"), "%" + number + "%"), queryPredicate);
        }
        if ((deviceCount != null) && (deviceCount.length() > 0)) {
            queryPredicate = cb.and(cb.equal(root.get("deviceCount"), deviceCount.trim()), queryPredicate);
        }
        if ((isActive != null) && (isActive.length() > 0)) {
            queryPredicate = cb.and(cb.equal(root.get("isAvailable"), true), queryPredicate);
        }
        if ((deviceType != null) && (deviceType.length() > 0)) {
            queryPredicate = cb.and(cb.equal(root.get("deviceType"),
                    Device.DeviceType.valueOf(deviceType.trim())), queryPredicate);
        }
        if (startDateToSearch != null && endDateToSearch != null) {
            DateTimeFormatter dbDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

            LocalDate startDate = null;
            LocalDate endDate = null;
            try {
                startDate = LocalDate.parse(startDateToSearch, dbDateTimeFormatter);
                endDate = LocalDate.parse(endDateToSearch, dbDateTimeFormatter);
            } catch (Exception pe) {
                logger.error("Cannot parse date", pe); //TODO: add exception catching
            }
            //verifications with date between these two dates
            queryPredicate = cb.and(cb.between(root.get("date"), java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)), queryPredicate);

        }
        return queryPredicate;
    }
}
