package com.fleming.exmaple.learnsqlite;

import android.support.v4.app.Fragment;

public class MainActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return new UserFragment();
    }

    @Override
    protected void onActivityCreate() {
        setContentView(R.layout.fragment_main);
    }
}
