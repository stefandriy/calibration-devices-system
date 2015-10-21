package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGraphic;
import com.softserve.edu.service.utils.EmployeeDTO;

import java.util.Date;
import java.util.List;

public interface CalibratorEmployeeService {

     void addEmployee(User calibratorEmployee);

     User oneCalibratorEmployee(String username);

     List<EmployeeDTO> getAllCalibrators(List<String> role, User employee);
     
     Date convertToDate(String date) throws IllegalArgumentException;
     
     List<ProviderEmployeeGraphic> buidGraphicMainPanel(Date from, Date to, Long idOrganization);
}
