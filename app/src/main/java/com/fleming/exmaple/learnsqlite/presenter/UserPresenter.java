package com.fleming.exmaple.learnsqlite.presenter;

import android.support.annotation.NonNull;

import com.fleming.exmaple.learnsqlite.contract.UserContract;
import com.fleming.exmaple.learnsqlite.local.DBManager;
import com.fleming.exmaple.learnsqlite.local.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fleming on 2016/12/19.
 */

public class UserPresenter implements UserContract.Presenter {

    private final UserContract.View mUserView;

    public UserPresenter(@NonNull UserContract.View userView) {
        mUserView = userView;
        mUserView.setPresenter(this);
    }

    @Override
    public void start() {
        DBManager.getInstance().deleteAll();
        boolean flag = false;
        for (int i = 0; i < 5; i++) {
            flag = DBManager.getInstance().insert(new User("陈加龙" + i, 20 + i));
        }

        if (flag) {
            mUserView.showMessage("5条数据插入成功");
            mUserView.showUsers(loadUsers());
        }
    }

    @Override
    public List<User> loadUsers() {
        return DBManager.getInstance().selectAll();
    }

    @Override
    public boolean releaseDB() {
        return DBManager.getInstance().release();
    }

    @Override
    public boolean clearData() {
        mUserView.showUsers(new ArrayList<User>(0));
        return DBManager.getInstance().deleteAll();
    }
}
