package com.softserve.edu.repository;

import com.softserve.edu.entity.Device;
import com.softserve.edu.entity.catalogue.AbstractCatalogue;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {

	public Page<Device> findAll(Pageable pageable);
	Page<Device> findByNumberLikeIgnoreCase(String number, Pageable pageable);
	

}
