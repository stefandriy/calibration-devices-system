package com.softserve.edu.service.utils;

import com.softserve.edu.entity.organization.Agreement;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
public enum SortCriteriaAgreement {
    ID() {
        public Order getSortOrder(Root<Agreement> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("id"));
            } else {
                return cb.desc(root.get("id"));
            }
        }
    },
    UNDEFINED() {
        public Order getSortOrder(Root<Agreement> root, CriteriaBuilder cb, String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("number"));
            } else {
                return cb.desc(root.get("number"));
            }
        }
    },
    NUMBER() {
        public Order getSortOrder(Root<Agreement> root, CriteriaBuilder cb, String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("number"));
            } else {
                return cb.desc(root.get("number"));
            }
        }
    },
    DEVICE_TYPE() {
        public Order getSortOrder(Root<Agreement> root, CriteriaBuilder cb, String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("deviceType"));
            } else {
                return cb.desc(root.get("deviceType"));
            }
        }
    },
    DEVICE_COUNT() {
        public Order getSortOrder(Root<Agreement> root, CriteriaBuilder cb, String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("deviceCount"));
            } else {
                return cb.desc(root.get("deviceCount"));
            }
        }
    },
    CUSTOMER_NAME() {
        public Order getSortOrder(Root<Agreement> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return (cb.asc(root.join("customerID").get("name")));
            } else {
                return (cb.desc(root.join("customerID").get("name")));
            }
        }
    },
    EXECUTOR_NAME() {
        public Order getSortOrder(Root<Agreement> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return (cb.asc(root.join("executor").get("name")));
            } else {
                return (cb.desc(root.join("executor").get("name")));
            }
        }
    },
    DATE() {
        public Order getSortOrder(Root<Agreement> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("date"));
            } else {
                return cb.desc(root.get("date"));
            }
        }
    };

    public Order getSortOrder(Root<Agreement> root, CriteriaBuilder cb, String sortOrder) {
        return null;
    }
}
