package com.softserve.edu.service.utils.export;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Yurko on 23.12.2015.
 */
@Getter
@Setter
public class TableExportColumn {
    // Column name
    private String name;

    // Column data type
    private String type;

    // Column data
    private List<String> data;

    public TableExportColumn(String name, List<String> data) {
        this.name = name;
        this.data = data;
    }

    public TableExportColumn(String name, String type, List<String> data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
