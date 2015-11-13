package com.windroilla.invoker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.windroilla.invoker.data.BlocktimeContract.BlocktimeEntry;

/**
 * Created by vishnu on 13/11/15.
 */
public class BlocktimeDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "blocktime.db";
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;


    public BlocktimeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_BLOCKTIME_TABLE = "CREATE TABLE " + BlocktimeEntry.TABLE_NAME + " (" +
                BlocktimeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BlocktimeEntry.COLUMN_START_TIME + " DATETIME NOT NULL, " +
                BlocktimeEntry.COLUMN_END_TIME + " DATETIME NOT NULL, " +
                BlocktimeEntry.COLUMN_CREATED_TIME + " DATETIME NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_BLOCKTIME_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BlocktimeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
