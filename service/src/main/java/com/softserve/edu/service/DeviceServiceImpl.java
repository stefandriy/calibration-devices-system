package com.softserve.edu.service;


import com.softserve.edu.entity.Device;
import com.softserve.edu.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DeviceServiceImpl implements DeviceService{
	
	@Autowired
	private DeviceRepository deviceRepository;

    @Override
	@Transactional
	public boolean existsWithDeviceId(Long id) {
		return deviceRepository.findOne(id) != null;
	}

    @Override
	@Transactional
	public Device getById(Long id) {
		return deviceRepository.findOne(id);
	}

    @Override
	@Transactional
	public List<Device> getAll() {
		return (List<Device>) deviceRepository.findAll(); 
	}

    @Override
	@Transactional
	  public Page<Device> getDevicesBySearchAndPagination(int pageNumber, int itemsPerPage, String search) {
	  PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
	  return search == null ? deviceRepository.findAll(pageRequest) : deviceRepository.findByNumberLikeIgnoreCase("%" + search + "%", pageRequest);
	 }

    @Override
	@Transactional
	public List<Device> getAllByType(String device) {
		return (List<Device>) deviceRepository.findByDeviceName(device);}
}
