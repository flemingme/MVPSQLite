package com.fleming.exmaple.learnsqlite.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fleming.exmaple.learnsqlite.base.Constants;

/**
 * DBHelper
 * Created by Fleming on 2016/11/30.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    public DBHelper(Context c, String dbName) {
        super(c, dbName, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Constants.TABLE_NAME + "( "
                + Constants.UESR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + Constants.USER_NAME + " TEXT, "
                + Constants.USER_AGE +" INTEGER );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
            onCreate(db);
        }
    }
}
