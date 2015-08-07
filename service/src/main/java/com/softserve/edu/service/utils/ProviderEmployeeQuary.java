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
                                                       String lastName, String organization, String telephone,
                                                       EntityManager em, Long idOrganization, String sortingLastName) {
        int sortLastName = Integer.valueOf(sortingLastName);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Join<User, Organization> joinSearch = root.join("organization");
        Join<User, UserRole> joinRole = root.join("userRoles");


        Predicate predicate = ProviderEmployeeQuary.buildPredicate(root, cb, joinRole, joinSearch, userName,
                role, firstName, lastName, organization, telephone, idOrganization);
        if (sortLastName < 0) {
            criteriaQuery.orderBy(cb.desc(root.get("lastName")));
        } else {
            criteriaQuery.orderBy(cb.asc(root.get("lastName")));
        }

        criteriaQuery.select(root).distinct(true);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }


    private static Predicate buildPredicate(Root<User> root, CriteriaBuilder cb, Join<User, UserRole> joinRole,
                                            Join<User, Organization> joinSearch, String userName, String role,
                                            String firstName, String lastName, String organization, String telephone,
                                            Long idOrganization) {
        Predicate queryPredicate = cb.conjunction();

        queryPredicate = cb.and(cb.equal(joinSearch.get("id"), idOrganization), queryPredicate);

        if (!(userName == null) && !(userName.isEmpty())) {
            queryPredicate = cb.and(cb.like(root.get("username"), "%" + userName + "%"), queryPredicate);
        }
        if (!(role == null) && !(role.isEmpty())) {
            queryPredicate = cb.and(cb.like(joinRole.get("role"), "%" + role + "%"), queryPredicate);

        }
        if (!(firstName == null) && !(firstName.isEmpty())) {
            queryPredicate = cb.and(cb.like(root.get("firstName"), "%" + firstName + "%"), queryPredicate);
        }
        if (!(lastName == null) && !(lastName.isEmpty())) {
            queryPredicate = cb.and(cb.like(root.get("lastName"), "%" + lastName + "%"), queryPredicate);
        }
        if (!(organization == null) && !(organization.isEmpty())) {
            queryPredicate = cb.and(cb.like(root.get("organization").get("name"), "%" + organization + "%"), queryPredicate);
        }
        if (!(telephone == null) && !(telephone.isEmpty())) {
            queryPredicate = cb.and(cb.like(root.get("phone"), "%" + telephone + "%"), queryPredicate);
        }

        return queryPredicate;
    }

    public static CriteriaQuery<Long> buildCountQuery(String userName, String role, String firstName,
                                                      String lastName, String organization, String telephone,
                                                      Long idOrganization, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> root = countQuery.from(User.class);
        Join<User, Organization> joinSearch = root.join("organization");
        Join<User, UserRole> joinRole = root.join("userRoles");
        Predicate predicate = ProviderEmployeeQuary.buildPredicate(root, cb, joinRole, joinSearch, userName, role,
                firstName, lastName, organization, telephone, idOrganization);

        countQuery.select(cb.countDistinct(root));
        countQuery.where(predicate);
        return countQuery;


    }


}