package com.fleming.exmaple.mvpsqlite.contract;

import com.fleming.exmaple.mvpsqlite.base.BasePresenter;
import com.fleming.exmaple.mvpsqlite.base.BaseView;
import com.fleming.exmaple.mvpsqlite.local.User;

import java.util.List;

/**
 * UserContract
 * Created by Fleming on 2016/12/19.
 */

public class UserContract {

    public interface View extends BaseView<Presenter> {

        void showUsers(List<User> users);

        void showMessage(String msg);

    }

    public interface Presenter extends BasePresenter {

        User loadUser(int id);

        List<User> loadUsers();

        boolean releaseDB();

        boolean clearData();
    }

}
