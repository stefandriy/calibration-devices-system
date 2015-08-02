package com.softserve.edu.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.Device;
import com.softserve.edu.entity.util.DeviceType;
import com.softserve.edu.repository.DeviceRepository;


@Service
public class DeviceService {
	
	@Autowired
	private DeviceRepository deviceRepository;

	@Transactional
	public boolean existsWithDeviceid(Long id) {
		return deviceRepository.findOne(id) != null;
	}
	@Transactional
	public Device getById(Long id) {
		return deviceRepository.findOne(id);
	}


	@Transactional
	public List<Device> getAll() {
		return (List<Device>) deviceRepository.findAll(); 
	}
	@Transactional
	  public Page<Device> getDevicesBySearchAndPagination(int pageNumber,
	   int itemsPerPage, String search) {
	  PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
	  return search == null ? deviceRepository.findAll(pageRequest)
	    : deviceRepository.findByNumberLikeIgnoreCase("%" + search + "%",
	  	      pageRequest);
	 }

	@Transactional
	public List<Device> getAllByType(String device) {
		return (List<Device>) deviceRepository.findByDeviceName(device);}

}
