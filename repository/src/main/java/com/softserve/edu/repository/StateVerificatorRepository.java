package com.softserve.edu.repository;

import com.softserve.edu.entity.StateVerificator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StateVerificatorRepository extends CrudRepository<StateVerificator, Long> {
        public List<StateVerificator> findByAddressDistrict(String designation);
}
