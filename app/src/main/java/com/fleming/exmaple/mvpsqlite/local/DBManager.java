package com.fleming.exmaple.mvpsqlite.local;

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
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (user.getId() != 0) values.put(DBContract.UserEntity.COLUMN_NAME_UESR_ID, user.getId());
        if (!TextUtils.isEmpty(user.getName())) values.put(DBContract.UserEntity.COLUMN_NAME_USER_NAME, user.getName());
        if (user.getAge() != 0) values.put(DBContract.UserEntity.COLUMN_NAME_USER_AGE, user.getAge());

        long _id = db.insert(DBContract.UserEntity.TABLE_NAME, null, values);

        db.close();

        return _id > 0;
    }

    @Override
    public boolean delete(int userId) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final String selection = DBContract.UserEntity.COLUMN_NAME_UESR_ID + "=?";
        final String[] selectionArgs = new String[]{String.valueOf(userId)};
        Cursor query = mHelper.getReadableDatabase().query(DBContract.UserEntity.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        int _id = 0;
        if (query.getCount() > 0) {
            _id = db.delete(DBContract.UserEntity.TABLE_NAME, selection, selectionArgs);
        }

        query.close();
        db.close();

        return _id > 0;
    }

    @Override
    public boolean update(int userId, User user) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final String selection = DBContract.UserEntity.COLUMN_NAME_UESR_ID + "=?";
        final String[] selectionArgs = new String[]{String.valueOf(userId)};

        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(user.getName())) values.put(DBContract.UserEntity.COLUMN_NAME_USER_NAME, user.getName());
        if (user.getAge() != 0) values.put(DBContract.UserEntity.COLUMN_NAME_USER_AGE, user.getAge());

        Cursor query = mHelper.getReadableDatabase().query(DBContract.UserEntity.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        int _id = 0;
        if (query.getCount() > 0) {
            _id = db.update(DBContract.UserEntity.TABLE_NAME, values, selection, selectionArgs);
        }

        query.close();
        db.close();

        return _id > 0;
    }

    @Override
    public User selectById(int userId) {
        final SQLiteDatabase db = mHelper.getReadableDatabase();
        final String selection = DBContract.UserEntity.COLUMN_NAME_UESR_ID + "=?";
        final String[] selectionArgs = new String[]{String.valueOf(userId)};

        Cursor cursor = db.query(DBContract.UserEntity.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        User user = null;
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_UESR_ID));
            String name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_USER_NAME));
            int age = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_USER_AGE));
            user = new User(_id, name, age);
        }

        cursor.close();
        db.close();

        return user;
    }

    @Override
    public List<User> selectAll() {
        final SQLiteDatabase db = mHelper.getReadableDatabase();
        List<User> list = new ArrayList<>();
        Cursor cursor = db.query(DBContract.UserEntity.TABLE_NAME, null, null, null, null, null, DBContract.UserEntity.COLUMN_NAME_UESR_ID);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int _id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_UESR_ID));
            String name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_USER_NAME));
            int age = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_USER_AGE));
            list.add(new User(_id, name, age));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return list;
    }

    @Override
    public boolean deleteAll() {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        int _id = db.delete(DBContract.UserEntity.TABLE_NAME, null, null);

        db.close();

        return _id > 0;
    }

    @Override
    public boolean release() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        if (db.isOpen()) {
            db.close();
            return true;
        }
        return false;
    }
}
