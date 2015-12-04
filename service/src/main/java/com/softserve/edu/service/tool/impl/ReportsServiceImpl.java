package com.softserve.edu.service.tool.impl;

import com.softserve.edu.common.Constants;
import com.softserve.edu.documents.FileFactory;
import com.softserve.edu.documents.parameter.FileFormat;
import com.softserve.edu.documents.parameter.FileParameters;
import com.softserve.edu.documents.parameter.FileSystem;
import com.softserve.edu.documents.resources.DocumentType;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.tool.ReportsService;
import org.apache.commons.vfs2.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Service for reports generation.
 */
@Service
@Transactional(readOnly = true)
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    VerificationRepository verificationRepository;

    public FileObject buildFile(Long providerId, DocumentType documentType,
                                FileFormat fileFormat) throws Exception {
        FileParameters fileParameters = new FileParameters(documentType, fileFormat);
        fileParameters.setFileSystem(FileSystem.RAM);
        fileParameters.setFileName(documentType.toString());
        Map<String, List<String>> data = getDataForProviderEmployeesReport(providerId);
        return FileFactory.buildReportFile(data, fileParameters);
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
