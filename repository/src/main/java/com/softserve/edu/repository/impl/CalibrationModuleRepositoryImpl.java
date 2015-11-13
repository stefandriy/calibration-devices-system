package com.softserve.edu.repository.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.repository.CalibrationModuleRepository;
import com.softserve.edu.repository.CalibrationModuleRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 * Created by roman on 13.11.15.
 *
 */

public class CalibrationModuleRepositoryImpl implements CalibrationModuleRepositoryCustom {

    @Autowired
    CalibrationModuleRepository calibrationModuleRepository;

    public CalibrationModule saveWithGenerating(CalibrationModule calibrationModule) {
        calibrationModuleRepository.save(calibrationModule);
        calibrationModule.generateModuleNumber();
        return calibrationModuleRepository.save(calibrationModule);
    }

}
