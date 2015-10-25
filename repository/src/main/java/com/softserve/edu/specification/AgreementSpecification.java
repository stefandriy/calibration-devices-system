package com.softserve.edu.specification;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.specification.sort.AgreementSortCriteria;
import org.apache.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


public final class AgreementSpecification {

    static Logger logger = Logger.getLogger(AgreementSpecification.class);

    public static final String ID = "id";
    public static final String CUSTOMER_NAME = "customerName";
    public static final String EXECUTOR_NAME = "executorName";
    public static final String NUMBER = "number";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String DEVICE_COUNT = "deviceCount";
    public static final String START_DATE_TO_SEARCH = "startDateToSearch";
    public static final String END_DATE_TO_SEARCH = "endDateToSearch";
    public static final String IS_AVAILABLE = "isAvailable";
    public static final String DATE = "date";

    private AgreementSpecification() {
    }

    public static Specification<Agreement> buildPredicate(Map<String, String> searchKeys) {
        return (root, query, cb) -> {
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
        };
        //return (root, query, cb) -> cb.like(root.get(NUMBER), getContainsLikePattern(searchTerm));
    }

    /**
     * If the searchTerm is null or empty, return the String "%". This ensures that if the search term is not given,
     * our specification builder class will create a specification that returns all entries
     *
     * @param searchTerm
     * @return
     */
  /*  private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm + "%";
        }
    }*/
    private static boolean checkKey(Map<String, String> searchKeys, String key) {
        return searchKeys.containsKey(key) && (checkValue(searchKeys.get(key)));
    }

    private static boolean checkValue(String value) {
        return (value != null) && (!value.isEmpty());
    }

    /**
     * Create Sort object for Specification executor bu criteria and order
     *
     * @param sortCriteria
     * @param sortOrder
     * @return
     */
    public static Sort getSort(String sortCriteria, String sortOrder) {
        if (checkValue(sortCriteria) && checkValue(sortOrder)) {
            return AgreementSortCriteria.valueOf(sortCriteria.toUpperCase()).getSort(sortOrder);
        } else {
            return AgreementSortCriteria.UNDEFINED.getSort("asc");
        }
    }

    public static Pageable constructPageSpecification(int pageNumber, int itemsPerPage, String sortCriteria, String sortOrder) {
        return new PageRequest(pageNumber, itemsPerPage, getSort(sortCriteria, sortOrder));
    }
}
