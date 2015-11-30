package com.softserve.edu.service.utils.export;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Yurko on 27.11.2015.
 */
public interface TableExporter {
    /**
     * Performs exporting to XLS
     *
     * @param data        Table for exporting
     * @param output      Output file
     */
    void export(Map<String, List<String>> data, File output) throws Exception;
}