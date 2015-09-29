package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.utils.EmployeeDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface CalibratorService {

    List<Organization> findByDistrict(String district, String type);

    Organization findById(Long id);

    void uploadBbi(InputStream file, String idVerification, String originalFileFullName) throws IOException ;

    String findBbiFileByOrganizationId(String id);

    void deleteBbiFile(String idVerification);

    User oneCalibratorEmployee(String username);

    List<EmployeeDTO> getAllCalibratorEmployee(List<String> role, User employee);

    void assignCalibratorEmployee(String verificationId, User calibratorEmployee);

}
