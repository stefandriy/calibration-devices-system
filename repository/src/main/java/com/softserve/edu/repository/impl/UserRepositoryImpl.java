package com.softserve.edu.repository.impl;

import com.softserve.edu.entity.enumeration.user.EmployeeRole;
import com.softserve.edu.repository.catalogue.UserRepositoryCustomisation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by Bogdan on 14.10.2015.
 */
@Repository
public class UserRepositoryImpl implements UserRepositoryCustomisation {

    private static final String COUNT_VERIFICATION_QUERY_PREFIX = "SELECT COUNT(v) FROM Verification v ";
    private static final String USERNAME_PARAMETER = ":username";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long countEmployeeVerifications(EmployeeRole employeeRole, String username) {

        String queryStatement = COUNT_VERIFICATION_QUERY_PREFIX;

        switch (employeeRole) {
            case PROVIDER:
                queryStatement =
                        COUNT_VERIFICATION_QUERY_PREFIX +
                                "JOIN v.providerEmployee " +
                                "WHERE v.providerEmployee.username = " +
                                USERNAME_PARAMETER;
                break;
            case CALIBRATOR:
                queryStatement =
                        COUNT_VERIFICATION_QUERY_PREFIX +
                                "JOIN v.calibratorEmployee " +
                                "WHERE v.calibratorEmployee.username = " +
                                USERNAME_PARAMETER;
                break;
            case STATE_VERIFICATOR:
                queryStatement =
                        COUNT_VERIFICATION_QUERY_PREFIX +
                                "JOIN v.stateVerificatorEmployee " +
                                "WHERE v.stateVerificatorEmployee.username = " +
                                USERNAME_PARAMETER;
                break;
        }

        TypedQuery<Long> query = entityManager.createQuery(queryStatement, Long.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }
}
