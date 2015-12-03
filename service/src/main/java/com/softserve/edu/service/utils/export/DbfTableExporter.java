package com.softserve.edu.service.utils.export;

import com.linuxense.javadbf.DBFField;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileOutputStream;
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
        DBFField[] fields = new DBFField[data.size()];
        Object[] fieldNames = data.keySet().toArray();
        List<Integer> lengths = getCellLengths(data);
        for (int i = 0; i < data.size(); ++i) {
            fields[i] = new DBFField();
            fields[i].setName(String.valueOf(fieldNames[i]));
            fields[i].setDataType(DBFField.FIELD_TYPE_C);
            fields[i].setFieldLength(lengths.get(i) + extraLength);
        }

        com.linuxense.javadbf.DBFWriter writer = new com.linuxense.javadbf.DBFWriter();
        writer.setFields(fields);

        ArrayList<List<String>> values = new ArrayList<List<String>>();
        Object[] valArray = data.values().toArray();
        for (int i = 0; i < data.values().size(); ++i) {
            values.add((List<String>)valArray[i]);
        }
        for (int i = 0; i < values.get(0).size(); ++i) {
            Object[] row = new Object[values.size()];
            for (int j = 0; j < values.size(); ++j) {
                row[j] = values.get(j).get(i);
            }
            writer.addRecord(row);
        }

        FileOutputStream fos = new FileOutputStream(output);
        writer.write(fos);
        fos.close();
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