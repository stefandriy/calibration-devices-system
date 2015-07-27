package com.softserve.edu.entity.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.softserve.edu.entity.Verification;

public enum Status {
    SENT() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.SENT);
        	return queryPredicate;
        }
    }, 
    ACCEPTED() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.ACCEPTED);
        	return queryPredicate;
        }
    },
    REJECTED() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.REJECTED);
        	return queryPredicate;
        }
    },
    IN_PROGRESS() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.IN_PROGRESS);
        	return queryPredicate;
        }
    },
    TEST_PLACE_DETERMINED() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.TEST_PLACE_DETERMINED);
        	return queryPredicate;
        }
    }, 
    SENT_TO_TEST_DEVICE() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.SENT_TO_TEST_DEVICE);
        	return queryPredicate;
        }
    },
    TEST_COMPLETED() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.TEST_COMPLETED);
        	return queryPredicate;
        }
    },
    SENT_TO_VERIFICATOR() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.SENT_TO_VERIFICATOR);
        	return queryPredicate;
        }
    },
    TEST_OK() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.TEST_OK);
        	return queryPredicate;
        }
    },
    TEST_NOK() {
    	public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    		Predicate queryPredicate = cb.equal(root.get("status"), Status.TEST_NOK);
        	return queryPredicate;
        }
    };
        
    public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
    	return null;
    }
}
