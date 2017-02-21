package com.fleming.exmaple.mvpsqlite.adapter;

/**
 * ItemTouchHelperAdapter
 * Created by Fleming on 2016/12/16.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}