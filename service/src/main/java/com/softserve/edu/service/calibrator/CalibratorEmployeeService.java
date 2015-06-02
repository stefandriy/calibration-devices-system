package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.user.CalibratorEmployee;
import com.softserve.edu.repository.CalibratorEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.softserve.edu.entity.user.CalibratorEmployee.CalibratorEmployeeRole.CALIBRATOR_EMPLOYEE;

@Service
public class CalibratorEmployeeService {

    @Autowired
    private CalibratorEmployeeRepository calibratorEmployeeRepository;

    @Transactional
    public void addEmployee(CalibratorEmployee calibratorEmployee) {

        String passwordEncoded = new BCryptPasswordEncoder().encode(calibratorEmployee.getPassword());
        calibratorEmployee.setPassword(passwordEncoded);
        calibratorEmployee.setRole(CALIBRATOR_EMPLOYEE);
        calibratorEmployeeRepository.save(calibratorEmployee);
    }
}
