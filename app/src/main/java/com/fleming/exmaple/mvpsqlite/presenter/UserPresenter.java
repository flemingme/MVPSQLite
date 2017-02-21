package com.fleming.exmaple.mvpsqlite.presenter;

import android.support.annotation.NonNull;

import com.fleming.exmaple.mvpsqlite.contract.UserContract;
import com.fleming.exmaple.mvpsqlite.local.DBManager;
import com.fleming.exmaple.mvpsqlite.local.User;

import java.util.ArrayList;
import java.util.List;

/**
 * UserPresenter
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
            flag = DBManager.getInstance().insert(new User(i+1, "张三丰" + i, 20 + i));
        }

        if (flag) {
            mUserView.showMessage("数据初始化成功");
            mUserView.showUsers(loadUsers());
        }
    }

    @Override
    public User loadUser(int id) {
        return DBManager.getInstance().selectById(id);
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
