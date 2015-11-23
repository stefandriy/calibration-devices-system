package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.device.UnsuitabilityReason;
import com.softserve.edu.repository.CounterTypeRepository;
import com.softserve.edu.repository.UnsuitabilityReasonRepository;
import com.softserve.edu.service.admin.UnsuitabilityReasonService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Sonka on 23.11.2015.
 */
@Service
public class UnsuitabilityReasonServiceImpl implements UnsuitabilityReasonService {
    @Autowired
    private UnsuitabilityReasonRepository unsuitabilityReasonRepository;
    @Autowired
    private CounterTypeRepository counterTypeRepository;
    @Override
    public void addUnsuitabilityReason(String name, Long counterId) {
        unsuitabilityReasonRepository.save(new UnsuitabilityReason(name, counterTypeRepository.findOne(counterId)));
    }

    @Override
    public void removeUnsuitabilityReason(Long id) {

    }

    @Override
    public ListToPageTransformer<UnsuitabilityReason> getUnsuitabilityReasonBySearchAndPagination(int pageNumber, int itemsPerPage, Long id, String counterTypeName, String name, String sortCriteria, String sortOrder) {
        return null;
    }
}
