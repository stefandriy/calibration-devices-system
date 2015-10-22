package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.ReadStatus;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.BbiProtocol;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.AdditionalInfo;
import com.softserve.edu.repository.*;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.storage.FileOperations;
import com.softserve.edu.service.utils.EmployeeDTO;

import com.softserve.edu.service.utils.ExcelFileDTO;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.time.LocalTime;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class CalibratorServiceImpl implements CalibratorService {

    private static final String[] bbiExtensions = {"bbi", "BBI"};
    private static final String[] dbfExtensions = {"db", "dbf", "DB", "DBF"};

    @Autowired
    private OrganizationRepository calibratorRepository;

    @Autowired
    private UploadBbiRepository uploadBbiRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileOperations fileOperations;

    @Autowired
    private AdditionalInfoRepository additionalInfoRepository;

    private final Logger logger = Logger.getLogger(CalibratorServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return calibratorRepository.findOne(id);
    }

    @Override
    @Transactional
    public Map<Boolean, String> uploadArchive(InputStream inputStream, String originalFileFullName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        File directoryWithUnpackedFiles = null;
        directoryWithUnpackedFiles = unpackArchive(inputStream, originalFileFullName);

        Map<String, String> bbiFileNamesToVerificationMap = getBBIfileNamesToVerificationMap(directoryWithUnpackedFiles);
        List<File> listOfBBIfiles = new ArrayList<>(FileUtils.listFiles(directoryWithUnpackedFiles, bbiExtensions, true));
        Map<Boolean, String> resultsOfBBIProcessing = processListOfBBIFiles(bbiFileNamesToVerificationMap, listOfBBIfiles);
        return resultsOfBBIProcessing;
    }

    private Map<Boolean, String> processListOfBBIFiles(Map<String, String> bbiFileNamesToVerificationMap, List<File> listOfBBIfiles){
        Map<Boolean, String> resultsOfBBIProcessing = new LinkedHashMap<>();
        for (File bbiFile : listOfBBIfiles) {
            String verification = bbiFileNamesToVerificationMap.getOrDefault(bbiFile.getName(), null);
            if (verification != null) {
                try {
                    uploadBbi(FileUtils.openInputStream(bbiFile), verification, bbiFile.getName());
                } catch (IOException e) {
                    resultsOfBBIProcessing.put(false, bbiFile.getName());
                }
                resultsOfBBIProcessing.put(true, bbiFile.getName());
            }
        }
        return resultsOfBBIProcessing;
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

    private Map<String, String> getBBIfileNamesToVerificationMap(File directoryWithUnpackedFiles) throws SQLException, ClassNotFoundException, FileNotFoundException {
        Map<String, String> bbiFilesToVerification = new LinkedHashMap<>();
        Optional<File> foundDBFile = FileUtils.listFiles(directoryWithUnpackedFiles, dbfExtensions, true).stream().findFirst();
        File dbFile = foundDBFile.orElseThrow(() -> new FileNotFoundException("DBF not found"));

        Class.forName("org.sqlite.JDBC");

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT FileNumber, Id_pc FROM Results");
            while (rs.next()) {
                String verificationID = rs.getString("Id_pc");
                String fileNumber = rs.getString("FileNumber");
                bbiFilesToVerification.put(fileNumber, verificationID);
            }
        }
        return bbiFilesToVerification;
    }

    @Override
    @Transactional
    public void uploadBbi(InputStream fileStream, String idVerification,
                          Long installmentNumber, String originalFileFullName) throws IOException {
        String absolutePath = fileOperations.putBbiFile(fileStream, installmentNumber, originalFileFullName);
        Verification verification = verificationRepository.findOne(idVerification);
        BbiProtocol bbiProtocol = new BbiProtocol(originalFileFullName, absolutePath, verification);
        verification.setBbiProtocol(bbiProtocol);
        verificationRepository.save(verification);
        System.out.println("saved verification");
        uploadBbiRepository.save(bbiProtocol);
        System.out.println("saved bbi!!!!111");
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

    @Override
    public void saveInfo(int entrance, int doorCode, int floor, Date dateOfVerif, String time, boolean serviceability, Date noWaterToDate, String notes, String verificationId) {
        Verification verification = verificationRepository.findOne(verificationId);
        verification.setAddInfoExists(true);
        LocalTime timeFrom;
        LocalTime timeTo;
        if (time == null){
            timeFrom = null;
            timeTo = null;
        } else {
            String timeFromString = time.substring(0, 5);
            String timeToString = time.substring(6, 11);
            timeFrom = LocalTime.parse(timeFromString);
            timeTo = LocalTime.parse(timeToString);
        }
        additionalInfoRepository.save(new AdditionalInfo(entrance, doorCode, floor, dateOfVerif, timeFrom, timeTo, serviceability,
                noWaterToDate, notes, verification));
        verificationRepository.save(verification);
    }

    @Override
    public boolean checkIfAdditionalInfoExists(String verificationId) {
        Verification verification = verificationRepository.findOne(verificationId);
        return verification.isAddInfoExists();
    }

    @Override
    public AdditionalInfo findAdditionalInfoByVerifId(String verificationId) {
        return additionalInfoRepository.findAdditionalInfoByVerificationId(verificationId);
    }

}
