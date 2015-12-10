package com.softserve.edu.service.utils.export;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Yurko on 27.11.2015.
 */
public interface TableExporter {
    OutputStream export(Map<String, List<String>> data, OutputStream output) throws Exception;
}