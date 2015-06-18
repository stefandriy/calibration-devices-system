package com.softserve.edu.service;

import com.softserve.edu.documents.DocumentFactory;
import com.softserve.edu.documents.FileFactory;
import com.softserve.edu.documents.document.Document;
import com.softserve.edu.documents.parameter.FileFormat;
import com.softserve.edu.documents.parameter.FileParameters;
import com.softserve.edu.documents.parameter.FileSystem;
import com.softserve.edu.documents.resources.DocumentType;
import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.util.CalibrationTestResult;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.apache.commons.vfs2.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * Service for documents generation.
 */
@Service
@Transactional(readOnly = true)
public class DocumentsService {

    @Autowired
    VerificationRepository verificationRepository;

    @Autowired
    CalibrationTestRepository calibrationTestRepository;

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
    public FileObject buildFile(String verificationCode, FileFormat fileFormat) {
        Verification verification = verificationRepository.findOne(verificationCode);
        Set<CalibrationTest> calibrationTests = verification.getCalibrationTests();

        CalibrationTest calibrationTest = calibrationTests.iterator().next();
        CalibrationTestResult testResult = calibrationTest.getTestResult();
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
     * @param documentType type of the needed document
     * @param verification the file's verification
     * @param calibrationTest one of verifiction's tests
     * @param fileFormat format of the file
     * @return the built file
     */
    private FileObject buildFile(DocumentType documentType, Verification verification,
                                 CalibrationTest calibrationTest, FileFormat fileFormat) {
        Document document = DocumentFactory.build(documentType, verification,
                calibrationTest);

        FileParameters fileParameters = new FileParameters(document, documentType,
                fileFormat);
        fileParameters.setFileSystem(FileSystem.RAM);
        fileParameters.setFileName(documentType.toString());

        return FileFactory.buildFile(fileParameters);
    }
}
