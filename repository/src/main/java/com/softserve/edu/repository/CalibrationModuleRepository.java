package com.softserve.edu.repository;

import com.softserve.edu.entity.device.CalibrationModule;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CalibrationModuleRepository extends PagingAndSortingRepository<CalibrationModule, Long>, JpaSpecificationExecutor {

    CalibrationModule findCalibrationModuleBySerialNumber(String serialNumber);

}
