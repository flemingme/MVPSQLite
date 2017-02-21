package com.fleming.exmaple.mvpsqlite.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fleming.exmaple.mvpsqlite.R;
import com.fleming.exmaple.mvpsqlite.adapter.DividerDecoration;
import com.fleming.exmaple.mvpsqlite.adapter.ItemTouchCallback;
import com.fleming.exmaple.mvpsqlite.adapter.OnStartDragListener;
import com.fleming.exmaple.mvpsqlite.adapter.UserAdapter;
import com.fleming.exmaple.mvpsqlite.contract.UserContract;
import com.fleming.exmaple.mvpsqlite.local.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * UserFragment
 * Created by Fleming on 2016/12/16.
 */

public class UserFragment extends Fragment implements UserAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, UserContract.View, OnStartDragListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.sf_layout)
    SwipeRefreshLayout swipeLayout;
    private UserAdapter mAdapter;
    private UserContract.Presenter mPresenter;
    private boolean isRefresh;
    private List<User> mUsers = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        //设置列表展示
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerDecoration());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new UserAdapter(mUsers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnDragListener(this);

        //设置item拖拽和滑出
        ItemTouchHelper.Callback callback = new ItemTouchCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
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
        showMessage(String.valueOf(position));
    }

    @Override
    public void onItemLongClick(int position, View view) {
        mAdapter.modifyItem(position);
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
                mAdapter.insertItem(mAdapter.getItemCount(), new User(mAdapter.getItemCount() + 1, "王二小", 23));
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
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
                    mAdapter.insertItem(mAdapter.getItemCount(), new User(mAdapter.getItemCount() + 1, "李世民", 33));
                }
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                swipeLayout.setRefreshing(false);
                isRefresh = false;
            }
        }, 500);
    }

    @Override
    public void showUsers(@NonNull List<User> users) {
        mUsers = users;
        mAdapter.setData(users);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
