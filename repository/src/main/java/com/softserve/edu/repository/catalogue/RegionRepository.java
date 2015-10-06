package com.softserve.edu.repository.catalogue;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softserve.edu.entity.catalogue.Region;

@Repository
public interface RegionRepository extends CrudRepository<Region, Long> {
    Region findByDesignation(String designation);

    @Query("SELECT region FROM District district INNER JOIN district.region region WHERE district.id=:districtId")
    Region findByDistrictId(@Param("districtId") Long districtId);
}
