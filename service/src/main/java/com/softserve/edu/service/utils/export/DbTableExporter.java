package com.softserve.edu.service.utils.export;

import org.tmatesoft.sqljet.core.SqlJetEncoding;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yurko on 18.12.2015.
 */
public class DbTableExporter {
    private String tableName;

    public DbTableExporter() {
        this.tableName = "Task";
    }

    public DbTableExporter(String tableName) {
        this.tableName = tableName;
    }

    public OutputStream export(Map<String, List<String>> data, OutputStream output) throws Exception {
        return null;
    }

    public void export(Map<String, List<String>> data, File output) throws Exception {
        output.delete();

        SqlJetDb db = SqlJetDb.open(output, true);
        //db.getOptions().setEncoding(SqlJetEncoding.UTF8);
        db.getOptions().setAutovacuum(true);
        db.runTransaction(new ISqlJetTransaction() {
            public Object run(SqlJetDb db) throws SqlJetException {
                db.getOptions().setUserVersion(1);
                return true;
            }
        }, SqlJetTransactionMode.WRITE);


        db.beginTransaction(SqlJetTransactionMode.WRITE);
        Object[] keys = data.keySet().toArray();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
        for (int i = 0; i < data.size() - 1; ++i) {
            createTableQuery += keys[i].toString() + " TEXT, ";
        }
        createTableQuery += keys[data.size() - 1].toString() + " TEXT) ";
        createTableQuery += "PRAGMA encoding = \"UTF-8\"";
        db.createTable(createTableQuery);
        db.beginTransaction(SqlJetTransactionMode.WRITE);
        try {
            ISqlJetTable table = db.getTable(tableName);

            List<List<String>> values = new ArrayList<List<String>>();
            Object[] valArray = data.values().toArray();
            for (int i = 0; i < data.values().size(); ++i) {
                values.add((List<String>)valArray[i]);
            }
            for (int i = 0; i < values.get(0).size(); ++i) {
                Object[] row = new Object[values.size()];
                for (int j = 0; j < values.size(); ++j) {
                    row[j] = values.get(j).get(i);
                }
                table.insert(row);
            }
        } finally {
            db.commit();
        }
        db.close();
    }
}
