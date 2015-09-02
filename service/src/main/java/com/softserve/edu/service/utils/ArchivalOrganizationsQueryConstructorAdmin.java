package com.softserve.edu.service.utils;

import com.softserve.edu.entity.Organization;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

/**
 * Created by vova on 02.09.15.
 */
public class ArchivalOrganizationsQueryConstructorAdmin {
    static Logger logger = Logger.getLogger(ArchivalOrganizationsQueryConstructorAdmin.class);

    public static CriteriaQuery<Organization> buildSearchQuery(String sortCriteria, String sortOrder,EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Organization> criteriaQuery = cb.createQuery(Organization.class);
        Root<Organization> root = criteriaQuery.from(Organization.class);

        Join<Organization, Organization> calibratorJoin = root.join("stateVerificator");

        Predicate predicate = ArchivalOrganizationsQueryConstructorAdmin.buildPredicate(root, cb, calibratorJoin);
        if((sortCriteria != null)&&(sortOrder != null)) {
            criteriaQuery.orderBy(SortCriteriaOrganization.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, cb, sortOrder));
        } else {
            criteriaQuery.orderBy(cb.desc(root.get("id")));
        }
        criteriaQuery.select(root);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }

    private static Predicate buildPredicate(Root<Organization> root, CriteriaBuilder cb, Join<Organization, Organization> calibratorJoin) {
        return null;
    }
}
