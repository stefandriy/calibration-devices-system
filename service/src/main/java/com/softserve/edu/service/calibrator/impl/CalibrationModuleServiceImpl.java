package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.CalibrationModuleRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.calibrator.CalibrationModuleService;
import com.softserve.edu.service.calibrator.specifications.CalibrationModuleSpecifications;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class CalibrationModuleServiceImpl implements CalibrationModuleService {

    @Autowired
    private CalibrationModuleRepository moduleRepository;

    private Specifications<CalibrationModuleSpecifications> specifications;

    @Autowired
    private UserRepository userRepository;

    private Logger logger = Logger.getLogger(CalibrationModule.class);

    @Override
    @SuppressWarnings("all")
    public List<String> findAllCalibrationModulsNumbers(String moduleType, Date workDate, String applicationFiled,String userName) {

        User user = userRepository.findOne(userName);
        if (user == null) {
            logger.error("Cannot found user!");
        }
        // TODO potential NPE here
        List<CalibrationModule> modules = new ArrayList<>();
        try {
            modules = (List<CalibrationModule>) moduleRepository.findAll(specifications.where(CalibrationModuleSpecifications.moduleHasType(moduleType))
                    .and(CalibrationModuleSpecifications.moduleHasWorkDate(workDate)).and(CalibrationModuleSpecifications.moduleHasCalibratorId(user.getOrganization().getId()))
                    .and(CalibrationModuleSpecifications.moduleDeviceType(applicationFiled)).and(CalibrationModuleSpecifications.moduleIsAvaliable()));
        } catch (NullPointerException e){
            logger.error("Cannot found modules!", e);
        }

        List<String> serialNumbersList = new ArrayList<>();
        for (CalibrationModule module : modules) {
            serialNumbersList.add(module.getSerialNumber());
        }
        return serialNumbersList;
    }
}
