package com.softserve.edu.repository.catalogue;

import java.util.List;

import com.softserve.edu.entity.catalogue.StreetType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetTypeRepository extends CrudRepository<StreetType, Long> {
    List<StreetType> findAll();
}
