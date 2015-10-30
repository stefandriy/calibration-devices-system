package com.softserve.edu.specification;

import com.softserve.edu.specification.sort.AgreementSortCriteria;
import org.apache.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public final class AgreementSpecification {

    static Logger logger = Logger.getLogger(AgreementSpecification.class);

    public static final String ID = "id";
    public static final String CUSTOMER_JOIN_NAME = "customer.name";
    public static final String CUSTOMER_NAME = "customerName";
    public static final String EXECUTOR_JOIN_NAME = "executor.name";
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
