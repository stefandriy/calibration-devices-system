package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.device.UnsuitabilityReason;
import com.softserve.edu.repository.DeviceRepository;
import com.softserve.edu.repository.UnsuitabilityReasonRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnsuitabilityReasonServiceImplTest {
    @Mock
    private EntityManager em;
    @Mock
    private UnsuitabilityReason unsuitabilityReason;
    @Mock
    private Device device;
    @Mock
    private UnsuitabilityReasonRepository unsuitabilityReasonRepository;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private CriteriaBuilder cb;
    @Mock
    CriteriaQuery<UnsuitabilityReason> cq;
    @Mock
    Root<UnsuitabilityReason> reasons;

    @Mock
    TypedQuery<UnsuitabilityReason> typedQuery;
    @InjectMocks
    private UnsuitabilityReasonServiceImpl unsuitabilityReasonService;

    private long id;
    private String reasonName;
    @Before
    public void initializeMockito() {
        MockitoAnnotations.initMocks(this);
        id = 1;
        reasonName = "reason name";
        when(deviceRepository.findOne(id)).thenReturn(device);
        unsuitabilityReason = new UnsuitabilityReason(reasonName, device);
    }
    @After
    public void tearDown() {
        reasonName = null;
    }
    @Test
    public void testAddUnsuitabilityReason() throws Exception {
        unsuitabilityReasonService.addUnsuitabilityReason(reasonName, id);
        verify(deviceRepository).findOne(id);
        verify(unsuitabilityReasonRepository).save(new UnsuitabilityReason(reasonName, deviceRepository.findOne(id)));
    }

    @Test
    public void testRemoveUnsuitabilityReason() throws Exception {
        unsuitabilityReasonService.removeUnsuitabilityReason(id);
        verify(unsuitabilityReasonRepository).delete(id);
    }

    @Test
    public void testFindUnsuitabilityReasonsPagination() throws Exception {

        List<UnsuitabilityReason> expected = Collections.singletonList(unsuitabilityReason);
        int pageNumber = 1;
        int itemsPerPage = 1;
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(UnsuitabilityReason.class)).thenReturn(cq);
        when(cq.from(UnsuitabilityReason.class)).thenReturn(reasons);
        when(em.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expected);
        List<UnsuitabilityReason> actual = unsuitabilityReasonService.findUnsuitabilityReasonsPagination(pageNumber, itemsPerPage);
        verify(typedQuery).setFirstResult((pageNumber - 1) * itemsPerPage);
        verify(typedQuery).setMaxResults(itemsPerPage);

        Assert.assertEquals(expected, actual);
    }
}