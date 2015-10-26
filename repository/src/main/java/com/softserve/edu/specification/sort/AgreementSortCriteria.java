package com.softserve.edu.specification.sort;

import com.softserve.edu.specification.AgreementSpecification;
import org.springframework.data.domain.Sort;

public enum AgreementSortCriteria {
    ID() {
        public Sort getSort(String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, AgreementSpecification.ID);
            } else {
                return new Sort(Sort.Direction.DESC, AgreementSpecification.ID);
            }
        }
    },
    UNDEFINED() {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, AgreementSpecification.NUMBER);
            } else {
                return new Sort(Sort.Direction.DESC, AgreementSpecification.NUMBER);
            }
        }
    },
    NUMBER() {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, AgreementSpecification.NUMBER);
            } else {
                return new Sort(Sort.Direction.DESC, AgreementSpecification.NUMBER);
            }
        }
    },
    DEVICE_TYPE() {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, AgreementSpecification.DEVICE_TYPE);
            } else {
                return new Sort(Sort.Direction.DESC, AgreementSpecification.DEVICE_TYPE);
            }
        }
    },
    DEVICE_COUNT() {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, AgreementSpecification.DEVICE_COUNT);
            } else {
                return new Sort(Sort.Direction.DESC, AgreementSpecification.DEVICE_COUNT);
            }
        }
    },
    CUSTOMER_NAME() {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, AgreementSpecification.CUSTOMER_JOIN_NAME);
            } else {
                return new Sort(Sort.Direction.DESC, AgreementSpecification.CUSTOMER_JOIN_NAME);
            }
        }
    },
    EXECUTOR_NAME() {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, AgreementSpecification.EXECUTOR_JOIN_NAME);
            } else {
                return new Sort(Sort.Direction.DESC, AgreementSpecification.EXECUTOR_JOIN_NAME);
            }
        }
    },
    DATE() {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, AgreementSpecification.DATE);
            } else {
                return new Sort(Sort.Direction.DESC, AgreementSpecification.DATE);
            }
        }
    };

    public Sort getSort(String sortOrder) {
        return null;
    }
}
