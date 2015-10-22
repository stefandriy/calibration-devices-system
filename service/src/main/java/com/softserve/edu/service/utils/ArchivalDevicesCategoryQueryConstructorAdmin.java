package com.softserve.edu.service.utils;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

public class ArchivalDevicesCategoryQueryConstructorAdmin {
    static Logger logger = Logger.getLogger(ArchivalDevicesCategoryQueryConstructorAdmin.class);

    public static CriteriaQuery<Device> buildSearchQuery(String number, String deviceType, String deviceName, String sortCriteria, String sortOrder, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Device> criteriaQuery = cb.createQuery(Device.class);
        Root<Device> root = criteriaQuery.from(Device.class);

        Predicate predicate = ArchivalDevicesCategoryQueryConstructorAdmin.buildPredicate(number, deviceType, deviceName, root, cb);
        if((sortCriteria != null)&&(sortOrder != null)) {
            criteriaQuery.orderBy(SortCriteriaDeviceCategory.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, cb, sortOrder));
        } else {
            criteriaQuery.orderBy(cb.desc(root.get("number")));
        }
        criteriaQuery.select(root);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }

    public static CriteriaQuery<Long> buildCountQuery (String number, String deviceType, String deviceName, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Device> root = countQuery.from(Device.class);

        Predicate predicate = ArchivalDevicesCategoryQueryConstructorAdmin.buildPredicate(number, deviceType, deviceName, root, cb);
        countQuery.select(cb.count(root));
        countQuery.where(predicate);

        return countQuery;
    }
    private static Predicate buildPredicate(String number, String deviceType, String deviceName, Root<Device> root, CriteriaBuilder cb) {
        Predicate queryPredicate = cb.conjunction();
        if ((number != null)&&(number.length()>0)) {
            queryPredicate = cb.and(cb.like(root.get("number"), "%" + number + "%"), queryPredicate);
        }
        if ((deviceType != null)&&(deviceType.length()>0)) {
            queryPredicate = cb.and(cb.equal(root.get("deviceType"),
                    DeviceType.valueOf(deviceType.trim())), queryPredicate);
        }
        if ((deviceName != null)&&(deviceName.length()>0)) {
            queryPredicate = cb.and(
                    cb.like(root.get("deviceName"), "%" + deviceName + "%"),
                    queryPredicate);
        }

        return queryPredicate;
    }
}
