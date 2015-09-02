package com.softserve.edu.service.utils;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;
import com.softserve.edu.entity.user.User;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

/**
 * Created by vova on 02.09.15.
 */
public class ArchivalOrganizationsQueryConstructorAdmin {
    static Logger logger = Logger.getLogger(ArchivalOrganizationsQueryConstructorAdmin.class);

    public static CriteriaQuery<Organization> buildSearchQuery(String name,
                                                               String email, String number, String type, String sortCriteria, String sortOrder, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Organization> criteriaQuery = cb.createQuery(Organization.class);
        Root<Organization> root = criteriaQuery.from(Organization.class);

        Join<Organization, OrganizationType> organizationTypeJoin = root.join("organizationId");

        Predicate predicate = ArchivalOrganizationsQueryConstructorAdmin.buildPredicate(name, email, type, number,root, cb, organizationTypeJoin);
        if((sortCriteria != null)&&(sortOrder != null)) {
            criteriaQuery.orderBy(SortCriteriaOrganization.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, cb, sortOrder));
        } else {
            criteriaQuery.orderBy(cb.desc(root.get("id")));
        }
        criteriaQuery.select(root);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }

    public static CriteriaQuery<Long> buildCountQuery (String name,
                                                       String email, String number, String type, String sortCriteria, String sortOrder,EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Organization> root = countQuery.from(Organization.class);
        Join<Organization, OrganizationType> organizationTypeJoin = root.join("organizationId");

        Predicate predicate = ArchivalOrganizationsQueryConstructorAdmin.buildPredicate(name, email, type, number,root, cb, organizationTypeJoin);
        countQuery.select(cb.count(root));
        countQuery.where(predicate);
        return countQuery;
    }
    private static Predicate buildPredicate( String name,
                                             String email, String number, String type, Root<Organization> root, CriteriaBuilder cb, Join<Organization, OrganizationType> organizationTypeJoin ) {
        Predicate queryPredicate = cb.conjunction();
       // queryPredicate = cb.and(cb.equal(organizationTypeJoin .get(""), employeeId), queryPredicate);

        if ((name != null)&&(name.length()>0)) {
            queryPredicate = cb.and(cb.like(root.get("name"), "%" + name + "%"), queryPredicate);
        }
        if ((email != null)&&(email.length()>0)) {
            queryPredicate = cb.and(cb.like(root.get("email"), "%" + email + "%"),
                    queryPredicate);
        }
        if ((number != null)&&(number.length()>0)) {
            queryPredicate = cb.and(
                    cb.like(root.get("number"), "%" + number + "%"),
                    queryPredicate);
        }
       /* if ((type != null)&&(type.length()>0)) {
            Join<Organization, OrganizationType> joinOrganizationType = root.join("organizationId");
            Predicate searchByOrganizationType = cb.like(joinOrganizationType.get("type"),
                    "%" + type + "%");
            queryPredicate = cb.and(searchByOrganizationType, queryPredicate);
        }*/

        return queryPredicate;
    }
}
