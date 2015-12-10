package com.softserve.edu.service.admin;

import com.softserve.edu.entity.device.UnsuitabilityReason;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.springframework.data.domain.Page;

import java.util.List;


public interface UnsuitabilityReasonService {
    void addUnsuitabilityReason(String name, Long counterId);

    void removeUnsuitabilityReason(Long id);

    List<UnsuitabilityReason> findUnsuitabilityReasonsPagination(int pageNumber, int itemsPerPage);

    List<UnsuitabilityReason> findAll();
}
