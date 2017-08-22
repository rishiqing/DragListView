package com.woxthebox.draglistview;

import android.view.ViewGroup;

public class PageHolder {
    public ViewGroup root;
    public DragItemRecyclerView recyclerView;
    private int distance = 0;

    //    测量item的位置
    public void measureListDistance() {
        int[] recyclerViewLocation = new int[2];
        recyclerView.getLocationOnScreen(recyclerViewLocation);
        int rY = recyclerViewLocation[1];
        int[] rootLocation = new int[2];
        root.getLocationOnScreen(rootLocation);
        int rootY = rootLocation[1];
        distance = rY - rootY;
    }

    public int getDistance() {
        return distance;
    }
}
