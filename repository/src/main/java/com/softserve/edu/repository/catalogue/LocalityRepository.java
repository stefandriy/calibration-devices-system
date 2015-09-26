package com.softserve.edu.repository.catalogue;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softserve.edu.entity.catalogue.Locality;

@Repository
public interface LocalityRepository extends CrudRepository<Locality, Long> {
    List<Locality> findDistinctByDistrictId(Long id);

    List<String> findMailIndexByDesignationAndDistrictId(String designation, Long districtId);

}
