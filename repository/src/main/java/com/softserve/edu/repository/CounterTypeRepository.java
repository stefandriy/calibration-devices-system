package com.softserve.edu.repository;

import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.organization.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CounterTypeRepository extends CrudRepository<CounterType, Long> {

    List<CounterType> findByDeviceId(Long deviceId);

    List<CounterType> findBySymbolAndDeviceId(String symbol, Long deviceId);

    List<CounterType> findAll();

    List<CounterType> findBySymbol(String symbol);

    CounterType findOneBySymbolAndStandardSize(String symbol, String standardSize);

    CounterType findOneBySymbolAndStandardSizeAndDeviceId(String symbol, String standardSize, Long deviceId);

}
