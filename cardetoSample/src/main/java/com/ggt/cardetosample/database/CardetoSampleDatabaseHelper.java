package com.ggt.cardetosample.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DatabaseHelper to store logs.
 *
 * @author guiguito
 */
public class CardetoSampleDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_LOG = "logs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOG = "log";

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_LOG
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LOG + " text not null);";

    public CardetoSampleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CardetoSampleDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
        onCreate(db);
    }

}