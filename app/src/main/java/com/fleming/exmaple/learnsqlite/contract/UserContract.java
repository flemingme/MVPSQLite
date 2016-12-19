package com.fleming.exmaple.learnsqlite.contract;

import com.fleming.exmaple.learnsqlite.base.BasePresenter;
import com.fleming.exmaple.learnsqlite.base.BaseView;
import com.fleming.exmaple.learnsqlite.local.User;

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

        List<User> loadUsers();

        boolean releaseDB();

        boolean clearData();
    }

}
