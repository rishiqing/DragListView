/*
 * Copyright 2014 Magnus Woxblom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.woxthebox.draglistview.sample;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public abstract class SuperDragItemAdapter<M> extends DragItemAdapter<M, SuperDragItemAdapter.ViewHolder> {

    protected int mLayoutId;
    protected int mGrabHandleId;
    private boolean mDragOnLongPress;

    public SuperDragItemAdapter(ArrayList<M> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        setHasStableIds(true);
        setItemList(list);
    }


    public M getItem(int pos) {


        return mItemList.get(pos);
    }


    @Override
    public void onBindViewHolder(SuperDragItemAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindData(holder, position);
    }

    protected void onBindData(ViewHolder holder, int position) {

    }


    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mText;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            mText = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void onItemClicked(View view) {
            Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
