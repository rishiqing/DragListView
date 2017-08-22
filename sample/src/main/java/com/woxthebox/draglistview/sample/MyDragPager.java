package com.woxthebox.draglistview.sample;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItemRecyclerView;
import com.woxthebox.draglistview.DragPager;
import com.woxthebox.draglistview.PageHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：husongzhen on 17/7/12 15:46
 * 邮箱：husongzhen@musikid.com
 */

public class MyDragPager extends DragPager {


    private List<ArrayList<Pair<Long, String>>> data = new ArrayList<>();

    public void setData(List<ArrayList<Pair<Long, String>>> data) {
        this.data = data;
    }

    private Context context;

    public MyDragPager(Context context) {
        this.context = context;
    }

    @Override
    public int getPagerCount() {
        return data.size();
    }

    @Override
    public Object getItem(int index) {
        return data.get(index);
    }

    @Override
    protected PageHolder onCreatePager(BoardView boardView, ViewGroup pager, int index) {
        PageHolder pageHolder = new PageHolder();
        pageHolder.root = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.drag_item_recycler_view, boardView, false);
        ((TextView) pageHolder.root.findViewById(R.id.text)).setText("Column ");
        ((TextView) pageHolder.root.findViewById(R.id.item_count)).setText("");
        pageHolder.recyclerView = (DragItemRecyclerView) pageHolder.root.findViewById(R.id.recycler);
        ItemAdapter listAdapter = new ItemAdapter(data.get(index), R.layout.column_item, R.id.item_layout, true);
        initRecycler(boardView, listAdapter, pageHolder.recyclerView);
        return pageHolder;
    }


}
