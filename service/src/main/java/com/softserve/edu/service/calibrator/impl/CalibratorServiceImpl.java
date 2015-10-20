package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.verification.BbiProtocol;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.ReadStatus;
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

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class CalibratorServiceImpl implements CalibratorService {

    private static final String dbFileExtensionPattern = "^.*\\.(db|DB|)$";
    private static final String contentExtensionPattern = "^.*\\.(bbi|BBI|)$";

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
    public Organization findById(Long id) {
        return calibratorRepository.findOne(id);
    }

    @Override
    @Transactional
    public void uploadArchive(InputStream inputStream, String originalFileFullName) throws IOException {

        String filename = originalFileFullName.substring(0, originalFileFullName.lastIndexOf('.'));
        ZipEntry entry;

        Map<String, String> bbiFilesToVerificationMap = getBBIfilesToVerificationMap(inputStream);

        try (ZipInputStream zipStream = new ZipInputStream(inputStream)) {
            while ((entry = zipStream.getNextEntry()) != null) {
                String compressedFilePath = entry.getName();
                String bbiFileName = compressedFilePath.substring(compressedFilePath.lastIndexOf("/"), compressedFilePath.length());
                String verificationName = bbiFilesToVerificationMap.getOrDefault(bbiFileName, null);
                if(verificationName != null){
                    uploadBbi(zipStream, verificationName, bbiFileName);
                }
            }
        }
    }

    private Map<String, String> getBBIfilesToVerificationMap(InputStream inputStream) {
        Map<String, String> bbiFilesToVerification = new LinkedHashMap<>();
        ZipEntry entry;
        try (ZipInputStream zipStream = new ZipInputStream(inputStream)) {
            while ((entry = zipStream.getNextEntry()) != null) {
                if (Pattern.compile(dbFileExtensionPattern, Pattern.CASE_INSENSITIVE).matcher(entry.getName()).matches()) {
                    File dbFile = File.createTempFile(entry.getName(), "");
                    try (OutputStream os = new FileOutputStream(dbFile)) {
                        IOUtils.copy(zipStream, os);
                        Class.forName("org.sqlite.JDBC");
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery("SELECT FileNumber, Id_pc FROM Results");
                        while (rs.next()) {
                            String verificationID = rs.getString("Id_pc");
                            String fileNumber = rs.getString("FileNumber");
                            bbiFilesToVerification.put(fileNumber, verificationID);
                        }
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bbiFilesToVerification;
    }

    @Override
    @Transactional
    public void uploadBbi(InputStream file, String idVerification, String originalFileFullName) throws IOException {
        String filename = originalFileFullName.substring(0, originalFileFullName.lastIndexOf('.'));
        System.out.println("gets filename");
        byte[] bytesOfBbi = IOUtils.toByteArray(file);
        System.out.println("gets bytes");
        Verification verification = verificationRepository.findOne(idVerification);
        System.out.println("finds verification");
        BbiProtocol bbiProtocol = new BbiProtocol(bytesOfBbi, verification, filename);
        System.out.println("creates protocol");
        verification.setBbiProtocol(bbiProtocol);
        System.out.println("sets protocol");
        uploadBbiRepository.save(bbiProtocol);
        System.out.println("saves protocol");
        verificationRepository.save(verification);
        System.out.println("saves verification");
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
        return userRepository.findOne(username);
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAllCalibratorEmployee(List<String> role, User employee) {
        List<EmployeeDTO> calibratorListEmployee = new ArrayList<>();
        if (role.contains(UserRole.CALIBRATOR_ADMIN.name())) {
            List<User> allAvailableUsersList = userRepository.findAllAvailableUsersByRoleAndOrganizationId(UserRole.CALIBRATOR_EMPLOYEE,
                    employee.getOrganization().getId())
                    .stream()
                    .collect(Collectors.toList());
            calibratorListEmployee = EmployeeDTO.giveListOfEmployeeDTOs(allAvailableUsersList);
        } else {
            EmployeeDTO userPage = new EmployeeDTO(employee.getUsername(), employee.getFirstName(),
                    employee.getLastName(), employee.getMiddleName(), role.get(0));
            calibratorListEmployee.add(userPage);
        }
        return calibratorListEmployee;
    }

    @Override
    @Transactional
    public void assignCalibratorEmployee(String verificationId, User calibratorEmployee) {
        Verification verification = verificationRepository.findOne(verificationId);
        verification.setCalibratorEmployee(calibratorEmployee);
        verification.setReadStatus(ReadStatus.READ);
        verification.setTaskStatus(Status.PLANNING_TASK);
        verificationRepository.save(verification);
    }
}
