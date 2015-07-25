package com.softserve.edu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.MeasuringEquipment;
import com.softserve.edu.repository.MeasuringEquipmentRepository;

@Service
public class MeasuringEquipmentService {

    @Autowired
    private MeasuringEquipmentRepository measuringEquipmentRepository;

    @Transactional
    public List<MeasuringEquipment> getAll() {
        return (List<MeasuringEquipment>) measuringEquipmentRepository.findAll();
    }

    @Transactional
    public Page<MeasuringEquipment> getMeasuringEquipmentsBySearchAndPagination(int pageNumber,
                                                                                int itemsPerPage, String search) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return search == null ? measuringEquipmentRepository.findAll(pageRequest)
                : measuringEquipmentRepository.findByNameLikeIgnoreCase("%" + search + "%",
                pageRequest);
    }

    @Transactional
    public void addMeasuringEquipment(MeasuringEquipment measuringEquipment) {
        measuringEquipmentRepository.save(measuringEquipment);
    }

    @Transactional
    public MeasuringEquipment getMeasuringEquipmentById(Long equipmentId) {
        return measuringEquipmentRepository.findOne(equipmentId);
    }

    @Transactional
    public void editMeasuringEquipment(Long equipmentId, String name, String deviceType, String manufacturer, String verificationInterval) {
        MeasuringEquipment measuringEquipment = measuringEquipmentRepository.findOne(equipmentId);
        measuringEquipment.setName(name);
        measuringEquipment.setDeviceType(deviceType);
        measuringEquipment.setManufacturer(manufacturer);
        measuringEquipment.setVerificationInterval(verificationInterval);
    }

    @Transactional
    public MeasuringEquipment deleteMeasuringEquipment(Long equipmentId) {
        MeasuringEquipment deletedEquipment = measuringEquipmentRepository.findOne(equipmentId);
        measuringEquipmentRepository.delete(equipmentId);
        return deletedEquipment;
    }

}
