package com.softserve.edu.repository;

import com.softserve.edu.entity.device.CounterType;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterTypeRepository extends CrudRepository<CounterType, Long> {

    List<CounterType> findByDeviceId(Long deviceId);

}
