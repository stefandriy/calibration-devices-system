package com.softserve.edu.repository.catalogue;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softserve.edu.entity.catalogue.District;

@Repository
public interface DistrictRepository extends CrudRepository<District, Long> {
    List<District> findByRegionId(Long id);

    District findByDesignationAndRegionId(String designation, Long region);

    Long findIdByDesignation(String designation);
}
