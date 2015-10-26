package com.softserve.edu.service.utils;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import com.softserve.edu.entity.organization.Organization;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Deprecated
public class AgreementQueryConstructor {
    static Logger logger = Logger.getLogger(AgreementQueryConstructor.class);

    private static final String CUSTOMER_NAME = "customerName";
    private static final String EXECUTOR_NAME = "executorName";
    private static final String NUMBER = "number";
    private static final String DEVICE_TYPE = "deviceType";
    private static final String DEVICE_COUNT= "deviceCount";
    private static final String START_DATE_TO_SEARCH = "startDateToSearch";
    private static final String END_DATE_TO_SEARCH = "endDateToSearch";
    private static final String IS_AVAILABLE = "isAvailable";

    /**
     * @param sortCriteria
     * @param sortOrder
     * @param em
     * @return
     */
    public static CriteriaQuery<Agreement> buildSearchQuery(Map<String,String> searchKeys,
                                                            String sortCriteria, String sortOrder, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Agreement> criteriaQuery = cb.createQuery(Agreement.class);
        Root<Agreement> root = criteriaQuery.from(Agreement.class);

        Predicate predicate = AgreementQueryConstructor.buildPredicate(searchKeys, root, cb);
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

     * @param em
     * @return
     */
    public static CriteriaQuery<Long> buildCountQuery(Map<String,String> searchKeys, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Agreement> root = countQuery.from(Agreement.class);

        Predicate predicate = AgreementQueryConstructor.buildPredicate(searchKeys, root, cb);
        countQuery.select(cb.count(root));
        countQuery.where(predicate);

        return countQuery;
    }

    /**
     * Builds predicate for query
     * @param root
     * @param cb
     * @return predicate for query
     */
    private static Predicate buildPredicate(Map<String,String> searchKeys,
                                            Root<Agreement> root, CriteriaBuilder cb) {
        Join<Agreement, Organization> customerJoin = root.join("customer");
        Join<Agreement, Organization> executorJoin = root.join("executor");

        Predicate queryPredicate = cb.conjunction();
        if (checkKey(searchKeys, CUSTOMER_NAME)) {
            queryPredicate = cb.and(cb.like(customerJoin.get("name"), "%" + searchKeys.get(CUSTOMER_NAME) + "%"), queryPredicate);
        }
        if (checkKey(searchKeys, EXECUTOR_NAME)) {
            queryPredicate = cb.and(cb.like(executorJoin.get("name"), "%" + searchKeys.get(EXECUTOR_NAME) + "%"), queryPredicate);
        }
        if (checkKey(searchKeys, NUMBER)) {
            queryPredicate = cb.and(cb.like(root.get("number"), "%" + searchKeys.get(NUMBER) + "%"), queryPredicate);
        }
        if (searchKeys.containsKey(DEVICE_COUNT)) {
            queryPredicate = cb.and(cb.equal(root.get("deviceCount"), String.valueOf(searchKeys.get(DEVICE_COUNT))), queryPredicate);
        }
        if (checkKey(searchKeys, IS_AVAILABLE)) {
            queryPredicate = cb.and(cb.equal(root.get("isAvailable"), true), queryPredicate);
        }
        if (checkKey(searchKeys, DEVICE_TYPE)) {
            queryPredicate = cb.and(cb.equal(root.get("deviceType"),
                    Device.DeviceType.valueOf(searchKeys.get(DEVICE_TYPE).trim())), queryPredicate);
        }
        if (checkKey(searchKeys, START_DATE_TO_SEARCH) && checkKey(searchKeys, END_DATE_TO_SEARCH)) {
            DateTimeFormatter dbDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

            LocalDate startDate = null;
            LocalDate endDate = null;
            try {
                startDate = LocalDate.parse(searchKeys.get(START_DATE_TO_SEARCH), dbDateTimeFormatter);
                endDate = LocalDate.parse(searchKeys.get(END_DATE_TO_SEARCH), dbDateTimeFormatter);
            } catch (Exception pe) {
                logger.error("Cannot parse date", pe); //TODO: add exception catching
            }
            //verifications with date between these two dates
            queryPredicate = cb.and(cb.between(root.get("date"), java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)), queryPredicate);

        }
        return queryPredicate;
    }

    private static boolean checkKey(Map<String,String> searchKeys, String key){
        return searchKeys.containsKey(key) && (searchKeys.get(key).length() > 0);
    }
}
