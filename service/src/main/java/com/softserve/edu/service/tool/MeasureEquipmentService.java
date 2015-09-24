package com.softserve.edu.service.tool;

import com.softserve.edu.entity.MeasuringEquipment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MeasureEquipmentService {

    List<MeasuringEquipment> getAll();

    Page<MeasuringEquipment> getMeasuringEquipmentsBySearchAndPagination(int pageNumber,int itemsPerPage, String search);

    void addMeasuringEquipment(MeasuringEquipment measuringEquipment);

    MeasuringEquipment getMeasuringEquipmentById(Long equipmentId);

    void editMeasuringEquipment(Long equipmentId, String name, String deviceType, String manufacturer, String verificationInterval);

    void deleteMeasuringEquipment(Long equipmentId);
}
