package com.softserve.edu.service.admin;

import com.softserve.edu.entity.device.CalibrationModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

/**
 * Created by Pavlo on 02.11.2015.
 */
public interface CalibrationModuleService {
    CalibrationModule addCalibrationModule(CalibrationModule calibrationModule);

    CalibrationModule findModuleById(Long calibrationModuleId);

    void disableCalibrationModule(Long calibrationModuleId);

    Page<CalibrationModule> findAllModules(Pageable pageable);

    Page<CalibrationModule> getFilteredPageOfCalibrationModule(Map<String, String> searchKeys, Pageable pageable, boolean status);

}
