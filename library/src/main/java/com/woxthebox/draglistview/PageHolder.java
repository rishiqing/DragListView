package com.woxthebox.draglistview;

import android.view.View;
import android.view.ViewGroup;

public class PageHolder {
    public ViewGroup root;
    public DragItemRecyclerView recyclerView;
    private int topDistance = 0;
    private int bottomDistance = 0;

    //    测量item的位置
    public void measureListDistance() {
        int topY = getListTopY();
        int parentY = getParentY();
        int rootY = getRootY();
        topDistance = parentY - rootY;
        int recyclerViewMeasuredHeight = recyclerView.getMeasuredHeight();
        int rootMeasuredHeight = root.getMeasuredHeight();
        bottomDistance = (rootMeasuredHeight + rootY) - (recyclerViewMeasuredHeight + topY);
    }

    private int getRootY() {
        int[] rootLocation = new int[2];
        root.getLocationInWindow(rootLocation);
        return rootLocation[1];
    }

    private int getParentY() {
        int[] prect = new int[2];
        View parent = (View) recyclerView.getParent();
        parent.getLocationInWindow(prect);
        return prect[1];
    }

    private int getListTopY() {
        int[] recyclerViewLocation = new int[2];
        recyclerView.getLocationInWindow(recyclerViewLocation);
        return recyclerViewLocation[1];
    }

    public int getBottomDistance() {
        return bottomDistance;
    }

    public int getTopDistance() {
        return topDistance;
    }
}
