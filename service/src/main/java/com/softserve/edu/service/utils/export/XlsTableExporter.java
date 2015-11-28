package com.softserve.edu.service.utils.export;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yurko on 27.11.2015.
 */
@Getter
@Setter
public class XlsTableExporter implements TableExporter {
    // Additional width for each cell for better view
    private int extraLength;

    private int fontSize;

    private String sheetName;

    // region Constructors

    public XlsTableExporter() {
        sheetName = " ";
        extraLength = 2;
        fontSize = 10;
    }

    public XlsTableExporter(String sheetName) {
        this.sheetName = sheetName;
        extraLength = 2;
        fontSize = 10;
    }

    public XlsTableExporter(String sheetName, int extraLength, int fontSize) {
        this.sheetName = sheetName;
        this.extraLength = extraLength;
        this.fontSize = fontSize;
    }

    // endregion

    @Override
    public void export(Map<String, List<String>> data, String destination) throws Exception {
        File output = new File(destination);
        WritableWorkbook myDoc = Workbook.createWorkbook(output);
        WritableSheet sheet = myDoc.createSheet(sheetName, 0);
        Object[] header = data.keySet().toArray();

        List<Integer> lengths = getCellLengths(data);

        // region Create table header

        WritableFont boldItemFont = new WritableFont(WritableFont.ARIAL, fontSize);
        boldItemFont.setBoldStyle(WritableFont.BOLD);

        WritableCellFormat boldItemFormat = new WritableCellFormat(boldItemFont);
        boldItemFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

        for (int i = 0; i < header.length; ++i) {
            Label headerItem = new Label(i, 0, header[i].toString(), boldItemFormat);
            sheet.setColumnView(0, lengths.get(i) + extraLength);
            sheet.addCell(headerItem);
        }

        // endregion

        // region Fill table body

        WritableFont itemFont = new WritableFont(WritableFont.ARIAL, fontSize);
        WritableCellFormat itemFormat = new WritableCellFormat(itemFont);
        itemFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

        for (int i = 0; i < data.size(); ++i) {
            List<String> row = data.get(header[i]);
            for (int j = 0; j < row.size(); ++j) {
                Label item = new Label(i, j + 1, row.get(j), itemFormat);
                sheet.setColumnView(j, lengths.get(j) + extraLength);
                sheet.addCell(item);
            }
        }

        // endregion

        myDoc.write();
        myDoc.close();
    }

    /**
     * Get maximum length for each column
     *
     * @param data Table data
     * @return Max length for each column
     */
    private List<Integer> getCellLengths(Map<String, List<String>> data) {
        Object[] header = data.keySet().toArray();
        List<Integer> lengths = new ArrayList<Integer>();

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