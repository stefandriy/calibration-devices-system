package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.repository.CalibrationModuleRepository;
import com.softserve.edu.service.admin.CalibrationModuleService;
import com.softserve.edu.service.utils.filter.internal.Comparison;
import com.softserve.edu.service.utils.filter.internal.Condition;
import com.softserve.edu.specification.CalibrationModuleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.softserve.edu.service.utils.filter.Filter;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by Pavlo on 02.11.2015.
 */
public class CalibrationModuleServiceImpl implements CalibrationModuleService {
    @Autowired
    CalibrationModuleRepository calibrationModuleRepository;
    Map<String, Specification<CalibrationModule>> specificationMap;

    private Specification<CalibrationModule> getSpecification(Map<String, String> searchKeys, boolean status) {
        Specification<CalibrationModule> specification = CalibrationModuleSpecification.moduleIsActiveOrNot(status);
        if (searchKeys.containsKey("deviceType")) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleDeviceType(searchKeys.get("deviceType")));
        }
        if (searchKeys.containsKey("organizationCode")) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleHasOrganization(searchKeys.get("organizationCode")));
        }
        if (searchKeys.containsKey("condDesignation")) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleHasCondDesignation(searchKeys.get("condDesignation")));
        }
        if (searchKeys.containsKey("serialNumber")) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleHasSerialNumber(searchKeys.get(("serialNumber"))));
        }
        if (searchKeys.containsKey("employeeFullName")) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleHasEmployeeFullName(searchKeys.get("employeeFullName")));
        }
        if (searchKeys.containsKey("telephone")) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleHasTelephone(searchKeys.get("telephone")));
        }
        if (searchKeys.containsKey("moduleType")) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleHasType(searchKeys.get("moduleType")));
        }
        if (searchKeys.containsKey("email")) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleHasEmail(searchKeys.get("email")));
        }
        if (searchKeys.containsKey("calibrationType")) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleHasCalibrationType(searchKeys.get("calibrationType")));
        }
        if (searchKeys.containsKey(("calibratorId"))) {
            specification = where(specification).and(CalibrationModuleSpecification.moduleHasCalibratorId(Long.parseLong(searchKeys.get("calibratotId"))));
        }
        return specification;
    }

    public CalibrationModule addCalibrationModule(CalibrationModule calibrationModule) {
        if (calibrationModule == null) {
            throw new NullPointerException("Adding null pointer to calibration modules");
        } else return calibrationModuleRepository.save(calibrationModule);
    }


    public CalibrationModule findModuleById(Long calibrationModuleId) {
        return calibrationModuleRepository.findOne(calibrationModuleId);
    }



    public void disableCalibrationModule(Long calibrationModuleId) {
        CalibrationModule calibrationModule = calibrationModuleRepository.findOne(calibrationModuleId);
        calibrationModule.setIsActive(false);
        calibrationModuleRepository.save(calibrationModule);
    }


    public Page<CalibrationModule> getFilteredPageOfCalibrationModule(Map<String, String> searchKeys, Pageable pageable
            , boolean status) {
        CalibrationModuleSpecification calibrationModuleSpecification = new CalibrationModuleSpecification();
        Filter filter = new Filter();
        for (Map.Entry<String, String> entry : searchKeys.entrySet()) {
            filter.addCondition(new Condition.Builder()
                    .setComparison(Comparison.like)
                    .setField(entry.getKey())
                    .setValue(entry.getValue())
                    .build());
        }
        return calibrationModuleRepository.findAll(filter, pageable);
    }

    public Page<CalibrationModule> findAllModules(Pageable pageable) {
        return calibrationModuleRepository.findAll(pageable);
    }

    public void updateCalibrationModule(CalibrationModule calibrationModule) {

    }

    public List<String> findAllCalibrationModulsNumbers (String moduleType,
                                                         Date workDate, String applicationFiled,
                                                         String userName) {
        return new ArrayList<String>();
    }

}
