package com.softserve.edu.repository.catalogue;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softserve.edu.entity.catalogue.Locality;

@Repository
public interface LocalityRepository extends CrudRepository<Locality, Long> {

    List<Locality> findDistinctByDistrictId(Long id);

    /**
     * Find all mail indexes by locality designation and district id
     * @param designation
     * @param districtId
     * @return
     */
    @Query("SELECT locality.mailIndex FROM Locality locality INNER JOIN locality.district d " +
            "WHERE d.id=:districtId AND locality.designation=:designation")
    List<String> findMailIndexByDesignationAndDistrictId(@Param("designation") String designation, @Param("districtId") Long districtId);

}
