package com.softserve.edu.entity.enumeration.verification;

import com.softserve.edu.entity.verification.Verification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public enum Status {
    SENT,
    ACCEPTED,
    REJECTED,
    CREATED_BY_CALIBRATOR,
    IN_PROGRESS,
    PLANNING_TASK,
    TASK_PLANED,
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
