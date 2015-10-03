package com.softserve.edu.service.tool;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;

public interface MailService {
    void sendMail(String to, String userName, String clientCode, String providerName, String deviceType);

    void sendNewPasswordMail(String employeeEmail, String employeeName, String newPassword);

    void sendRejectMail(String to, String userName, String verificationId, String msg, String deviceType);

    void sendAcceptMail(String to, String verificationId, String deviceType);

    void sendClientMail(String to, String from, String userFirstName, String userLastName, String verificationId, String msg);

    void sendMailFromProvider(String to, String userName, String clientCode, String providerName, String deviceType);

    void sendTimeExceededMail(String verificationId, int processTimeExceeding, int maxProcessTime, String mailTo);

    void sendOrganizationChanges(Organization organization, User admin);
}