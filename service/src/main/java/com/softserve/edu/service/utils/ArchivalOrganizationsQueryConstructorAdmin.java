package com.softserve.edu.service.utils;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

//TODO: What is going on here?!
public class ArchivalOrganizationsQueryConstructorAdmin {
    static Logger logger = Logger.getLogger(ArchivalOrganizationsQueryConstructorAdmin.class);

    public static CriteriaQuery<Organization> buildSearchQuery(String name,
                                                               String email, String phone, String type, String region, String district, String locality, String streetToSearch, String sortCriteria, String sortOrder, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Organization> criteriaQuery = cb.createQuery(Organization.class);
        Root<Organization> root = criteriaQuery.from(Organization.class);

        Predicate predicate = ArchivalOrganizationsQueryConstructorAdmin.buildPredicate( name, email, type, phone, region, district, locality, streetToSearch, root, cb);
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
                                                       String email, String phone, String type, String region, String district, String locality, String streetToSearch, EntityManager em ) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Organization> root = countQuery.from(Organization.class);

        Predicate predicate = ArchivalOrganizationsQueryConstructorAdmin.buildPredicate(name, email, type, phone, region, district, locality, streetToSearch, root, cb);
        countQuery.select(cb.count(root));
        countQuery.where(predicate);
        return countQuery;
    }
    private static Predicate buildPredicate( String name,
                                             String email, String phone, String type, String region, String district, String locality, String streetToSearch, Root<Organization> root, CriteriaBuilder cb) {
        Predicate queryPredicate = cb.conjunction();

        if ((name != null)&&(name.length()>0)) {
            queryPredicate = cb.and(cb.like(root.get("name"), "%" + name + "%"), queryPredicate);
        }
        if ((email != null)&&(email.length()>0)) {
            queryPredicate = cb.and(cb.like(root.get("email"), "%" + email + "%"),
                    queryPredicate);
        }
        if ((phone != null)&&(phone.length()>0)) {
            queryPredicate = cb.and(
                    cb.like(root.get("phone"), "%" + phone + "%"),
                    queryPredicate);
        }
        if ((type != null)&&(type.length()>0)) {
            Join<Organization, OrganizationType> joinOrganizationType = root.join("organizationId");
            Predicate searchByOrganizationType = cb.like(joinOrganizationType.get("value"),
                    "%" + type + "%");
            queryPredicate = cb.and(searchByOrganizationType, queryPredicate);
        }
        if ((streetToSearch != null)&&(streetToSearch.length()>0)) {
            queryPredicate = cb.and(
                    cb.like(root.get("address").get("street"), "%" + streetToSearch + "%"),
                    queryPredicate);
        }
        if ((region != null) && (region.length() > 0)) {
            queryPredicate = cb.and(
                    cb.like(root.get("address").get("region"), "%" + region + "%"),
                    queryPredicate);
        }
        if ((district != null) && (district.length() > 0)) {
            queryPredicate = cb.and(
                    cb.like(root.get("address").get("district"), "%" + district + "%"),
                    queryPredicate);
        }
        if ((locality != null) && (locality.length() > 0)) {
            queryPredicate = cb.and(
                    cb.like(root.get("address").get("locality"), "%" + locality + "%"),
                    queryPredicate);
        }


        return queryPredicate;
    }
}
