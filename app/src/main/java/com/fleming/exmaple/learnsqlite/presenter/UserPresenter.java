package com.fleming.exmaple.learnsqlite.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fleming.exmaple.learnsqlite.contract.UserContract;
import com.fleming.exmaple.learnsqlite.local.DBManager;
import com.fleming.exmaple.learnsqlite.local.User;

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
            flag = DBManager.getInstance().insert(new User(i, "张三丰" + i, 20 + i));
        }

        if (flag) {
            mUserView.showMessage("5条数据插入成功");
            mUserView.showUsers(loadUsers());
        }
    }

    @Override
    public boolean addUser(User user) {
        boolean flag = DBManager.getInstance().insert(user);
        if (flag) {
            Log.d("chen", "addUser: " + flag);
            mUserView.showUsers(loadUsers());
        }
        return flag;
    }

    @Override
    public boolean removeUser(int id) {
        boolean flag = DBManager.getInstance().delete(id);
        if (flag) {
            Log.d("chen", "removeUser: " + flag);
            mUserView.showUsers(loadUsers());
        }
        return flag;
    }

    @Override
    public boolean modifyData(int id, User newUser) {
        boolean flag = DBManager.getInstance().update(id, newUser);
        if (flag) mUserView.showUsers(loadUsers());
        return flag;
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
