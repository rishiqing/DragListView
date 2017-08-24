package com.woxthebox.draglistview.sample;

import android.support.v4.util.Pair;

import com.woxthebox.draglistview.IPageModel;

import java.util.ArrayList;

/**
 * 作者：husongzhen on 17/8/24 13:17
 * 邮箱：husongzhen@musikid.com
 */

public class PageModel implements IPageModel {

    ArrayList<Pair<Long, String>> list = new ArrayList<>();
    public void add(Pair<Long, String> item){
        list.add(item);
    }


    public ArrayList<Pair<Long, String>> getList() {
        return list;
    }

    @Override
    public int getKey() {
        return hashCode();
    }
}
