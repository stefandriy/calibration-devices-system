package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.AdditionalInfo;
import com.softserve.edu.service.utils.EmployeeDTO;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public interface CalibratorService {

    Organization findById(Long id);

    CounterType findOneBySymbolAndStandardSize(String symbol, String standardSize);

    void uploadBbi(InputStream fileStream, String verificationId,
                   String originalFileFullName) throws IOException;

    void uploadBbi(InputStream fileStream, Verification verification,
                   String originalFileFullName) throws IOException;

    String findBbiFileByOrganizationId(String id);

    User oneCalibratorEmployee(String username);

    List<EmployeeDTO> getAllCalibratorEmployee(List<String> role, User employee);

    void assignCalibratorEmployee(String verificationId, User calibratorEmployee);

//    void saveInfo (int entrance, int doorCode, int floor, Date dateOfVerif,
//                   String time, boolean serviceability, Date noWaterToDate,String notes, String verificationId);

    boolean checkIfAdditionalInfoExists(String verificationId);

//    AdditionalInfo findAdditionalInfoByVerifId(String verificationId);

    Set<String> getTypesById(Long id);

}
