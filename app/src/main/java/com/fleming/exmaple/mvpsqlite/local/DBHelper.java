package com.fleming.exmaple.mvpsqlite.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBHelper
 * Created by Fleming on 2016/11/30.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 5;

    public DBHelper(Context c, String dbName) {
        super(c, dbName, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + DBContract.UserEntity.TABLE_NAME + "( "
                + DBContract.UserEntity.COLUMN_NAME_UESR_ID + " INTEGER PRIMARY KEY NOT NULL, "
                + DBContract.UserEntity.COLUMN_NAME_USER_NAME + " TEXT, "
                + DBContract.UserEntity.COLUMN_NAME_USER_AGE +" INTEGER );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntity.TABLE_NAME);
            onCreate(db);
        }
    }
}
