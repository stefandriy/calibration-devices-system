package com.softserve.edu.service.utils.export;

import com.hexiong.jdbf.DBFWriter;
import com.hexiong.jdbf.JDBField;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yurko on 28.11.2015.
 */
@Getter
@Setter
public class DbfTableExporter implements TableExporter {
    // Additional width for each cell for better view
    private int extraLength;

    // region Constructors

    public DbfTableExporter() {
        extraLength = 2;
    }

    public DbfTableExporter(int extraLength) {
        this.extraLength = extraLength;
    }

    // endregion

    public void export(Map<String, List<String>> data, File output) throws Exception {
        JDBField[] fields = new JDBField[data.size()];
        List<Integer> lengths = getCellLengths(data);
        Object[] header = data.keySet().toArray();

        // region Create columns

        for (int i = 0; i < header.length; ++i) {
            fields[i] = new JDBField(header[i].toString(), 'C', lengths.get(i) + extraLength, 0);
        }

        // endregion

        DBFWriter dbfWriter = new DBFWriter(output.getAbsolutePath(), fields);
        int recordsLength = data.get(header[0]).size();

        // region Fill table

        for (int i = 0; i < recordsLength; ++i) {
            Object[] row = new Object[data.size()];
            for (int j = 0; j < data.size(); ++j) {
                row[j] = data.get(header[j]).get(i);
            }
            dbfWriter.addRecord(row);
        }

        // endregion

        dbfWriter.close();
    }

    /**
     * Get maximum length for each column
     *
     * @param data Table data
     * @return Max length for each column
     */
    private List<Integer> getCellLengths(Map<String, List<String>> data) {
        Object[] header = data.keySet().toArray();
        List<Integer> lengths = new ArrayList<>();

        for (int i = 0; i < data.size(); ++i) {
            int max = header[i].toString().length();
            List<String> content = data.get(header[i]);
            for (int j = 0; j < content.size(); ++j) {
                int cellLength = content.get(j).length();
                max = cellLength > max ? cellLength : max;
            }
            lengths.add(max);
        }
        return lengths;
    }
}