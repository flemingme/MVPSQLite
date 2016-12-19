package com.fleming.exmaple.learnsqlite.view;

import android.support.v4.app.Fragment;

import com.fleming.exmaple.learnsqlite.R;
import com.fleming.exmaple.learnsqlite.base.BaseActivity;
import com.fleming.exmaple.learnsqlite.presenter.UserPresenter;

public class MainActivity extends BaseActivity {

    private UserFragment userFragment = new UserFragment();

    @Override
    protected Fragment createFragment() {
        return userFragment;
    }

    @Override
    protected void init() {
        super.init();
        new UserPresenter(userFragment);
    }

    @Override
    protected void onActivityCreate() {
        setContentView(R.layout.activity_base);
    }
}
