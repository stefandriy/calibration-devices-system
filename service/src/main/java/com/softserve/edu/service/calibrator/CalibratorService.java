package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.utils.EmployeeDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public interface CalibratorService {
    
    Organization findById(Long id);

    void uploadBbi(InputStream fileStream, String idVerification,
                   Long installmentNumber, String originalFileFullName) throws IOException ;

    String findBbiFileByOrganizationId(String id);

    void deleteBbiFile(String idVerification);

    User oneCalibratorEmployee(String username);

    List<EmployeeDTO> getAllCalibratorEmployee(List<String> role, User employee);

    void assignCalibratorEmployee(String verificationId, User calibratorEmployee);

    void saveInfo (int entrance, int doorCode, int floor, Date dateOfVerif,
                   String time, boolean serviceability, Date noWaterToDate,String notes, String verificationId);

}
