package com.softserve.edu.service.catalogue;

import com.softserve.edu.entity.catalogue.Region;

public interface RegionService {

    Iterable<Region> getAll();

    Region getRegionByDesignation(String designation);

    Region findByDistrictId(Long districtId);
}
