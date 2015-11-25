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
import java.util.List;


/**
 * Created by Sonka on 23.11.2015.
 */
@Service
public class UnsuitabilityReasonServiceImpl implements UnsuitabilityReasonService {
    @Autowired
    private UnsuitabilityReasonRepository unsuitabilityReasonRepository;
    @Autowired
    private DeviceRepository deviceRepository;

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
    public List<UnsuitabilityReason> findAllUnsuitabilityReasons() {

        return unsuitabilityReasonRepository.findAll();
    }











    /*@Override
    public ListToPageTransformer<UnsuitabilityReason> getUnsuitabilityReasonBySearchAndPagination(int pageNumber, int itemsPerPage, Long id, String counterTypeName, String name, String sortCriteria, String sortOrder) {
        return new UnsuitabilityReason("Причина1", counterTypeRepository.findOne(1l));
    }*/
   /* @Transactional(readOnly = true)
    public List<Verification> findPageOfVerificationsByCalibratorIdAndStatus(
            User calibratorEmployee, int pageNumber, int itemsPerPage, Status status) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Verification> cq = cb.createQuery(Verification.class);
        Root<Verification> verifications = cq.from(Verification.class);

        cq.where(cb.and(cb.equal(verifications.get("calibratorEmployee"), calibratorEmployee),
                cb.equal(verifications.get("status"), status)));

        TypedQuery<Verification> typedQuery = em.createQuery(cq);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);

        return typedQuery.getResultList();
    }*/
}
