package com.softserve.edu.service.utils;

import com.softserve.edu.entity.verification.calibration.CalibrationTest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;


public enum SortCriteriaCalibrationTest {
    DATE() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("dateTest"));
            } else {
                return cb.desc(root.get("dateTest"));
            }
        }
    },
    CLIENT_LAST_NAME() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.join("verificationId").get("clientData").get("lastName"));
            } else {
                return cb.desc(root.join("verificationId").get("clientData").get("lastName"));
            }
        }
    },
    CLIENT_FIRST_NAME() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.join("verificationId").get("clientData").get("firstName"));
            } else {
                return cb.desc(root.join("verificationId").get("clientData").get("firstName"));
            }
        }
    },
    STREET() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.join("verificationId").get("clientData").get("clientAddress").get("street"));
            } else {
                return cb.desc(root.join("verificationId").get("clientData").get("clientAddress").get("street"));
            }
        }
    },
    DISTRICT() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {

            if (sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.join("verificationId").get("clientData").get("clientAddress").get("district"));
            } else {
                return cb.desc(root.join("verificationId").get("clientData").get("clientAddress").get("district"));
            }
        }
    },

    REGION() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {

            if (sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.join("verificationId").get("clientData").get("clientAddress").get("region"));
            } else {
                return cb.desc(root.join("verificationId").get("clientData").get("clientAddress").get("region"));
            }
        }
    },

    LOCALITY() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {

            if (sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.join("verificationId").get("clientData").get("clientAddress").get("locality"));
            } else {
                return cb.desc(root.join("verificationId").get("clientData").get("clientAddress").get("locality"));
            }
        }
    },
    MEASUREMENT_DEVICE_ID() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return (cb.asc(root.join("verificationId").join("device").get("id")));
            } else {
                return (cb.desc(root.join("verificationId").join("device").get("id")));
            }
        }
    },
    MEASUREMENT_DEVICE_TYPE() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return (cb.asc(root.join("verificationId").join("device").get("deviceType")));
            } else {
                return (cb.desc(root.join("verificationId").join("device").get("deviceType")));
            }
        }
    },
    PROTOCOL_ID(){
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return (cb.asc(root.get("id")));
            } else {
                return (cb.desc(root.get("id")));
            }
        }
    },
    PROTOCOL_STATUS() {
        public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return (cb.asc(root.get("testResult")));
            } else {
                return (cb.desc(root.get("testResult")));
            }
        }
    };

    public Order getSortOrder(Root<CalibrationTest> root, CriteriaBuilder cb, String sortOrder) {
        return null;
    }

}
