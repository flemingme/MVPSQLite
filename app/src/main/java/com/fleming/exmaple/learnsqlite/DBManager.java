package com.fleming.exmaple.learnsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DBManager
 * Created by Fleming on 2016/12/2.
 */

public class DBManager implements DBOperateMethods {

    private static DBManager sManager = null;
    private DBHelper mHelper;

    public static DBManager getInstance() {
        if (sManager == null) {
            synchronized (DBManager.class) {
                if (sManager == null) {
                    sManager = new DBManager();
                }
            }
        }
        return sManager;
    }

    public void initDBManager(Context context, String dbName) {
        if (context != null) {
            mHelper = new DBHelper(context, dbName);
        }
    }


    @Override
    public boolean insert(User user) {
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(user.getName())) values.put(Constants.USER_NAME, user.getName());
        if (user.getAge() != 0) values.put(Constants.USER_AGE, user.getAge());
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        long _id = db.insert(Constants.TABLE_NAME, null, values);
        return _id > 0;
    }

    @Override
    public boolean delete(int userId) {
        final String whereClause = Constants.UESR_ID + "=?";
        final String[] whereArgs = new String[]{String.valueOf(userId)};
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        int _id = db.delete(Constants.TABLE_NAME, whereClause, whereArgs);
        return _id > 0;
    }

    @Override
    public boolean update(int userId, User user) {
        final String selection = Constants.UESR_ID + "=?";
        final String[] selectionArgs = new String[]{String.valueOf(userId)};
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(user.getName())) values.put(Constants.USER_NAME, user.getName());
        if (user.getAge() != 0) values.put(Constants.USER_AGE, user.getAge());

        Cursor query = mHelper.getReadableDatabase().query(Constants.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        int _id = 0;
        if (query.getCount() > 0) {
            _id = db.update(Constants.TABLE_NAME, values, selection, selectionArgs);
        }
        query.close();
        return _id > 0;
    }

    @Override
    public User selectById(int userId) {
        final String selection = Constants.UESR_ID + "=?";
        final String[] selectionArgs = new String[]{String.valueOf(userId)};
        final SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        User user = null;
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndex(Constants.UESR_ID));
            String name = cursor.getString(cursor.getColumnIndex(Constants.USER_NAME));
            int age = cursor.getInt(cursor.getColumnIndex(Constants.USER_AGE));
            user = new User(_id, name, age);
        }
        cursor.close();
        return user;
    }

    @Override
    public List<User> selectAll() {
        final SQLiteDatabase db = mHelper.getReadableDatabase();
        List<User> list = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME, null, null, null, null, null, Constants.UESR_ID);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int _id = cursor.getInt(cursor.getColumnIndex(Constants.UESR_ID));
            String name = cursor.getString(cursor.getColumnIndex(Constants.USER_NAME));
            int age = cursor.getInt(cursor.getColumnIndex(Constants.USER_AGE));
            list.add(new User(_id, name, age));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @Override
    public void release() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        if (db.isOpen()) {
            db.close();
        }
    }
}
