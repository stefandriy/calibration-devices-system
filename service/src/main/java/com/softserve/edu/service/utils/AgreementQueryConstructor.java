package com.softserve.edu.service.utils;

import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.organization.Agreement;
import com.softserve.edu.entity.organization.Organization;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;


public class AgreementQueryConstructor {
    static Logger logger = Logger.getLogger(AgreementQueryConstructor.class);

    /**
     *
     * @param customer
     * @param executor
     * @param number
     * @param deviceCount
     * @param date
     * @param deviceType
     * @param sortCriteria
     * @param sortOrder
     * @param em
     * @return
     */
    public static CriteriaQuery<Agreement> buildSearchQuery(String customer, String executor, String number,
                                                            String deviceCount, String date, String deviceType,
                                                            String sortCriteria, String sortOrder, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Agreement> criteriaQuery = cb.createQuery(Agreement.class);
        Root<Agreement> root = criteriaQuery.from(Agreement.class);

        Predicate predicate = AgreementQueryConstructor.buildPredicate(customer, executor, number, deviceCount, date, deviceType, root, cb);
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
     *
     * @param customer
     * @param executor
     * @param number
     * @param deviceCount
     * @param date
     * @param deviceType
     * @param em
     * @return
     */
    public static CriteriaQuery<Long> buildCountQuery(String customer, String executor, String number,
                                                      String deviceCount, String date, String deviceType, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Agreement> root = countQuery.from(Agreement.class);


        Predicate predicate = AgreementQueryConstructor.buildPredicate(customer, executor, number, deviceCount, date, deviceType, root, cb);
        countQuery.select(cb.count(root));
        countQuery.where(predicate);

        return countQuery;
    }

    /**
     * Builds predicate for query
     * @param customer
     * @param executor
     * @param number
     * @param deviceCount
     * @param date
     * @param deviceType
     * @param root
     * @param cb
     * @return predicate for query
     */
    private static Predicate buildPredicate(String customer, String executor, String number,
                                            String deviceCount, String date, String deviceType, Root<Agreement> root, CriteriaBuilder cb) {
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
        if ((deviceType != null) && (deviceType.length() > 0)) {
            queryPredicate = cb.and(cb.equal(root.get("deviceType"),
                    DeviceType.valueOf(deviceType.trim())), queryPredicate);
        }

        return queryPredicate;
    }
}
