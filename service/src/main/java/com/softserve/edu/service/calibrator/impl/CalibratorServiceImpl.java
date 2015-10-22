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
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.RandomStringUtils;
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
        File directoryWithUnpackedFiles = null;
        try {
            directoryWithUnpackedFiles = unpackArchive(inputStream, originalFileFullName);
        } catch (ZipException e) {
            e.printStackTrace();
        }

        Map<String, String> bbiFileNamesToVerificationMap = getBBIfilesToVerificationMap(directoryWithUnpackedFiles);
        List<File> listOfBBIfiles = new ArrayList<>(FileUtils.listFiles(directoryWithUnpackedFiles, new String[]{"bbi"}, true));

        for(File bbiFile: listOfBBIfiles){
            String verification = bbiFileNamesToVerificationMap.getOrDefault(bbiFile.getName(), null);
           if(verification != null){
                uploadBbi(FileUtils.openInputStream(bbiFile), verification, bbiFile.getName());
            }
        }

    }

    private File unpackArchive(InputStream inputStream, String originalFileFullName) throws IOException, ZipException {
        String randomDirectoryName = RandomStringUtils.random(8);
        File directoryForUnpacking = FileUtils.getFile(FileUtils.getTempDirectoryPath(), randomDirectoryName);
        FileUtils.forceMkdir(directoryForUnpacking);

        File zipFileDownloaded = FileUtils.getFile(FileUtils.getTempDirectoryPath(), originalFileFullName);

        try (OutputStream os = new FileOutputStream(zipFileDownloaded)) {
            IOUtils.copy(inputStream, os);
        }

        ZipFile zipFile = new ZipFile(zipFileDownloaded);
        zipFile.extractAll(directoryForUnpacking.toString());

        return directoryForUnpacking;
    }

    private Map<String, String> getBBIfilesToVerificationMap(File directoryWithUnpackedFiles) {
        Map<String, String> bbiFilesToVerification = new LinkedHashMap<>();
        List<File> listOfDBFs = new ArrayList<>(FileUtils.listFiles(directoryWithUnpackedFiles, new String[]{"db"}, true));

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        File singleBBIfile = listOfDBFs.get(0);

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + singleBBIfile);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT FileNumber, Id_pc FROM Results");
            while (rs.next()) {
                            String verificationID = rs.getString("Id_pc");
                            String fileNumber = rs.getString("FileNumber");
                            bbiFilesToVerification.put(fileNumber, verificationID);
            }
        } catch (SQLException e) {
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
