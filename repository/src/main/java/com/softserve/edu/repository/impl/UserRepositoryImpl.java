package com.softserve.edu.repository.impl;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.repository.catalogue.UserRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private static final String COUNT_VERIFICATION_QUERY_PREFIX = "SELECT COUNT(v) FROM Verification v ";
    private static final String USERNAME_PARAMETER = ":username";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long countEmployeeVerifications(UserRole employeeRole, String username) {

        String queryStatement = COUNT_VERIFICATION_QUERY_PREFIX;

        switch (employeeRole) {
            case PROVIDER_EMPLOYEE:
                queryStatement =
                        COUNT_VERIFICATION_QUERY_PREFIX +
                                "JOIN v.providerEmployee " +
                                "WHERE v.providerEmployee.username = " +
                                USERNAME_PARAMETER;
                break;
            case CALIBRATOR_EMPLOYEE:
                queryStatement =
                        COUNT_VERIFICATION_QUERY_PREFIX +
                                "JOIN v.calibratorEmployee " +
                                "WHERE v.calibratorEmployee.username = " +
                                USERNAME_PARAMETER;
                break;
            case STATE_VERIFICATOR_EMPLOYEE:
                queryStatement =
                        COUNT_VERIFICATION_QUERY_PREFIX +
                                "JOIN v.stateVerificatorEmployee " +
                                "WHERE v.stateVerificatorEmployee.username = " +
                                USERNAME_PARAMETER;
                break;
            default:
                throw new IllegalArgumentException(
                        "Provided UserRole '" + employeeRole + "' is incorrect. Only employee can have verifications assigned"
                );
        }

        TypedQuery<Long> query = entityManager.createQuery(queryStatement, Long.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }
}
