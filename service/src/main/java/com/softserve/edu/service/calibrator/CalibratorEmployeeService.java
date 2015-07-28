package com.softserve.edu.service.calibrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;


/**
 * Service for adding employees for calibrator.
 */

@Service
public class CalibratorEmployeeService {

    @Autowired
    private UserRepository calibratorEmployeeRepository;

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

    
//    @Transactional()
//    public Employee findByUserame(String userName){
//    	return calibratorEmployeeRepository.findByUsername(userName);
//}
}
