package com.softserve.edu.service.admin;

import com.softserve.edu.entity.device.UnsuitabilityReason;
import com.softserve.edu.service.utils.ListToPageTransformer;

import java.util.List;

/**
 * Created by Sonka on 23.11.2015.
 */
public interface UnsuitabilityReasonService {
    void addUnsuitabilityReason(String name, Long counterId);
    void removeUnsuitabilityReason(Long id);

    ListToPageTransformer<UnsuitabilityReason> getUnsuitabilityReasonBySearchAndPagination(int pageNumber, int itemsPerPage, Long id,
                                                                          String counterTypeName, String name,
                                                                          String sortCriteria, String sortOrder);

}
