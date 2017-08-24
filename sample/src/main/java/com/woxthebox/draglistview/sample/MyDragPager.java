package com.woxthebox.draglistview.sample;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItemRecyclerView;
import com.woxthebox.draglistview.DragPager;
import com.woxthebox.draglistview.IPageModel;
import com.woxthebox.draglistview.PageHolder;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.id.list;
import static android.media.CamcorderProfile.get;

/**
 * 作者：husongzhen on 17/7/12 15:46
 * 邮箱：husongzhen@musikid.com
 */

public class MyDragPager extends DragPagerView<PageModel> {


    public MyDragPager(Context context) {
        super(context);
    }

    @Override
    public PageHolder onCreatePager(final BoardView boardView, ViewGroup pager, int index) {
        PageHolder pageHolder = new PageHolder();
        pageHolder.root = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.drag_item_recycler_view, boardView, false);
        ((TextView) pageHolder.root.findViewById(R.id.text)).setText("Column " + index);
        ((TextView) pageHolder.root.findViewById(R.id.text)).setTag(getItem(index));
        ((TextView) pageHolder.root.findViewById(R.id.text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPageModel pos = (IPageModel) v.getTag();
                int index = getDragPager().indexOfItem(pos);
                getDragPager().removePage(index);
            }
        });
        ((TextView) pageHolder.root.findViewById(R.id.item_count)).setText("");
        pageHolder.recyclerView = (DragItemRecyclerView) pageHolder.root.findViewById(R.id.recycler);
        ItemAdapter listAdapter = new ItemAdapter(getItem(index).getList(), R.layout.column_item, R.id.item_layout, true);
        getDragPager().initRecycler(boardView, listAdapter, pageHolder);
        return pageHolder;
    }


}
