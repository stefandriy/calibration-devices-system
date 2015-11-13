package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.CalibrationModuleRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.admin.CalibrationModuleService;
import com.softserve.edu.service.utils.filter.internal.Comparison;
import com.softserve.edu.service.utils.filter.internal.Condition;
import com.softserve.edu.specification.CalibrationModuleSpecification;
import com.softserve.edu.specification.CalibrationModuleSpecifications;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.softserve.edu.service.utils.filter.Filter;
import org.springframework.stereotype.Service;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by Pavlo on 02.11.2015.
 */
@Service
public class CalibrationModuleServiceImpl implements CalibrationModuleService {
    @Autowired
    CalibrationModuleRepository calibrationModuleRepository;
    @Autowired
    UserRepository userRepository;
    private Logger logger = Logger.getLogger(CalibrationModule.class);
    //Map<String, Specification<CalibrationModule>> specificationMap;

    /*private Specification<CalibrationModule> getSpecification(Map<String, String> searchKeys, boolean status) {
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
    }*/

    /*private void buildMap(Map<String, String> searchKeys) {

    }*/

    public CalibrationModule addCalibrationModule(CalibrationModule calibrationModule) {
        if (calibrationModule == null) {
            throw new NullPointerException("Adding null pointer to calibration modules");
        } else return calibrationModuleRepository.saveWithGenerating(calibrationModule);
    }


    public CalibrationModule findModuleById(Long calibrationModuleId) {
        return calibrationModuleRepository.findOne(calibrationModuleId);
    }

    ;

    public void disableCalibrationModule(Long calibrationModuleId) {
        CalibrationModule calibrationModule = calibrationModuleRepository.findOne(calibrationModuleId);
        calibrationModule.setIsActive(false);
        calibrationModuleRepository.save(calibrationModule);
    }

    ;

    public Page<CalibrationModule> getFilteredPageOfCalibrationModule(Map<String, String> searchKeys, Pageable pageable, boolean status) {
        CalibrationModuleSpecification calibrationModuleSpecification = new CalibrationModuleSpecification();
        Filter filter = new Filter();
        for (Map.Entry<String, String> entry : searchKeys.entrySet()) {
            if (entry.getKey().equals("isActive")) {
                filter.addCondition(new Condition.Builder()
                        .setComparison(Comparison.eq)
                        .setField(entry.getKey())
                        .setValue(Boolean.parseBoolean(entry.getValue()))
                        .build());
            } else {
                filter.addCondition(new Condition.Builder()
                        .setComparison(Comparison.like)
                        .setField(entry.getKey())
                        .setValue(entry.getValue())
                        .build());
            }
        }
        return calibrationModuleRepository.findAll(filter, pageable);
    }

    public Page<CalibrationModule> findAllModules(Pageable pageable) {
        return calibrationModuleRepository.findAll(pageable);
    }

    public void updateCalibrationModule(Long moduleId, CalibrationModule calibrationModule) {
        CalibrationModule changedCalibrationModule = calibrationModuleRepository.findOne(moduleId);
        changedCalibrationModule.updateFields(calibrationModule);
        calibrationModuleRepository.save(changedCalibrationModule);
    }


    public List<String> findAllCalibrationModulsNumbers(String moduleType, Date workDate, String applicationFiled,
                                                         String userName) {
        Filter filter = new Filter();
        List<Condition> conditions = new ArrayList<>();
        User user = userRepository.findOne(userName);
        List<String> serialNumbersList = new ArrayList<>();
        if (user == null) {
            logger.error("Cannot found user!");
            throw new NullPointerException();
        }
        conditions.add(new Condition.Builder()
                .setComparison(Comparison.like).setField("moduleType").setValue(moduleType).build());
        conditions.add(new Condition.Builder()
                .setComparison(Comparison.eq).setField("workDate").setValue(workDate).build());
        conditions.add(new Condition.Builder()
                .setComparison(Comparison.eq).setField("deviceType").setValue(applicationFiled).build());
        conditions.add(new Condition.Builder()
                .setComparison(Comparison.eq).setField("organizationCode").setValue(user.getOrganization().getId())
                .build());
        filter.addConditionList(conditions);
//        List<CalibrationModule> modules = modules = (List<CalibrationModule>) calibrationModuleRepository
//                .findAll(specifications.where(CalibrationModuleSpecifications.moduleHasType(moduleType))
//                        .and(CalibrationModuleSpecifications.moduleHasWorkDate(workDate)).and(CalibrationModuleSpecifications
//                                .moduleHasCalibratorId(user.getOrganization().getId()))
//                        .and(CalibrationModuleSpecifications.moduleDeviceType(applicationFiled)));
        List<CalibrationModule> modules = calibrationModuleRepository.findAll(filter);
        if (modules == null) {
            logger.error("Cannot found modules for the choosen workDate " + workDate);
            throw new NullPointerException();
        } else {
            for (CalibrationModule module : modules) {
                serialNumbersList.add(module.getSerialNumber());
            }
        }
        return serialNumbersList;

    }

}
