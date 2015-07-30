package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.BbiProtocol;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Roles;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UploadBbiRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.utils.EmployeeProvider;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalibratorService {

    @Autowired
    private OrganizationRepository calibratorRepository;

    @Autowired
    private UploadBbiRepository uploadBbiRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<Organization> findByDistrict(String district, String type) {
        System.err.println("searching calibrators");
        return calibratorRepository.getByTypeAndDistrict(district, type);
    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return calibratorRepository.findOne(id);
    }

    @Transactional
    public void uploadBbi(InputStream file, String idVerification, String originalFileFullName) throws IOException {
        String filename = originalFileFullName.substring(0, originalFileFullName.lastIndexOf('.'));
        byte[] bytesOfBbi = IOUtils.toByteArray(file);
        Verification verification = verificationRepository.findOne(idVerification);
        BbiProtocol bbiProtocol = new BbiProtocol(bytesOfBbi, verification, filename);
        verification.setBbiProtocol(bbiProtocol);
        uploadBbiRepository.save(bbiProtocol);
        verificationRepository.save(verification);
    }

    @Transactional(readOnly = true)
    public String findBbiFileByOrganizationId(String id) {
        return uploadBbiRepository.findFileNameByVerificationId(id);
    }

    @Transactional
    public void deleteBbiFile(String idVerification) {
        Verification verification = verificationRepository.findOne(idVerification);
        BbiProtocol bbiProtocol = uploadBbiRepository.findByVerification(idVerification);
        verification.setBbiProtocol(null);
        verificationRepository.save(verification);
        uploadBbiRepository.delete(bbiProtocol);
    }

    @Transactional
    public User oneProviderEmployee(String username) {
        return userRepository.getUserByUserName(username);
    }

    @Transactional
    public List<EmployeeProvider> getAllProviders(List<String> role, User employee) {
        List<EmployeeProvider> providerListEmployee = new ArrayList<>();
        if (role.contains(Roles.CALIBRATOR_ADMIN.name())) {
            List<User> list = userRepository.getAllProviderUsers(Roles.CALIBRATOR_EMPLOYEE.name(),
                    employee.getOrganization().getId());
            providerListEmployee = EmployeeProvider.giveListOfProviders(list);
        } else {
            EmployeeProvider userPage = new EmployeeProvider(employee.getUsername(), employee.getFirstName(),
                    employee.getLastName(), employee.getMiddleName(),role.get(0));
            providerListEmployee.add(userPage);
        }
        return providerListEmployee;
    }

    @Transactional
    public void assignProviderEmployee(String verificationId, User calibratorEmployee) {
        Verification verification = verificationRepository.findOne(verificationId);
        verification.setCalibratorEmployee(calibratorEmployee);
        verification.setReadStatus(ReadStatus.READ);
        verificationRepository.save(verification);
    }

}
