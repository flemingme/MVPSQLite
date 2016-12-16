package com.fleming.exmaple.learnsqlite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * UserFragment
 * Created by Fleming on 2016/12/16.
 */

public class UserFragment extends Fragment {

    private static final String TAG = "MainActivity";
    @BindView(R.id.add)
    Button addBtn;
    @BindView(R.id.delete)
    Button deleteBtn;
    @BindView(R.id.update)
    Button updateBtn;
    @BindView(R.id.select)
    Button selectBtn;
    @BindView(R.id.result)
    TextView tvResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(getActivity());
        return v;
    }

    @OnClick({R.id.add, R.id.delete, R.id.update, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                initData();
                break;
            case R.id.delete:
                break;
            case R.id.update:
                break;
            case R.id.select:
                selectAll();
                break;
        }
    }

    private void initData() {
        boolean flag = false;
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("陈加龙" + i);
            user.setAge(20 + i);
            flag = DBManager.getInstance().insert(user);
        }
        Log.d(TAG, "initData: " + flag);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DBManager.getInstance().release();
    }

    private void selectAll() {
        List<User> list = DBManager.getInstance().selectAll();
        StringBuilder sb = new StringBuilder();
        for (User u : list) {
            sb.append(u.toString()).append("\n");
        }
        tvResult.setText(sb.toString());
    }
}
