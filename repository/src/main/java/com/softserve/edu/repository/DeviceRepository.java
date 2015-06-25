package com.softserve.edu.repository;

import java.util.List;

import com.softserve.edu.entity.Device;
import com.softserve.edu.entity.Organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {
	//public List<Device> findByDevice(Device device, Pageable pagebPageable);
	public Page<Device> findAll(Pageable pageable);
	Page<Device> findByNumberLikeIgnoreCase(String number, Pageable pageable);
}
