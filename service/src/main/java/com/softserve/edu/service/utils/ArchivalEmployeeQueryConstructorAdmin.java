package com.softserve.edu.service.utils;


import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

public class ArchivalEmployeeQueryConstructorAdmin {
    static Logger logger = Logger.getLogger(ArchivalEmployeeQueryConstructorAdmin.class);

    public static CriteriaQuery<User> buildSearchQuery(String userName, String role, String firstName, String lastName, String organization, String telephone,
                                                        String sortCriteria, String sortOrder, EntityManager em) {


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        Predicate predicate = ArchivalEmployeeQueryConstructorAdmin.buildPredicate(root, cb, userName,
                role, firstName, lastName, organization, telephone);

        logger.info("sortcriteria");
        logger.info(sortCriteria);
        if ((sortCriteria != null) && (sortOrder != null)) {
            criteriaQuery.orderBy(SortCriteriaUser.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, cb, sortOrder));
        } else {
            criteriaQuery.orderBy(cb.desc(root.get("username")));
        }
        criteriaQuery.select(root).distinct(true);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }


    private static Predicate buildPredicate(Root<User> root, CriteriaBuilder cb, String userName, String role,
                                            String firstName, String lastName, String organization, String telephone) {
        Predicate queryPredicate = cb.conjunction();
        if (!(userName == null) && !(userName.isEmpty())) {
            queryPredicate = cb.and(cb.like(root.get("username"), "%" + userName + "%"), queryPredicate);
        }
        if (!(role == null) && !(role.isEmpty())) {
            UserRole uRole = UserRole.valueOf(role.trim());
            queryPredicate = cb.and(cb.isMember(uRole, root.get("userRoles")), queryPredicate);
        }else {
            queryPredicate = cb.and(cb.not(cb.isMember(UserRole.SYS_ADMIN, root.get("userRoles"))));
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
                                                      String lastName, String organization, String telephone, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> root = countQuery.from(User.class);
        Predicate predicate = ArchivalEmployeeQueryConstructorAdmin.buildPredicate(root, cb, userName, role,
                firstName, lastName, organization, telephone);

        countQuery.select(cb.countDistinct(root));
        countQuery.where(predicate);
        return countQuery;
    }
}
