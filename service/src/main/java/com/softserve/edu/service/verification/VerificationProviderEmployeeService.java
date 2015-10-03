package com.softserve.edu.service.verification;

import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;

import java.util.List;

public interface VerificationProviderEmployeeService {
     void assignProviderEmployee(String verificationId, User providerEmployee);

     User getProviderEmployeeById(String idVerification);

     List<Verification> getVerificationListbyProviderEmployee(String username);

     List<Verification> getVerificationListbyCalibratormployee(String username);

     Long countByProviderEmployeeTasks(String username);

    Long countByCalibratorEmployeeTasks(String username);

     User oneProviderEmployee(String username);
}
