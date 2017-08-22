package com.woxthebox.draglistview;

import android.view.ViewGroup;

public class PageHolder {
    public ViewGroup root;
    public DragItemRecyclerView recyclerView;
    private int topDistance = 0;
    private int bottomDistance = 0;

    //    测量item的位置
    public void measureListDistance() {
        int[] recyclerViewLocation = new int[2];
        recyclerView.getLocationOnScreen(recyclerViewLocation);
        int topY = recyclerViewLocation[1];
        int[] rootLocation = new int[2];
        root.getLocationOnScreen(rootLocation);
        int rootY = rootLocation[1];
        topDistance = topY - rootY;
        int recyclerViewMeasuredHeight = recyclerView.getMeasuredHeight();
        int rootMeasuredHeight = root.getMeasuredHeight();
        bottomDistance = (rootMeasuredHeight + rootY) - (recyclerViewMeasuredHeight + topY);
    }

    public int getBottomDistance() {
        return bottomDistance;
    }

    public int getTopDistance() {
        return topDistance;
    }
}
