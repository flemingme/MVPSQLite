package com.fleming.exmaple.mvpsqlite.base;

import android.app.Application;

import com.fleming.exmaple.mvpsqlite.local.DBManager;

/**
 * Created by Fleming on 2016/12/13.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.getInstance().initDBManager(getApplicationContext(), "myApp.db");
    }
}
