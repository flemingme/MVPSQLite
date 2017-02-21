package com.fleming.exmaple.mvpsqlite.local;

import java.util.List;

/**
 * DBOperateMethods
 * Created by Fleming on 2016/12/13.
 */

public interface DBOperateMethods {

    boolean insert(User user);

    boolean delete(int userId);

    boolean update(int userId, User user);

    User selectById(int userId);

    List<User> selectAll();

    boolean release();

    boolean deleteAll();
}
