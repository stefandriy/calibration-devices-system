package com.softserve.edu.entity.util;

import com.softserve.edu.entity.Verification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public enum Status {
    SENT,
    ACCEPTED,
    REJECTED,
    IN_PROGRESS,
    TEST_PLACE_DETERMINED,
    SENT_TO_TEST_DEVICE,
    TEST_COMPLETED,
    SENT_TO_VERIFICATOR,
    TEST_OK,
    TEST_NOK;

    public Predicate getQueryPredicate(Root<Verification> root, CriteriaBuilder cb) {
        return cb.equal(root.get("status"), this);
    }
}
