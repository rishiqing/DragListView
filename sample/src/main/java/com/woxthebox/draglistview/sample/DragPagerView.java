package com.woxthebox.draglistview.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.IPageModel;
import com.woxthebox.draglistview.PageHolder;

/**
 * 作者：husongzhen on 17/8/21 14:22
 * 邮箱：husongzhen@musikid.com
 */

public abstract class DragPagerView<T extends IPageModel> {


    protected Context context;
    protected DragPagerController dragPager;

    public DragPagerView(Context context) {
        this.context = context;
    }

    public void setDragPager(DragPagerController dragPager) {
        this.dragPager = dragPager;
    }


    public T getItem(int index) {
        T t = (T) dragPager.getItem(index);
        return t;
    }


    public DragPagerController getDragPager() {
        return dragPager;
    }

    public CodeQuery inflater(int layout, View parent) {
        View root = LayoutInflater.from(context).inflate(layout, (ViewGroup) parent, false);
        CodeQuery query = new CodeQuery(context).setRoot(root);
        return query;
    }


    public abstract PageHolder onCreatePager(BoardView boardView, ViewGroup pager, int index);
}
