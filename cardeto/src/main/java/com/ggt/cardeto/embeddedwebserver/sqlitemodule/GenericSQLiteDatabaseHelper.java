package com.ggt.cardeto.embeddedwebserver.sqlitemodule;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Database helper to access any local database.
 *
 * @author guiguito
 */
public class GenericSQLiteDatabaseHelper {

    private String[] TABLES_TO_IGNORE = {"android_metadata", "sqlite_sequence"};
    // private String[] TABLES_TO_IGNORE = {};
    private String END_FILENAMES_TO_IGNORE = ".db-journal";

    private SQLiteDatabase mSqLiteDatabase;
    private Context mContext;

    public GenericSQLiteDatabaseHelper(Context context) {
        mContext = context;
    }

    private String getDatabasesLocalPath() {
        String result = "";
        PackageManager packageManager = mContext.getPackageManager();
        String packageName = mContext.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
            String packageDirectory = packageInfo.applicationInfo.dataDir;
            result = packageDirectory + "/databases";
        } catch (NameNotFoundException e) {
            // directory not found
            Log.e(getClass().toString(), e.getMessage());
        }
        return result;
    }

    public List<String> getDatabasesList() {
        List<String> result = new ArrayList<String>();
        File srcFile = new File(getDatabasesLocalPath());
        if (srcFile.isDirectory()) {
            String[] filenames = srcFile.list();
            for (String filename : filenames) {
                if (!filename.endsWith(END_FILENAMES_TO_IGNORE))
                    result.add(filename);
            }
        }
        return result;
    }

    /**
     * Check if the database exist and opens it is the case.
     *
     * @return true if it does exist, false otherwise.
     */
    public boolean checkAndOpenDataBase(String databaseName) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(getDatabasesLocalPath() + "/"
                    + databaseName, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database doesn't exist yet.
            Log.e(getClass().toString(), e.getMessage());
        }
        if (mSqLiteDatabase != null && checkDB != null) {
            mSqLiteDatabase.close();
        }
        mSqLiteDatabase = checkDB;
        return checkDB != null;
    }

    public void closeDatabase() {
        if (mSqLiteDatabase != null) {
            mSqLiteDatabase.close();
        }
    }

    public List<String> getTablesList() {
        List<String> results = new ArrayList<String>();
        if (mSqLiteDatabase != null) {
            Cursor cursor = mSqLiteDatabase.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table'", null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String tableName = cursor.getString(0);
                    boolean ignore = false;
                    for (String tableToIgnore : TABLES_TO_IGNORE) {
                        if (tableName.equalsIgnoreCase(tableToIgnore)) {
                            ignore = true;
                            break;
                        }
                    }
                    if (!ignore) {
                        results.add(tableName);
                    }
                    cursor.moveToNext();
                }
            }
        }
        return results;
    }

    public List<String> getColumnNames(String tableName) {
        List<String> results = new ArrayList<String>();
        if (mSqLiteDatabase != null) {
            Cursor cursor = mSqLiteDatabase.rawQuery("PRAGMA table_info("
                    + tableName + ")", null);
            if (cursor.moveToFirst()) {
                do {
                    results.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        return results;
    }

    public List<List<String>> getRows(String tableName) {
        List<List<String>> results = new ArrayList<List<String>>();
        if (mSqLiteDatabase != null) {
            Cursor cursor = mSqLiteDatabase.rawQuery("SELECT * FROM "
                    + tableName, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    List<String> row = new ArrayList<String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        // cursor.get
                        // TODO check type ?
                        row.add(cursor.getString(i));
                    }
                    results.add(row);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return results;
    }

    public boolean doesTableExist(String tableName) {
        boolean result = false;
        if (mSqLiteDatabase != null) {
            Cursor cursor = mSqLiteDatabase.rawQuery(
                    "SELECT name FROM sqlite_master WHERE name='" + tableName
                            + "'", null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                result = true;
                cursor.close();
            }
        }
        return result;
    }

    public List<List<String>> executeRequest(String request) {
        List<List<String>> results = new ArrayList<List<String>>();
        if (mSqLiteDatabase != null) {
            Cursor cursor = mSqLiteDatabase.rawQuery(request, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    List<String> row = new ArrayList<String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        // cursor.get
                        // TODO check type ?
                        row.add(cursor.getString(i));
                    }
                    results.add(row);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return results;
    }
}
