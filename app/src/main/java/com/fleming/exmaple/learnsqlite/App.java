package com.fleming.exmaple.learnsqlite;

import android.app.Application;

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
