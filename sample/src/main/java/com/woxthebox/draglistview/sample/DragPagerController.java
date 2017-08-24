package com.woxthebox.draglistview.sample;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragPager;
import com.woxthebox.draglistview.IPageModel;
import com.woxthebox.draglistview.PageHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：husongzhen on 17/7/12 15:46
 * 邮箱：husongzhen@musikid.com
 */

public class DragPagerController extends DragPager<IPageModel> {


    private List<IPageModel> data = new ArrayList<>();
    private SparseArray<DragPagerView> cachePager = new SparseArray<>();

    public void setData(List<IPageModel> data) {
        this.data = data;
        notifyData();
    }

    private Context context;

    public DragPagerController(Context context) {
        this.context = context;
    }


    public DragPagerController regester(Class clazz, DragPagerView pagerView) {
        cachePager.put(clazz.hashCode(), pagerView);
        pagerView.setDragPager(this);
        return this;
    }

    public BoardView getBoardView() {
        return boardView;
    }


    public void addItem(IPageModel item) {
        data.add(item);
    }

    public void addItem(int index, IPageModel item) {
        data.add(index, item);
        boardView.notifyItemInsert(index);
    }


    public int indexOfItem(IPageModel model) {
        return data.indexOf(model);
    }

    @Override
    public int getPagerCount() {
        return data.size();
    }


    public List<IPageModel> getData() {
        return data;
    }

    @Override
    public IPageModel getItem(int index) {
        return data.get(index);
    }

    @Override
    protected PageHolder onCreatePager(BoardView boardView, ViewGroup pager, int index) {
        DragPagerView pagerView = cachePager.get(getItem(index).getClass().hashCode());
        if (CodeCheck.isNotNull(pagerView)) {
            return pagerView.onCreatePager(boardView, pager, index);
        }
        return null;
    }


    public void removePage(Integer index) {
        boardView.removeColumn(index);
        data.remove(getItem(index));
    }
}
