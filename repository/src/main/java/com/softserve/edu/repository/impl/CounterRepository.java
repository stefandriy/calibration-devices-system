package com.softserve.edu.repository.impl;


import com.softserve.edu.entity.device.Counter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterRepository extends CrudRepository<Counter, Long> {
    List<Counter> findAll();

    Counter findByNumberCounter(String numberCounter);


}
