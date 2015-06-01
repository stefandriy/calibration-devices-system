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

import java.io.InputStream;
import java.util.Set;

@Service
public class DocumentsService {

    @Autowired
    VerificationRepository verificationRepository;

    @Autowired
    CalibrationTestRepository calibrationTestRepository;

    public FileObject getFile(String verificationCode, DocumentType documentType, FileFormat fileFormat) {
        Verification verification = verificationRepository.findOne(verificationCode);

        // check input parameters
        Assert.notNull(verification, "can't find verification with id " + verificationCode);

        Set<CalibrationTest> calibrationTests = verification.getCalibrationTests();

        Assert.state(calibrationTests.size() != 0,
                verification.getClass() + " with id " + verificationCode + " doesn't have any tests assigned to it");

        Assert.state(calibrationTests.size() == 1,
                verification.getClass() + " with id " + verificationCode + " have more than one test assigned to it. " +
                        "Specify the id of a concrete test");

        CalibrationTest calibrationTest = verification.getCalibrationTests().iterator().next();

        return builFile(documentType, verification, calibrationTest, fileFormat);
    }

    public FileObject getFile(String verificationCode, Long calibrationTestID,
                              DocumentType documentType, FileFormat fileFormat) {
        Verification verification = verificationRepository.findOne(verificationCode);
        CalibrationTest calibrationTest = calibrationTestRepository.findOne(calibrationTestID);

        // check input parameters
        Assert.notNull(verification, "can't find a " + Verification.class + " with id " + verificationCode);

        Assert.notNull(calibrationTest, "can't find a " + CalibrationTest.class + " with id" + calibrationTestID);

        Assert.state(calibrationTest.getVerification().equals(verification),
                calibrationTest.getClass() + " with id:" + CalibrationTest.class + " is not assigned to " +
                        verification.getClass() + " with id: " + verification.getId());

        return builFile(documentType, verification, calibrationTest, fileFormat);
    }

    private FileObject builFile(DocumentType documentType, Verification verification,
                                CalibrationTest calibrationTest, FileFormat fileFormat) {
        Document document = DocumentFactory.build(documentType, verification, calibrationTest);

        FileParameters fileParameters = new FileParameters(document, documentType, fileFormat);
        fileParameters.setFileSystem(FileSystem.RAM);

        return FileFactory.buildFile(fileParameters);
    }

    public FileObject getFile(String verificationCode, FileFormat fileFormat) {
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

        return builFile(documentType, verification, calibrationTest, fileFormat);
    }
}
