package com.fleming.exmaple.mvpsqlite.local;

import android.provider.BaseColumns;

/**
 * DBContract
 * Created by Fleming on 2016/12/2.
 */

public final class DBContract {

    private DBContract() {}

    public static abstract class UserEntity implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_UESR_ID = "_id";
        public static final String COLUMN_NAME_USER_NAME = "name";
        public static final String COLUMN_NAME_USER_AGE = "age";
    }
}
