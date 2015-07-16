package com.softserve.edu.service.utils;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

/**
 * Created by MAX on 11.07.2015.
 */
public class ProviderEmployeeQuary {

    public static CriteriaQuery<User> buildSearchQuery(String userName, String role, String firstName,
                                                                   String lastName, String organization, String telephone, Long numberOfWork,
                                                                   EntityManager em, Long idOrganization) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Join<User, Organization> joinSearch = root.join("organization");
        Join<User, UserRole> joinRole = root.join("userRoles");


        Predicate predicate = ProviderEmployeeQuary.buildPredicate(root, cb,joinRole, joinSearch, userName, role, firstName, lastName, organization, telephone, idOrganization, numberOfWork);

        criteriaQuery.orderBy(cb.desc(root.get("lastName")));
        criteriaQuery.select(root);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }


    private static Predicate buildPredicate(Root<User> root, CriteriaBuilder cb,Join<User, UserRole> joinRole, Join<User, Organization> joinSearch,
                                            String userName, String role, String firstName, String lastName, String organization, String telephone,
                                            Long idOrganization, Long numberOfWork) {
        Predicate queryPredicate = cb.conjunction();

        queryPredicate = cb.and(cb.equal(joinSearch.get("id"), idOrganization));
         queryPredicate = cb.and(cb.equal(joinRole.get("role"), role),queryPredicate);

        if (!(userName == null)) {
            queryPredicate = cb.and(cb.like(root.get("username"), "%" + userName + "%"), queryPredicate);
        }
//        if (!(role == null)) {
//            queryPredicate = cb.and(cb.like(root.get("role"), "%" + role + "%"), queryPredicate);
//        }
        if (!(firstName == null)) {
            queryPredicate = cb.and(cb.like(root.get("firstName"), "%" + firstName + "%"), queryPredicate);
        }
        if (!(lastName == null)) {
            queryPredicate = cb.and(cb.like(root.get("lastName"), "%" + lastName + "%"), queryPredicate);
        }
        if (!(organization == null)) {
            queryPredicate = cb.and(cb.like(root.get("organization").get("name"), "%" + organization + "%"), queryPredicate);
        }
        if (!(telephone == null)) {
            queryPredicate = cb.and(cb.like(root.get("phone"), "%" + telephone + "%"), queryPredicate);
        }
        if (!(numberOfWork == null)) {
            queryPredicate = cb.and(cb.equal(root.get("countOfWork"), numberOfWork), queryPredicate);
        }
        return queryPredicate;
    }

    public static CriteriaQuery<Long> buildCountQuery(String userName, String role, String firstName,
                                                      String lastName, String organization, String telephone, Long numberOfWork,
                                                      Long idOrganization, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> root = countQuery.from(User.class);
        Join<User, Organization> joinSearch = root.join("organization");
        Join<User, UserRole> joinRole = root.join("userRoles");
        Predicate predicate = ProviderEmployeeQuary.buildPredicate(root, cb,joinRole, joinSearch, userName, role, firstName, lastName, organization, telephone, idOrganization, numberOfWork);
        countQuery.select(cb.count(root));
        countQuery.where(predicate);
        return countQuery;


    }


}