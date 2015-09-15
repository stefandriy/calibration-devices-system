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

    public static CriteriaQuery<Organization> buildSearchQuery(/*Long id,*/ String name,
                                                               String email, String phone, String type, String region, String district, String locality, String streetToSearch, String sortCriteria, String sortOrder, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Organization> criteriaQuery = cb.createQuery(Organization.class);
        Root<Organization> root = criteriaQuery.from(Organization.class);
      //  Join<Organization, User> organizationUsersJoin = root.join("organization");

        Predicate predicate = ArchivalOrganizationsQueryConstructorAdmin.buildPredicate(/*id,*/ name, email, type, phone, region, district, locality, streetToSearch, root, cb/*, organizationUsersJoin*/);
        if((sortCriteria != null)&&(sortOrder != null)) {
            criteriaQuery.orderBy(SortCriteriaOrganization.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, cb, sortOrder));
        } else {
            criteriaQuery.orderBy(cb.desc(root.get("id")));
        }
        criteriaQuery.select(root);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }

    public static CriteriaQuery<Long> buildCountQuery (/*Long id,*/ String name,
                                                       String email, String phone, String type, String region, String district, String locality, String streetToSearch, String sortCriteria, String sortOrder,EntityManager em /*, Join<Organization, User> organizationUsersJoin*/) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Organization> root = countQuery.from(Organization.class);
    //    Join<Organization, OrganizationType> organizationTypeJoin = root.join("organizationId");

        Predicate predicate = ArchivalOrganizationsQueryConstructorAdmin.buildPredicate(/*id*/ name, email, type, phone, region, district, locality, streetToSearch, root, cb/*, organizationUsersJoin*/);
        countQuery.select(cb.count(root));
        countQuery.where(predicate);
        return countQuery;
    }
    private static Predicate buildPredicate(/*Long id,*/ String name,
                                             String email, String phone, String type, String region, String district, String locality, String streetToSearch, Root<Organization> root, CriteriaBuilder cb/*, Join<Organization, User> organizationUsersJoin*/) {
        Predicate queryPredicate = cb.conjunction();
      //  queryPredicate = cb.and(cb.equal(organizationUsersJoin .get(""), employeeId), queryPredicate);
        //Predicate<String> i  = (s)-> s.length() > 5;
        //return p -> p.getAge() > 21 && p.getGender().equalsIgnoreCase("M");
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
            Join<Organization, OrganizationType> joinOrganizationType = root.join("organizationTypes");
            Predicate searchByOrganizationType = cb.like(joinOrganizationType.get("type"),
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

  /*      Join<User, Organization> joinOrganizationUser = root.join("organization");

        Predicate searchUserName = cb.like(joinOrganizationUser.get("firstName"),
                "%" + "" +  "%");
        queryPredicate = cb.and(searchUserName, queryPredicate);*/

        return queryPredicate;
    }
}
