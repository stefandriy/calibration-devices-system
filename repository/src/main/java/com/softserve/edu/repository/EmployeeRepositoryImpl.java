package com.softserve.edu.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by MAX on 14.07.2015.
 */
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @PersistenceContext
    	    private EntityManager entityManager;

}
