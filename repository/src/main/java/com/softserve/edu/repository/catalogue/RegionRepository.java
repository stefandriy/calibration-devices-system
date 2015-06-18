package com.softserve.edu.repository.catalogue;

import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.catalogue.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends CrudRepository<Region, Long> {
    Region findByDesignation(String designation);
}
