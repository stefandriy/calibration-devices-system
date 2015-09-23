package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.user.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public interface CalibratorEmployeeService {

     void addEmployee(User calibratorEmployee);

     User oneCalibratorEmployee(String username);
}
