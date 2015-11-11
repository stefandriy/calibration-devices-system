package com.softserve.edu.repository;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Vasyl on 08.10.2015.
 */
@Repository
public interface CalibrationModuleRepository
        extends PagingAndSortingRepository<CalibrationModule, Long>, JpaSpecificationExecutor {

    CalibrationModule findBySerialNumber(String serialNumber);

    Page<CalibrationModule> findByOrganizationCode(String organizationCode, Pageable pageable);

    Page<CalibrationModule> findByCondDesignationIgnoreCase(String condDesignation, Pageable pageable);

    Page<CalibrationModule> findByEmployeeFullNameIgnoreCase(String employeeFullName, Pageable pageable);

    Page<CalibrationModule> findByTelephone(String telephone, Pageable pageable);

    Page<CalibrationModule> findByModuleTypeIgnoreCase(String moduleType, Pageable pageable);

    Page<CalibrationModule> findByEmail(String email, Pageable pageable);

    Page<CalibrationModule> findByCalibrationTypeIgnoreCase(String calibrationType, Pageable pageable);

    CalibrationModule findByModuleNumber(String moduleNumber);

    Page<CalibrationModule> findByIsActive(boolean isActive, Pageable pageable);

    Page<CalibrationModule> findByOrganization(Organization organization, Pageable pageable);

    Page<CalibrationModule> findByWorkDate(Date workDate, Pageable pageable);

    Page<CalibrationModule> findByDeviceType(Device.DeviceType deviceType, Pageable pageable);

}
