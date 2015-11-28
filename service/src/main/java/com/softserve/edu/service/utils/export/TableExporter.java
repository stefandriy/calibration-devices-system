package com.softserve.edu.service.utils.export;

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
     * @param destination Result filepath
     */
    void export(Map<String, List<String>> data, String destination) throws Exception;
}