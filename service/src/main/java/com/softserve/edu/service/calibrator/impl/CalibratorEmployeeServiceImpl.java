package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for adding employees for calibrator.
 */

@Service
public class CalibratorEmployeeServiceImpl implements CalibratorEmployeeService{

    @Autowired
    private UserRepository calibratorEmployeeRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    /**
     * Adds Employee for calibrator. Saves encoded password and
     * gives role CALIBRATOR_EMPLOYEE for user
     *
     * @param calibratorEmployee data for creation employee
     *      */

    @Transactional
    public void addEmployee(User calibratorEmployee) {

        String passwordEncoded = new BCryptPasswordEncoder().encode(calibratorEmployee.getPassword());
        calibratorEmployee.setPassword(passwordEncoded);
//        calibratorEmployee.setRole(CALIBRATOR_EMPLOYEE);
        calibratorEmployeeRepository.save(calibratorEmployee);
    }


    @Transactional
    public User oneCalibratorEmployee(String username) {
        return calibratorEmployeeRepository.getUserByUserName(username);
    }
}
