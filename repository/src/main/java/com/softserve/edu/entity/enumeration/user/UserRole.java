package com.softserve.edu.entity.enumeration.user;

import com.softserve.edu.entity.user.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public enum UserRole {
    SUPER_ADMIN,
    SYS_ADMIN,
    PROVIDER_EMPLOYEE,
    CALIBRATOR_EMPLOYEE,
    STATE_VERIFICATOR_EMPLOYEE,
    CALIBRATOR_ADMIN,
    PROVIDER_ADMIN,
    STATE_VERIFICATOR_ADMIN;

}
