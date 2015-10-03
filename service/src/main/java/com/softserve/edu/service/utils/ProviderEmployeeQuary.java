package com.softserve.edu.service.utils;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;



/**
 * Created by MAX on 11.07.2015.
 */
public class ProviderEmployeeQuary {

    static Logger logger = Logger.getLogger(ArchivalVerificationsQueryConstructorProvider.class);

    public static CriteriaQuery<User> buildSearchQuery(String userName, String role, String firstName,
                                                       String lastName, String organization, String telephone,
                                                       EntityManager em, Long idOrganization, String fieldToSort) {


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Join<User, Organization> joinSearch = root.join("organization");
        Join<User, UserRole> joinRole = root.join("userRoles");

        Predicate predicate = ProviderEmployeeQuary.buildPredicate(root, cb, joinRole, joinSearch, userName,
                role, firstName, lastName, organization, telephone, idOrganization);
        if (fieldToSort.length() > 0) {
            if (fieldToSort.substring(0, 1).equals("-")) {
                if (fieldToSort.substring(1, fieldToSort.length()).equals("role")) {
                    criteriaQuery.orderBy(cb.desc(joinRole.get(fieldToSort.substring(1, fieldToSort.length()))));
                } else {
                    criteriaQuery.orderBy(cb.desc(root.get(fieldToSort.substring(1, fieldToSort.length()))));
                }
            } else {
                if (fieldToSort.equals("role")) {
                    criteriaQuery.orderBy(cb.asc(joinRole.get(fieldToSort)));
                } else {
                    criteriaQuery.orderBy(cb.asc(root.get(fieldToSort)));
                }
            }
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
        if (idOrganization != null) {
            queryPredicate = cb.and(cb.equal(joinSearch.get("id"), idOrganization), queryPredicate);
        }
        if (!(userName == null) && !(userName.isEmpty())) {
            queryPredicate = cb.and(cb.like(root.get("username"), "%" + userName + "%"), queryPredicate);
        }
        if (!(role == null) && !(role.isEmpty())) {

            Join<User, UserRole> joinUserRole = root.join("username");
            logger.debug("ProviderEmployeeQuequal root = " + role );
            queryPredicate = cb.and(cb.equal(root.get(UserRole.PROVIDER_EMPLOYEE.name()) , role),queryPredicate);
                    //UserRole.valueOf(role.trim())), queryPredicate);

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
