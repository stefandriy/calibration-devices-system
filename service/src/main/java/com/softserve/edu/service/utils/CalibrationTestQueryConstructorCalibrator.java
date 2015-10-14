package com.softserve.edu.service.utils;

import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.enumeration.verification.CalibrationTestResult;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class CalibrationTestQueryConstructorCalibrator {
    static Logger logger = Logger.getLogger(CalibrationTestQueryConstructorCalibrator.class);


    public static CriteriaQuery<CalibrationTest> buildSearchQuery(Long employeeId, String region, String district, String locality,
                                                               String idToSearch, String fullNameToSearch, String streetToSearch, String status, String employeeName,
                                                               Long protocolId, String protocolStatus,
                                                               Long measurementDeviceId,
                                                               String measurementDeviceType,
                                                               String sortCriteria, String sortOrder,  EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CalibrationTest> criteriaQuery = cb.createQuery(CalibrationTest.class);
        Root<CalibrationTest> root = criteriaQuery.from(CalibrationTest.class);

        Join<CalibrationTest, Verification> verificationJoin = root.join("verificationId");

        Predicate predicate = CalibrationTestQueryConstructorCalibrator.buildPredicate(root, cb, employeeId,  idToSearch, fullNameToSearch, region, district, locality, streetToSearch,
                status, employeeName, protocolId, protocolStatus, measurementDeviceId, measurementDeviceType,  verificationJoin);
        if ((sortCriteria != null) && (sortOrder != null)) {
            criteriaQuery.orderBy(SortCriteriaCalibrationTest.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, cb, sortOrder));
        } else {
            criteriaQuery.orderBy(cb.desc(root.get("dateTest")));
        }
        criteriaQuery.select(root);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }


    public static CriteriaQuery<Long> buildCountQuery(Long employeeId,
                                                      String idToSearch, String fullNameToSearch, String region, String district, String locality,
                                                      String streetToSearch, String status, String employeeName,
                                                      Long protocolId, String protocolStatus, Long measurementDeviceId, String measurementDeviceType,
                                                      EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CalibrationTest> root = countQuery.from(CalibrationTest.class);

        Join<CalibrationTest, Verification> verificationJoin = root.join("verificationId");
        Predicate predicate = CalibrationTestQueryConstructorCalibrator.buildPredicate(root, cb, employeeId,
                idToSearch, fullNameToSearch, region, district, locality, streetToSearch, status, employeeName, protocolId, protocolStatus, measurementDeviceId, measurementDeviceType, verificationJoin);
        countQuery.select(cb.count(root));
        countQuery.where(predicate);
        return countQuery;
    }

    private static Predicate buildPredicate(Root<CalibrationTest> root, CriteriaBuilder cb, Long employeeId,
                                            String idToSearch,
                                            String fullNameToSearch,String region, String district, String locality, String streetToSearch, String searchStatus,
                                            String employeeName, Long protocolId, String protocolStatus,
                                            Long measurementDeviceId, String measurementDeviceType,
                                            Join<CalibrationTest, Verification> verificationJoin) {

        Predicate queryPredicate = cb.conjunction();
        
//
//        if (startDateToSearch != null && endDateToSearch != null) {
//            DateTimeFormatter dbDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
//
//            LocalDate startDate = null;
//            LocalDate endDate = null;
//            try {
//                startDate = LocalDate.parse(startDateToSearch, dbDateTimeFormatter);
//                endDate = LocalDate.parse(endDateToSearch, dbDateTimeFormatter);
//            } catch (Exception pe) {
//                logger.error("Cannot parse date", pe); //TODO: add exception catching
//            }
//            //CalibrationTests with date between these two dates
//            queryPredicate = cb.and(cb.between(root.get("initialDate"), java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)), queryPredicate);
//
//        }

        if ((idToSearch != null) && (idToSearch.length() > 0)) {
            Join<CalibrationTest, Verification> joinCalibratorTest = root.join("calibrationTests");
            queryPredicate = cb.and(cb.like(root.get("id"), "%" + idToSearch + "%"), queryPredicate);
        }

        if ((fullNameToSearch != null) && (fullNameToSearch.length() > 0)) {
            Join<CalibrationTest, Verification> joinCalibratorTest = root.join("calibrationTests");
            Predicate searchByClientFirstName = cb.like(joinCalibratorTest.get("clientData").get("firstName"), "%" + fullNameToSearch + "%");
            Predicate searchByClientLastName = cb.like(joinCalibratorTest.get("clientData").get("lastName"), "%" + fullNameToSearch + "%");
            Predicate searchByClientMiddleName = cb.like(root.get("clientData").get("middleName"), "%" + fullNameToSearch + "%");
            Predicate searchPredicateByClientFullName = cb.or(searchByClientFirstName, searchByClientLastName, searchByClientMiddleName);
            queryPredicate = cb.and(searchPredicateByClientFullName, queryPredicate);
        }

        if ((streetToSearch != null) && (streetToSearch.length() > 0)) {
            Join<CalibrationTest, Verification> joinCalibratorTest = root.join("verificationId");
            queryPredicate = cb.and(
                    cb.like(joinCalibratorTest.get("clientData").get("clientAddress").get("street"), "%" + streetToSearch + "%"),
                    queryPredicate);
        }

        if ((region != null) && (region.length() > 0)) {
            Join<CalibrationTest, Verification> joinCalibratorTest = root.join("verificationId");
            queryPredicate = cb.and(
                    cb.like(joinCalibratorTest.get("clientData").get("clientAddress").get("region"), "%" + region + "%"),
                    queryPredicate);
        }

        if ((district != null) && (district.length() > 0)) {
            Join<CalibrationTest, Verification> joinCalibratorTest = root.join("verificationId");
            queryPredicate = cb.and(
                    cb.like(joinCalibratorTest.get("clientData").get("clientAddress").get("district"), "%" + district + "%"),
                    queryPredicate);
        }

        if ((locality != null) && (locality.length() > 0)) {
            Join<CalibrationTest, Verification> joinCalibratorTest = root.join("verificationId");
            queryPredicate = cb.and(
                    cb.like(joinCalibratorTest.get("clientData").get("clientAddress").get("locality"), "%" + locality + "%"),
                    queryPredicate);
        }
        if (measurementDeviceId != null) {
            Join<CalibrationTest, Verification> joinCalibratorTest = root.join("verificationId");
            queryPredicate = cb.and(cb.like(new FilteringNumbersDataLikeStringData<Long>(cb, joinCalibratorTest.get("device").get("id")),
                    "%" + measurementDeviceId.toString() + "%"), queryPredicate);
        }
        if (measurementDeviceType != null) {
            Join<CalibrationTest, Verification> joinCalibratorTest = root.join("verificationId");
            queryPredicate = cb.and(cb.equal(joinCalibratorTest.get("device").get("deviceType"),
                    DeviceType.valueOf(measurementDeviceType.trim())), queryPredicate);
        }
        if (protocolId != null) {
            queryPredicate = cb.and(cb.like(new FilteringNumbersDataLikeStringData<Long>(cb, root.get("id")),
                    "%" + protocolId.toString() + "%"), queryPredicate);

        }
        if (protocolStatus != null) {
            logger.debug("CalibrationTestQueryConstructorCalibrator : protocolStatus = " + protocolStatus);
            queryPredicate = cb.and(cb.equal(root.get("testResult"),
                    CalibrationTestResult.valueOf(protocolStatus.trim())), queryPredicate);
        }

        return queryPredicate;
    }
}
