package com.softserve.edu.service.tool;


import com.softserve.edu.documents.parameter.FileFormat;
import com.softserve.edu.documents.resources.DocumentType;
import org.apache.commons.vfs2.FileObject;

import java.util.List;
import java.util.Map;

public interface ReportsService {
    FileObject buildFile(Long providerId, DocumentType documentType, FileFormat fileFormat) throws Exception;

    Map<String, List<String>> getDataForProviderEmployeesReport(Long providerId);
}
