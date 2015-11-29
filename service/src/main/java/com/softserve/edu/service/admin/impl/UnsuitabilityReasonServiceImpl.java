package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.device.UnsuitabilityReason;
import com.softserve.edu.repository.DeviceRepository;
import com.softserve.edu.repository.UnsuitabilityReasonRepository;
import com.softserve.edu.service.admin.UnsuitabilityReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class UnsuitabilityReasonServiceImpl implements UnsuitabilityReasonService {
    @Autowired
    private UnsuitabilityReasonRepository unsuitabilityReasonRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @PersistenceContext
    private EntityManager em;

    @Override
    public void addUnsuitabilityReason(String name, Long counterId) {
        Device device = deviceRepository.findOne(counterId);
        unsuitabilityReasonRepository.save(new UnsuitabilityReason(name, device));
    }

    @Override
    public void removeUnsuitabilityReason(Long id) {

        unsuitabilityReasonRepository.delete(id);
    }

    @Override
    public List<UnsuitabilityReason> findUnsuitabilityReasonsPagination(int pageNumber, int itemsPerPage) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UnsuitabilityReason> cq = cb.createQuery(UnsuitabilityReason.class);
        Root<UnsuitabilityReason> reasons = cq.from(UnsuitabilityReason.class);
        TypedQuery<UnsuitabilityReason> typedQuery = em.createQuery(cq);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);

        return typedQuery.getResultList();
    }
}










