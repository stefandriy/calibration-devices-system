package com.softserve.edu.service.tool;


import com.softserve.edu.documents.document.Document;
import com.softserve.edu.documents.parameter.FileFormat;
import com.softserve.edu.documents.resources.DocumentType;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.Verification;
import org.apache.commons.vfs2.FileObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public interface DocumentService {

    FileObject buildFile(String verificationCode, DocumentType documentType, FileFormat fileFormat);

    FileObject buildFile(String verificationCode, Long calibrationTestID, DocumentType documentType, FileFormat fileFormat);

    FileObject buildFile(String verificationCode, FileFormat fileFormat);

    FileObject buildFile(DocumentType documentType, Verification verification, CalibrationTest calibrationTest, FileFormat fileFormat);

    FileObject buildInfoFile(String verificationCode, FileFormat fileFormat);
    FileObject buildFile(Map<String, List<String>> data, DocumentType documentType, FileFormat fileFormat) throws Exception;

    Map<String, List<String>> getDataForProviderEmployeesReport(Long providerId);
}
