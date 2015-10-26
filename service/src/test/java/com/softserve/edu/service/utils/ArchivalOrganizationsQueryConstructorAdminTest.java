package com.softserve.edu.service.utils;

import com.softserve.edu.entity.organization.Organization;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by My on 10/26/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ArchivalOrganizationsQueryConstructorAdminTest {
    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilde;

    @Mock
    private CriteriaQuery<Organization> criteriaQuery;

    @Mock
    CriteriaQuery<Long> countQueryForBuildCountQuery;

    @Mock
    private Root<Organization> root;

    @Mock
    private Predicate predicate;

    @Mock
    private Answer<Integer> answer;


    private String name, email, phone, type, region, district, locality, streetToSearch;

    private String sortCriteria ;

    private String sortOrder ;

    public ArchivalOrganizationsQueryConstructorAdminTest() {
        name = null;
        email = null;
        phone = null;
        type = null;
        region = null;
        district = null;
        locality = null;
        streetToSearch = null;
        sortCriteria = "id";
        sortOrder = "desc";

    }

    @Before
    public void setUp(){
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilde);
        when(criteriaBuilde.createQuery(Organization.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Organization.class)).thenReturn(root);
    }


    @Test
    public void buildSearchQueryVerifySortCriteriaANDsortOrder() {
        ArchivalOrganizationsQueryConstructorAdmin.buildSearchQuery(name, email, phone, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager);
        verify(criteriaQuery, times(1)).orderBy(SortCriteriaOrganization.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, criteriaBuilde, sortOrder));
    }

    @Test
    public void buildSearchQueryNotNullCriteriaQuery() {
        CriteriaQuery<Organization> criteriaQuery = ArchivalOrganizationsQueryConstructorAdmin
                .buildSearchQuery(name, email, phone, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager);
        assertNotNull("criteriaQuery present", criteriaQuery);
    }

    @Test
    public void buildSearchQueryVerifySortCriteriaANDsortOrderNULL() {
        sortOrder = null;
        ArchivalOrganizationsQueryConstructorAdmin.buildSearchQuery(name, email, phone, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager);
        verify(criteriaQuery, times(1)).orderBy(criteriaBuilde.desc(root.get("id")));
    }

    @Test
    public void buildSearchQueryVerifySortCriteriaNULLANDsortOrder() {
        sortCriteria = null;
        ArchivalOrganizationsQueryConstructorAdmin.buildSearchQuery(name, email, phone, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager);
        verify(criteriaQuery, times(1)).orderBy(criteriaBuilde.desc(root.get("id")));
    }

    @Test
    public void buildCountQueryNotNullCriteriaQuery(){
        when(criteriaBuilde.createQuery(Long.class)).thenReturn(countQueryForBuildCountQuery);
        CriteriaQuery<Long> criteriaQuery = ArchivalOrganizationsQueryConstructorAdmin
                .buildCountQuery(name, email, phone, type, region, district, locality, streetToSearch, entityManager);
        assertNotNull("criteriaQuery present", criteriaQuery);
    }






}
