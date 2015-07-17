package com.softserve.edu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.MeasuringEquipment;
import com.softserve.edu.repository.MeasuringEquipmentRepository;


public class MeasuringEquipmentService {

	@Autowired
	MeasuringEquipmentRepository measuringEquipmentRepository;
	
	@Transactional
	public List<MeasuringEquipment> getAll() {
		return (List<MeasuringEquipment>) measuringEquipmentRepository.findAll(); 
	}
	@Transactional
	  public Page<MeasuringEquipment> getDevicesBySearchAndPagination(int pageNumber,
	   int itemsPerPage, String search) {
	  PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
	  return search == null ? measuringEquipmentRepository.findAll(pageRequest)
	    : measuringEquipmentRepository.findByNumberLikeIgnoreCase("%" + search + "%",
	  	      pageRequest);
	 }
	
}
