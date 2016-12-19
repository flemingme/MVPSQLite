package com.fleming.exmaple.learnsqlite.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fleming.exmaple.learnsqlite.R;
import com.fleming.exmaple.learnsqlite.adapter.DefaultItemDecoration;
import com.fleming.exmaple.learnsqlite.adapter.UserAdapter;
import com.fleming.exmaple.learnsqlite.contract.UserContract;
import com.fleming.exmaple.learnsqlite.local.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * UserFragment
 * Created by Fleming on 2016/12/16.
 */

public class UserFragment extends Fragment implements UserAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, UserContract.View {

    private static final String TAG = "MainActivity";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.sf_layout)
    SwipeRefreshLayout swipeLayout;
    private UserAdapter mAdapter;
    private UserContract.Presenter mPresenter;
    private boolean isRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DefaultItemDecoration());
        List<User> users = mPresenter.loadUsers();
        if (users == null && users.size() == 0) {
            mAdapter = new UserAdapter(getActivity());
        } else {
            mAdapter = new UserAdapter(getActivity(), users);
        }
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull UserContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onItemClick(int position, View view) {
        mAdapter.removeItem(position);
    }

    @Override
    public void onItemLongClick(int position, View view) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.init:
                mPresenter.start();
                break;
            case R.id.clear:
                mPresenter.clearData();
                break;
            case R.id.menu_add:
                mAdapter.addItem(mAdapter.getItemCount(), new User("李四", 22));
                break;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter.releaseDB()) {
            Log.d(TAG, "onDestroy: db is released");
        }
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) isRefresh = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 2; i++) {
                    mAdapter.addItem(i, new User("李四" + i, 22 + i));
                }
                swipeLayout.setRefreshing(false);
                isRefresh = false;
            }
        }, 500);
    }

    @Override
    public void showUsers(@NonNull List<User> users) {
        mAdapter.setData(users);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
