package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.BbiProtocol;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Roles;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UploadBbiRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.utils.EmployeeDTO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalibratorServiceImpl implements CalibratorService {

    @Autowired
    private OrganizationRepository calibratorRepository;

    @Autowired
    private UploadBbiRepository uploadBbiRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findByDistrict(String district, String type) {
        return calibratorRepository.findByDistrictAndType(district, type);
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return calibratorRepository.findOne(id);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public String findBbiFileByOrganizationId(String id) {
        return uploadBbiRepository.findFileNameByVerificationId(id);
    }

    @Override
    @Transactional
    public void deleteBbiFile(String idVerification) {
        Verification verification = verificationRepository.findOne(idVerification);
        BbiProtocol bbiProtocol = uploadBbiRepository.findByVerification(verification);
        verification.setBbiProtocol(null);
        verificationRepository.save(verification);
        uploadBbiRepository.delete(bbiProtocol);
    }

    @Override
    @Transactional
    public User oneCalibratorEmployee(String username) {
        return userRepository.getUserByUserName(username);
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAllCalibratorEmployee(List<String> role, User employee) {
        List<EmployeeDTO> providerListEmployee = new ArrayList<>();
        if (role.contains(Roles.CALIBRATOR_ADMIN.name())) {
            List<User> list = userRepository.getAllProviderUsersList(Roles.CALIBRATOR_EMPLOYEE.name(),
                    employee.getOrganization().getId(), true);
            providerListEmployee = EmployeeDTO.giveListOfProviders(list);
        } else {
            EmployeeDTO userPage = new EmployeeDTO(employee.getUsername(), employee.getFirstName(),
                    employee.getLastName(), employee.getMiddleName(), role.get(0));
            providerListEmployee.add(userPage);
        }
        return providerListEmployee;
    }

    @Override
    @Transactional
    public void assignCalibratorEmployee(String verificationId, User calibratorEmployee) {
        Verification verification = verificationRepository.findOne(verificationId);
        verification.setCalibratorEmployee(calibratorEmployee);
        verification.setReadStatus(ReadStatus.READ);
        verificationRepository.save(verification);
    }
}