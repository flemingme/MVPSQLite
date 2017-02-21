package com.fleming.exmaple.mvpsqlite.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fleming.exmaple.mvpsqlite.R;
import com.fleming.exmaple.mvpsqlite.local.DBManager;
import com.fleming.exmaple.mvpsqlite.local.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UserAdapter
 * Created by Fleming on 2016/12/16.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<User> mUsers;
    private OnItemClickListener itemClickListener;
    private OnStartDragListener dragListener;

    public UserAdapter(@NonNull List<User> users) {
        mUsers = (users != null ? users : new ArrayList<User>());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivLogo;
        private TextView tvName;
        private TextView tvAge;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAge = (TextView) itemView.findViewById(R.id.tv_age);
            this.itemView = itemView;
        }
    }

    public void setData(List<User> data) {
        mUsers = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ivLogo.setImageResource(R.drawable.logo);
        holder.tvName.setText(mUsers.get(position).getName());
        holder.tvAge.setText(String.valueOf(mUsers.get(position).getAge()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    Log.d("chen", holder.getAdapterPosition() + "");
                    itemClickListener.onItemClick(holder.getAdapterPosition(), v);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemLongClick(holder.getAdapterPosition(), v);
                }
                return false;
            }
        });
        holder.ivLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    public void insertItem(int position, User user) {
        if (user != null) {
            DBManager.getInstance().insert(user);
            mUsers.add(position, user);
            notifyItemInserted(position);
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mUsers, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        User user = mUsers.get(position);
        Log.d("chen", position + "拖动删除" + " user:" + user.toString());
        DBManager.getInstance().delete(user.getId());
        mUsers.remove(position);
        notifyItemRemoved(position);
    }

    public void modifyItem(int position) {
        User user = mUsers.get(position);
        user.setName("Lucy");
        DBManager.getInstance().update(user.getId(), user);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(@NonNull OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setOnDragListener(OnStartDragListener listener) {
        dragListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);

        void onItemLongClick(int position, View view);
    }
}
