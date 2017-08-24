package com.woxthebox.draglistview.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItemRecyclerView;
import com.woxthebox.draglistview.IPageModel;
import com.woxthebox.draglistview.PageHolder;

/**
 * 作者：husongzhen on 17/7/12 15:46
 * 邮箱：husongzhen@musikid.com
 */

public class MyDragTwoPager extends DragPagerView<PageModel> implements IPageModel{


    public MyDragTwoPager(Context context) {
        super(context);
    }

    @Override
    public PageHolder onCreatePager(final BoardView boardView, ViewGroup pager, int index) {
        PageHolder pageHolder = new PageHolder();
        pageHolder.root = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.drag_item_recycler_two_view, boardView, false);

        return pageHolder;
    }


    @Override
    public int getKey() {
        return hashCode();
    }
}
