package com.softserve.edu.service;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;

@Service
public class MailService {


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Value("${mail.credentials.username}")
    private String userName;

    @Value("${site.protocol}")
    private String protocol;

//    @Value("${site.domain}")
//    private String domain;

    Logger logger = Logger.getLogger(MailService.class);

    public void sendMail(String to, String userName, String clientCode, String providerName, String deviceType) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                String domain = null;
                try {
                    domain = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ue) {
                    logger.error("Cannot get host address", ue);
                }

                SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
                String date = form.format(new Date());
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", userName);
                templateVariables.put("protocol", protocol);
                templateVariables.put("domain", domain);
                templateVariables.put("applicationId", clientCode);
                templateVariables.put("providerName", providerName);
                templateVariables.put("deviceType", deviceType);
                templateVariables.put("date", date);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates/mailTemplate.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        this.mailSender.send(preparator);
    }

    public void sendNewPasswordMail(String employeeEmail, String employeeName, String newPassword) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(employeeEmail);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", employeeName);
                templateVariables.put("password", newPassword);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/mailNewPasswordEmployee.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        this.mailSender.send(preparator);
    }

    public void sendRejectMail(String to, String userName, String verificationId, String msg, String deviceType) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", userName);
                templateVariables.put("verificationId", verificationId);
                templateVariables.put("deviceType", deviceType);
                templateVariables.put("message", msg);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/rejectVerification.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");

            }
        };
        this.mailSender.send(preparator);
    }

    public void sendAcceptMail(String to, String verificationId, String deviceType) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                String domain = null;
                try {
                    domain = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ue) {
                    logger.error("Cannot get host address", ue);
                }
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("verificationId", verificationId);
                templateVariables.put("deviceType", deviceType);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/accepted.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        this.mailSender.send(preparator);
    }


        public void sendClientMail(String from, String userFirstName, String userLastName, String verificationId, String msg) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

                List<User> adminList = userRepository.findByRoleLikeIgnoreCase("SYS_ADMIN");
                if (!adminList.isEmpty() && adminList.get(0).getEmail() != null) {
                    message.setTo(adminList.get(0).getEmail());
                    logger.trace("Email send to:" + adminList.get(0).getEmail());
                } else {
                    message.setTo("metrology.calibration.devices@gmail.com");
                }
                message.setFrom(new InternetAddress(from));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("firstName", userFirstName);
                templateVariables.put("lastName", userLastName);
                templateVariables.put("mailAddress", from);
                templateVariables.put("message", msg);
                templateVariables.put("applicationId", verificationId);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/clientMail.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");

            }
        };
        this.mailSender.send(preparator);
    }

    public void sendMailFromProvider(String to, String userName, String clientCode, String providerName, String deviceType) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                String domain = null;
                try {
                    domain = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ue) {
                    logger.error("Cannot get host address", ue);
                }

                SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
                String date = form.format(new Date());
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", userName);
                templateVariables.put("protocol", protocol);
                templateVariables.put("domain", domain);
                templateVariables.put("applicationId", clientCode);
                templateVariables.put("providerName", providerName);
                templateVariables.put("deviceType", deviceType);
                templateVariables.put("date", date);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates/mailTemplate.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        this.mailSender.send(preparator);
    }

    public void sendTimeExceededMail(String verificationId, int processTimeExceeding, int maxProcessTime, String mailTo) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(mailTo);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("processTimeExceeding", processTimeExceeding);
                templateVariables.put("verificationId", verificationId);
                templateVariables.put("maxProcessTime", maxProcessTime);
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/processTimeExceeded.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        this.mailSender.send(preparator);
    }

    //TODO: remove calls to database out of this service
    public void sendOrganizationChanges (Long organizationId, String username){
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            Organization organization = organizationRepository
                    .findOne(organizationId);
            User admin = userRepository.findByUsername(username);

            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(organization.getEmail());
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Calibration devices system"));
                String domain = null;
                try {
                    domain = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ue) {
                    logger.error("Cannot get host address", ue);
                }
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", organization.getName());
                templateVariables.put("email", organization.getEmail());
                templateVariables.put("phone", organization.getPhone());
                templateVariables.put("types", organization.getOrganizationTypes());
                templateVariables.put("employeesCapacity", organization.getEmployeesCapacity());
                templateVariables.put("maxProcessTime", organization.getMaxProcessTime());
                templateVariables.put("region", organization.getAddress().getRegion());
                templateVariables.put("locality", organization.getAddress().getLocality());
                templateVariables.put("district", organization.getAddress().getDistrict());
                templateVariables.put("street", organization.getAddress().getStreet());
                templateVariables.put("building", organization.getAddress().getBuilding());
                templateVariables.put("flat", organization.getAddress().getFlat());
                templateVariables.put("username", username);
                templateVariables.put("firstName", admin.getFirstName());
                templateVariables.put("middleName", admin.getMiddleName());
                templateVariables.put("lastName", admin.getLastName());

                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/organizationChanges.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
        this.mailSender.send(preparator);
    }
}
