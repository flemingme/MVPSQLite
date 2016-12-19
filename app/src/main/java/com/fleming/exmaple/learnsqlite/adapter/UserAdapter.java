package com.fleming.exmaple.learnsqlite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fleming.exmaple.learnsqlite.R;
import com.fleming.exmaple.learnsqlite.local.User;

import java.util.List;

/**
 * UserAdapter
 * Created by Fleming on 2016/12/16.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<User> mUsers;
    private static OnItemClickListener slistener;

    public UserAdapter(Context c) {
        mInflater = LayoutInflater.from(c);
    }

    public UserAdapter(Context c, List<User> users) {
        mInflater = LayoutInflater.from(c);
        setData(users);
    }

    public void setData(List<User> data) {
        mUsers = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_user, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivLogo.setImageResource(R.drawable.logo);
        holder.tvName.setText(getItem(position).getName());
        holder.tvAge.setText(String.valueOf(getItem(position).getAge()));
    }

    private User getItem(int position) {
        return mUsers.size() != 0 ? mUsers.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void addItem(int position, User user) {
        mUsers.add(position, user);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        if (getItem(position) != null) {
            mUsers.remove(position);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private ImageView ivLogo;
        private TextView tvName;
        private TextView tvAge;

        public ViewHolder(View itemView) {
            super(itemView);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAge = (TextView) itemView.findViewById(R.id.tv_age);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            slistener.onItemClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            slistener.onItemLongClick(getLayoutPosition(), v);
            return true;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        slistener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
        void onItemLongClick(int position, View view);
    }

}
