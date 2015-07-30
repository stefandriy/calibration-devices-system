package com.softserve.edu.service;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.softserve.edu.service.verification.VerificationService;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;

@Service
public class
        MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

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
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Централізована система повірки лічильників"));
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
                String body = mergeTemplateIntoString(velocityEngine, "/velocity/templates" + "/mailTemplate.vm", "UTF-8", templateVariables);
                message.setText(body, true);
                message.setSubject("Important notification");
            }
        };
       this.mailSender.send(preparator);
    }
    
    public void sendRejectMail(String to, String userName, String verificationId, String msg) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Централізована система повірки лічильників"));
                String domain = null;	
				try {
					domain = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException ue) {
					logger.error("Cannot get host address", ue);
				}
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", userName);
                templateVariables.put("protocol", protocol);
                templateVariables.put("domain", domain);
                templateVariables.put("verificationId", verificationId);
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
                message.setFrom(new InternetAddress("metrology.calibrations@gmail.com", "Централізована система повірки лічильників"));
                String domain = null;	
				try {
					domain = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException ue) {
					logger.error("Cannot get host address", ue);
				}
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("protocol", protocol);
                templateVariables.put("domain", domain);
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
                message.setTo("metrology.calibration.devices@gmail.com");
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
}