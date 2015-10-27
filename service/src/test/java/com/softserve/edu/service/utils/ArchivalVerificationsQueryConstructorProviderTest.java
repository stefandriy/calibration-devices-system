package com.softserve.edu.service.utils;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

/**
 * @author Veronika Herasymenko
 */
@RunWith(MockitoJUnitRunner.class)
public class ArchivalVerificationsQueryConstructorProviderTest {

    @Mock
    User providerEmployee;

    @Mock
    Predicate predicate;

    @Mock
    EntityManager em;

    @Mock
    CriteriaBuilder cb;

    @Mock
    Verification verification;

    @InjectMocks
    ArchivalVerificationsQueryConstructorProvider archivalVerificationsQueryConstructorProvider;

    public void testBuildSearchQuery() {

        when(em.getCriteriaBuilder()).thenReturn(cb);
        CriteriaQuery<Verification> criteriaQuery = (CriteriaQuery<Verification>) mock(CriteriaQuery.class);
        when(cb.createQuery(Verification.class)).thenReturn(criteriaQuery);



       // archivalVerificationsQueryConstructorProvider.buildSearchQuery();
    }

}
