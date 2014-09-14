package com.ggt.cardetosample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Datasource deal with logs  usage in a sqlite database.
 *
 * @author guiguito
 */
public class CardetoSampleDatabaseDataSource {
    // Database fields
    private SQLiteDatabase database;
    private CardetoSampleDatabaseHelper dbHelper;
    private String[] allColumns = {CardetoSampleDatabaseHelper.COLUMN_ID,
            CardetoSampleDatabaseHelper.COLUMN_LOG};

    public CardetoSampleDatabaseDataSource(Context context) {
        dbHelper = new CardetoSampleDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Log createLog(String log) {
        ContentValues values = new ContentValues();
        values.put(CardetoSampleDatabaseHelper.COLUMN_LOG, log);
        long insertId = database.insert(CardetoSampleDatabaseHelper.TABLE_LOG,
                null, values);
        Cursor cursor = database.query(CardetoSampleDatabaseHelper.TABLE_LOG,
                allColumns, CardetoSampleDatabaseHelper.COLUMN_ID + " = "
                        + insertId, null, null, null, null);
        cursor.moveToFirst();
        Log newLog = cursorToLog(cursor);
        cursor.close();
        return newLog;
    }

    public void deleteLog(Log log) {
        long id = log.getId();
        System.out.println("Log deleted with id: " + id);
        database.delete(CardetoSampleDatabaseHelper.TABLE_LOG,
                CardetoSampleDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public void clearLogs() {
        database.delete(CardetoSampleDatabaseHelper.TABLE_LOG, null, null);
    }

    public List<Log> getAllLogs() {
        List<Log> logs = new ArrayList<Log>();

        Cursor cursor = database.query(CardetoSampleDatabaseHelper.TABLE_LOG,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log log = cursorToLog(cursor);
            logs.add(log);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return logs;
    }

    private Log cursorToLog(Cursor cursor) {
        Log log = new Log();
        log.setId(cursor.getLong(0));
        log.setLog(cursor.getString(1));
        return log;
    }

}
