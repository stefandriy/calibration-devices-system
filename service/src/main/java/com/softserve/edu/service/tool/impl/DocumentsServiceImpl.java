package com.softserve.edu.service.tool.impl;

import com.softserve.edu.common.Constants;
import com.softserve.edu.documents.DocumentFactory;
import com.softserve.edu.documents.FileFactory;
import com.softserve.edu.documents.document.Document;
import com.softserve.edu.documents.parameter.FileFormat;
import com.softserve.edu.documents.parameter.FileParameters;
import com.softserve.edu.documents.parameter.FileSystem;
import com.softserve.edu.documents.resources.DocumentType;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.tool.DocumentService;
import org.apache.commons.vfs2.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.File;
import java.util.*;

/**
 * Service for documents generation.
 */
@Service
@Transactional(readOnly = true)
public class DocumentsServiceImpl implements DocumentService {

    @Autowired
    CalibrationTestRepository calibrationTestRepository;
    @Autowired
    ProviderEmployeeService providerEmployeeService;
    @Autowired
    VerificationRepository verificationRepository;

    /**
     * Builds a file for a verification. Verification must have only one
     * test assigned to it.
     *
     * @param verificationCode verification's code
     * @param documentType     type of the needed document
     * @param fileFormat       format of the file
     * @return the built file
     * @throws IllegalStateException if one of parameters is incorrect
     */
    @Override
    public FileObject buildFile(String verificationCode,
                                DocumentType documentType, FileFormat fileFormat) {
        Verification verification = verificationRepository.findOne(verificationCode);

        Assert.notNull(verification, "can't find verification with id " + verificationCode);

        Set<CalibrationTest> calibrationTests = verification.getCalibrationTests();

        Assert.state(calibrationTests.size() != 0,
                Verification.class.getSimpleName() + " with id " +
                        verificationCode + " doesn't have any tests assigned to it");

        Assert.state(calibrationTests.size() == 1,
                Verification.class.getSimpleName() + " with id " +
                        verificationCode + " have more than one test assigned to it. " +
                        "Specify the id of a concrete test");

        CalibrationTest calibrationTest = verification.getCalibrationTests().
                iterator().next();

        return buildFile(documentType, verification, calibrationTest, fileFormat);
    }

    /**
     * Builds a file for verification and one of it's calibrations.
     *
     * @param verificationCode verification's code
     * @param documentType     type of the needed document
     * @param fileFormat       format of the file
     * @return the built file
     * @throws IllegalStateException if one of parameters is incorrect
     */
    @Override
    public FileObject buildFile(String verificationCode, Long calibrationTestID,
                                DocumentType documentType, FileFormat fileFormat) {
        Verification verification = verificationRepository.findOne(verificationCode);
        CalibrationTest calibrationTest = calibrationTestRepository.findOne(calibrationTestID);

        Assert.notNull(verification, "can't find a " +
                Verification.class.getSimpleName() + " with id " + verificationCode);

        Assert.notNull(calibrationTest, "can't find a " +
                CalibrationTest.class.getSimpleName() + " with id" + calibrationTestID);

        Assert.state(calibrationTest.getVerification().equals(verification),
                calibrationTest.getClass() + " with id:" +
                        CalibrationTest.class.getSimpleName() + " is not assigned to " +
                        verification.getClass() + " with id: " + verification.getId());

        return buildFile(documentType, verification, calibrationTest, fileFormat);
    }

    /**
     * Builds a file for verification. The last verification's test will be used
     * for the generated file. The document type is determined by the status
     * of test.
     *
     * @param verificationCode verification's code
     * @param fileFormat       format of the file
     * @return the built file
     * @throws IllegalStateException if one of parameters is incorrect
     */
    @Override
    public FileObject buildFile(String verificationCode, FileFormat fileFormat) {
        Verification verification = verificationRepository.findOne(verificationCode);
        Set<CalibrationTest> calibrationTests = verification.getCalibrationTests();

        CalibrationTest calibrationTest = calibrationTests.iterator().next();
        Verification.CalibrationTestResult testResult = calibrationTest.getTestResult();
        DocumentType documentType;

        switch (testResult) {
            case SUCCESS:
                documentType = DocumentType.VERIFICATION_CERTIFICATE;
                break;
            case FAILED:
                documentType = DocumentType.UNFITNESS_CERTIFICATE;
                break;
            default:
                throw new IllegalArgumentException(testResult.name() +
                        " is not supported");
        }

        return buildFile(documentType, verification, calibrationTest, fileFormat);
    }

    /**
     * Build a file using the specified parameters.
     *
     * @param documentType    type of the needed document
     * @param verification    the file's verification
     * @param calibrationTest one of verifiction's tests
     * @param fileFormat      format of the file
     * @return the built file
     */
    @Override
    public FileObject buildFile(DocumentType documentType, Verification verification,
                                CalibrationTest calibrationTest, FileFormat fileFormat) {
        Document document = DocumentFactory.build(documentType, verification,
                calibrationTest);

        FileParameters fileParameters = new FileParameters(document, documentType,
                fileFormat);
        fileParameters.setFileSystem(FileSystem.RAM);
        fileParameters.setFileName(documentType.toString());

        return FileFactory.buildFile(fileParameters);
    }

    @Override
    public FileObject buildInfoFile(String verificationCode, FileFormat fileFormat) {
        Verification verification = verificationRepository.findOne(verificationCode);

        DocumentType documentType = DocumentType.INFO_DOCUMENT;
        Document document = DocumentFactory.buildInfoDoc(documentType, verification);

        FileParameters fileParameters = new FileParameters(document, documentType,
                fileFormat);
        fileParameters.setFileSystem(FileSystem.RAM);
        fileParameters.setFileName(documentType.toString());

        return FileFactory.buildInfoFile(fileParameters);
    }

    @Override
    public File buildFile( Map<String, List<String>> data) throws Exception{
       // FileFactory.buildFile(getDataForProviderEmployeesReport(providerId));

        return FileFactory.buildFile(data);
    }

    public Map<String, List<String>> getDataForProviderEmployeesReport(Long providerId) {
        List<User> users = providerEmployeeService.getAllProviderEmployee(providerId);
        //String це назва колонки, List дані стовпця
        Map<String, List<String>> data = new LinkedHashMap<>();
        // ПІБ працівника
        List<String> employeeFullName = new ArrayList<>();
        // Кількість прийнятих заявок
        List<String> acceptedVerifications = new ArrayList<>();
        // Кількість відхилених заявок
        List<String> rejectedVerifications = new ArrayList<>();
        // Кількість виконаних заявок, всього
        List<String> allVerifications = new ArrayList<>();
        // Кількість виконаних заявок з результатом «придатний»
        List<String> doneSuccess = new ArrayList<>();
        //Кількість виконаних заявок з результатом «не придатний»
        List<String> doneFailed = new ArrayList<>();
        for (User user : users) {
            employeeFullName.add(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());

            acceptedVerifications.add(verificationRepository.countByProviderEmployeeUsernameAndStatus(
                    user.getUsername(), Status.ACCEPTED).toString());
            rejectedVerifications.add(verificationRepository.countByProviderEmployeeUsernameAndStatus(
                    user.getUsername(), Status.REJECTED).toString());
            Long done = verificationRepository.countByProviderEmployeeUsernameAndStatus(
                    user.getUsername(), Status.TEST_OK);
            doneSuccess.add(done.toString());
            Long failed = verificationRepository.countByProviderEmployeeUsernameAndStatus(
                    user.getUsername(), Status.TEST_NOK);
            doneFailed.add(failed.toString());
            Long all = done + failed;
            allVerifications.add(all.toString());
        }
        data.put(Constants.FULL_NAME, employeeFullName);
        data.put(Constants.COUNT_ACCEPTED_VERIFICATIONS, acceptedVerifications);
        data.put(Constants.COUNT_REJECTED_VERIFICATIONS, rejectedVerifications);
        data.put(Constants.COUNT_ALL_VERIFICATIONS, allVerifications);
        data.put(Constants.COUNT_OK_VERIFICATIONS, doneSuccess);
        data.put(Constants.COUNT_NOK_VERIFICATIONS, doneFailed);

        return data;
    }

}
