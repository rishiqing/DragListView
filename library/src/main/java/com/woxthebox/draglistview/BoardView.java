/*
 * Copyright 2014 Magnus Woxblom
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.woxthebox.draglistview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class BoardView extends HorizontalScrollView implements AutoScroller.AutoScrollListener {


    private int width;
    private int page_margin;
    private int insertX;

    public interface BoardListener {
        void onItemDragStarted(int column, int row);

        void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow);

        void onItemChangedColumn(int oldColumn, int newColumn);

        void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow);
    }

    private static final int SCROLL_ANIMATION_DURATION = 425;
    private Scroller mScroller;
    private AutoScroller mAutoScroller;
    private GestureDetector mGestureDetector;
    private FrameLayout mRootLayout;
    protected LinearLayout mColumnLayout;
    protected ArrayList<PageHolder> mLists = new ArrayList<>();
    //    protected DragItemRecyclerView mCurrentRecyclerView;
    protected PageHolder mCurrentHolder;
    protected DragItem mDragItem;
    protected BoardListener mBoardListener;
    private boolean mSnapToColumnWhenScrolling = true;
    private boolean mSnapToColumnWhenDragging = true;
    private boolean mSnapToColumnInLandscape = false;
    private float mTouchX;
    private float mTouchY;
    protected int mColumnWidth;
    protected int mDragStartColumn;
    protected int mDragStartRow;
    private boolean mHasLaidOut;
    protected boolean mDragEnabled = true;
    protected int mLastDragColumn = NO_POSITION;
    protected int mLastDragRow = NO_POSITION;


    private DragPager dragPager;
    private SparseArray<PageHolder> cachePage = new SparseArray<>();

    public void setDragPager(DragPager dragPager) {
        this.dragPager = dragPager;
        dragPager.setBoardView(this);
    }

    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Resources res = getResources();
        boolean isPortrait = res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (isPortrait) {
            width = res.getDisplayMetrics().widthPixels;
            mColumnWidth = (int) (width * 0.87);
            page_margin = (width - mColumnWidth) / 2;
        } else {
            mColumnWidth = (int) (res.getDisplayMetrics().density * 320);
        }


        mGestureDetector = new GestureDetector(getContext(), new GestureListener());
        mScroller = new Scroller(getContext(), new DecelerateInterpolator(1.1f));
        mAutoScroller = new AutoScroller(getContext(), this);
        mAutoScroller.setAutoScrollMode(snapToColumnWhenDragging() ? AutoScroller.AutoScrollMode.COLUMN : AutoScroller.AutoScrollMode
                .POSITION);
        mDragItem = new DragItem(getContext());

        mRootLayout = new FrameLayout(getContext());
        mRootLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

        mColumnLayout = new LinearLayout(getContext());
        mColumnLayout.setOrientation(LinearLayout.HORIZONTAL);
        mColumnLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        mColumnLayout.setMotionEventSplittingEnabled(false);
        mColumnLayout.setPadding(page_margin, 0, page_margin, 0);
        mRootLayout.addView(mColumnLayout);
        mRootLayout.addView(mDragItem.getDragItemView());
        addView(mRootLayout);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // Snap to closes column after first layout.
        // This is needed so correct column is scrolled to after a rotation.
        if (!mHasLaidOut && snapToColumnWhenScrolling()) {
            scrollToColumn(getClosestColumn(), true);
        }
        mHasLaidOut = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean retValue = handleTouchEvent(event);
        return retValue || super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retValue = handleTouchEvent(event);
        return retValue || super.onTouchEvent(event);
    }

    private boolean handleTouchEvent(MotionEvent event) {
        if (mLists.size() == 0) {
            return false;
        }
        mTouchX = event.getX();
        mTouchY = event.getY();
        if (isDragging()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (!mAutoScroller.isAutoScrolling()) {
                        updateScrollPosition();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mAutoScroller.stopAutoScroll();
                    mCurrentHolder.recyclerView.onDragEnded(mCurrentHolder);
                    if (snapToColumnWhenScrolling()) {
                        scrollToColumn(getColumnOfList(mCurrentHolder.recyclerView), true);
                    }
                    invalidate();
                    break;
            }
            return true;
        } else {
            if (snapToColumnWhenScrolling() && mGestureDetector.onTouchEvent(event)) {
                // A page fling occurred, consume event
                return true;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!mScroller.isFinished()) {
                        // View was grabbed during animation
                        mScroller.forceFinished(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (snapToColumnWhenScrolling()) {
                        scrollToColumn(getClosestColumn(), true);
                    } else {
                        scrollToColumn(currectPager, true);
                    }
                    break;
            }
            return false;
        }
    }

    @Override
    public void computeScroll() {
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (getScrollX() != x || getScrollY() != y) {
                scrollTo(x, y);
            }

            // If auto scrolling at the same time as the scroller is running,
            // then update the drag item position to prevent stuttering item
            if (mAutoScroller.isAutoScrolling()) {
                mDragItem.setPosition(getListTouchX(mCurrentHolder), getListTouchY(mCurrentHolder));
            }

            ViewCompat.postInvalidateOnAnimation(this);
        } else {
//            super.computeScroll();
        }
    }

    @Override
    public void onAutoScrollPositionBy(int dx, int dy) {
        if (isDragging()) {
            scrollBy(dx, dy);
            updateScrollPosition();
        } else {
            mAutoScroller.stopAutoScroll();
        }
    }

    @Override
    public void onAutoScrollColumnBy(int columns) {
        if (isDragging()) {
            PageHolder holder = getCurrentRecyclerView(getWidth() / 2 + getScrollX());
            DragItemRecyclerView currentList = holder.recyclerView;
            int newColumn = getColumnOfList(currentList) + columns;
            if (columns != 0 && newColumn >= 0 && newColumn < dragPager.getPagerCount()) {
                scrollToColumn(newColumn, true);
            }
            updateScrollPosition();
        } else {
            mAutoScroller.stopAutoScroll();
        }
    }

    private void updateScrollPosition() {
        // Updated event to scrollview coordinates
        PageHolder holder = getCurrentRecyclerView(mTouchX + getScrollX());
        DragItemRecyclerView currentList = holder.recyclerView;
        if (currentList == null) {
            return;
        }
        if (currentList != null && mCurrentHolder.recyclerView != currentList) {
            int oldColumn = getColumnOfList(mCurrentHolder.recyclerView);
            int newColumn = getColumnOfList(currentList);
            long itemId = mCurrentHolder.recyclerView.getDragItemId();
            Object item = mCurrentHolder.recyclerView.removeDragItemAndEnd();
            if (item != null) {
                mCurrentHolder = holder;
                mCurrentHolder.recyclerView.addDragItemAndStart(getListTouchY(mCurrentHolder), item, itemId);
                mDragItem.setOffset(getItemParentView(mCurrentHolder).getLeft(), mCurrentHolder.recyclerView.getTop());

                if (mBoardListener != null) {
                    mBoardListener.onItemChangedColumn(oldColumn, newColumn);
                }
            }
        }

        // Updated event to list coordinates
        mCurrentHolder.recyclerView.onDragging(mCurrentHolder, getListTouchX(mCurrentHolder), getListTouchY(mCurrentHolder));

        float scrollEdge = getResources().getDisplayMetrics().widthPixels * 0.14f;
        if (mTouchX > getWidth() - scrollEdge && getScrollX() < mColumnLayout.getWidth()) {
            mAutoScroller.startAutoScroll(AutoScroller.ScrollDirection.LEFT);
        } else if (mTouchX < scrollEdge && getScrollX() > 0) {
            mAutoScroller.startAutoScroll(AutoScroller.ScrollDirection.RIGHT);
        } else {
            mAutoScroller.stopAutoScroll();
        }
        invalidate();
    }

    public float getListTouchX(PageHolder holder) {
        return mTouchX + getScrollX() - getItemParentView(holder).getLeft();
    }

    float getListTouchY(PageHolder holder) {


        return mTouchY - holder.recyclerView.getTop();
    }

    private PageHolder getCurrentRecyclerView(float x) {
        for (PageHolder list : mLists) {
            View parent = list.root;
            if (parent.getLeft() <= x && parent.getRight() > x) {
                return list;
            }
        }
        return mCurrentHolder;
    }

    protected int getColumnOfList(DragItemRecyclerView list) {
        int column = 0;
        for (int i = 0; i < mLists.size(); i++) {
            RecyclerView tmpList = mLists.get(i).recyclerView;
            if (tmpList == list) {
                column = findIndexByItem(mLists.get(i).root);
            }
        }
        return column;
    }

    private int getCurrentColumn(float posX) {
        for (int i = 0; i < mLists.size(); i++) {
            View parent = mLists.get(i).root;
            int index = findIndexByItem(parent);
            int left = getPageLeftX(index);
            int right = left + mColumnWidth;
            if (left <= posX && right > posX) {
                return index;
            }
        }
        return 0;
    }

    private int findIndexByItem(View parent) {
        return mColumnLayout.indexOfChild(parent);
    }

    private int getClosestColumn() {
        int middlePosX = getScrollX() + getMeasuredWidth() / 2;
        int column = 0;
        int minDiffX = Integer.MAX_VALUE;
        for (int i = 0; i < mLists.size(); i++) {
            int listPosX = mLists.get(i).root.getLeft();
            int diffX = Math.abs(listPosX + mColumnWidth / 2 - middlePosX);
            if (diffX < minDiffX) {
                minDiffX = diffX;
                column = findIndexByItem(mLists.get(i).root);
            }
        }
        return column;
    }

    private boolean snapToColumnWhenScrolling() {
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        return mSnapToColumnWhenScrolling && (isPortrait || mSnapToColumnInLandscape);
    }

    private boolean snapToColumnWhenDragging() {
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        return mSnapToColumnWhenDragging && (isPortrait || mSnapToColumnInLandscape);
    }

    private boolean isDragging() {
        return mCurrentHolder != null && mCurrentHolder.recyclerView.isDragging();
    }

    public RecyclerView getRecyclerView(int column) {
        if (column >= 0 && column < cachePage.size()) {
            IPageModel obj = (IPageModel) dragPager.getItem(column);
            return cachePage.get(getKey(obj)).recyclerView;
        }
        return null;
    }

    public DragItemAdapter getAdapter(int column) {
        if (column >= 0 && column < mLists.size()) {
            IPageModel item = dragPager.getItem(column);
            return (DragItemAdapter) cachePage.get(getKey(item)).recyclerView.getAdapter();
        }
        return null;
    }

    public int getItemCount() {
        int count = 0;
        for (PageHolder list : mLists) {
            count += list.recyclerView.getAdapter().getItemCount();
        }
        return count;
    }

    public int getItemCount(int column) {
        if (mLists.size() > column) {
            return mLists.get(column).recyclerView.getAdapter().getItemCount();
        }
        return 0;
    }

    public int getColumnCount() {
        return mLists.size();
    }


    public void removeItem(int column, int row) {
        if (!isDragging() && mLists.size() > column && mLists.get(column).recyclerView.getAdapter().getItemCount() > row) {
            DragItemAdapter adapter = (DragItemAdapter) mLists.get(column).recyclerView.getAdapter();
            adapter.removeItem(row);
        }
    }

    public void addItem(int column, int row, Object item, boolean scrollToItem) {
        if (!isDragging() && mLists.size() > column && mLists.get(column).recyclerView.getAdapter().getItemCount() >= row) {
            DragItemAdapter adapter = (DragItemAdapter) mLists.get(column).recyclerView.getAdapter();
            adapter.addItem(row, item);
            if (scrollToItem) {
                scrollToItem(column, row, false);
            }
        }
    }

    public void moveItem(int fromColumn, int fromRow, int toColumn, int toRow, boolean scrollToItem) {
        if (!isDragging() && mLists.size() > fromColumn && mLists.get(fromColumn).recyclerView.getAdapter().getItemCount() > fromRow
                && mLists.size() > toColumn && mLists.get(toColumn).recyclerView.getAdapter().getItemCount() >= toRow) {
            DragItemAdapter adapter = (DragItemAdapter) mLists.get(fromColumn).recyclerView.getAdapter();
            Object item = adapter.removeItem(fromRow);
            adapter = (DragItemAdapter) mLists.get(toColumn).recyclerView.getAdapter();
            adapter.addItem(toRow, item);
            if (scrollToItem) {
                scrollToItem(toColumn, toRow, false);
            }
        }
    }

    public void moveItem(long itemId, int toColumn, int toRow, boolean scrollToItem) {
        for (int i = 0; i < mLists.size(); i++) {
            RecyclerView.Adapter adapter = mLists.get(i).recyclerView.getAdapter();
            final int count = adapter.getItemCount();
            for (int j = 0; j < count; j++) {
                long id = adapter.getItemId(j);
                if (id == itemId) {
                    moveItem(i, j, toColumn, toRow, scrollToItem);
                    return;
                }
            }
        }
    }

    public void replaceItem(int column, int row, Object item, boolean scrollToItem) {
        if (!isDragging() && mLists.size() > column && mLists.get(column).recyclerView.getAdapter().getItemCount() > row) {
            DragItemAdapter adapter = (DragItemAdapter) mLists.get(column).recyclerView.getAdapter();
            adapter.removeItem(row);
            adapter.addItem(row, item);
            if (scrollToItem) {
                scrollToItem(column, row, false);
            }
        }
    }

    public void scrollToItem(int column, int row, boolean animate) {
        if (!isDragging() && mLists.size() > column && mLists.get(column).recyclerView.getAdapter().getItemCount() > row) {
            mScroller.forceFinished(true);
            scrollToColumn(column, animate);
            if (animate) {
                mLists.get(column).recyclerView.smoothScrollToPosition(row);
            } else {
                mLists.get(column).recyclerView.scrollToPosition(row);
            }
        }
    }

    public void scrollToColumn(int column, boolean animate) {
        if (dragPager.getPagerCount() <= column) {
            return;
        }
        notifyData(column);
        View parent = cachePage.get(getKey(dragPager.getItem(column))).root;
        int left = getPageLeftX(column);
        int newX = left - (getMeasuredWidth() - mColumnWidth) / 2;
        int mrootWidth = getContentWidth();
        int maxScroll = mrootWidth - getMeasuredWidth();
        newX = newX < 0 ? 0 : newX;
        newX = newX > maxScroll ? maxScroll : newX;
        if (getScrollX() != newX) {
            mScroller.forceFinished(true);
            if (animate) {
                mScroller.startScroll(getScrollX(), getScrollY(), newX - getScrollX(), 0, SCROLL_ANIMATION_DURATION);
                ViewCompat.postInvalidateOnAnimation(this);
            } else {
                scrollTo(newX, getScrollY());
            }
        }

    }

    private int getPageLeftX(int column) {
        return (column == 0) ? 0 : column * mColumnWidth + page_margin;
    }

    private int getContentWidth() {
        return dragPager.getPagerCount() * mColumnWidth + page_margin * 2;
    }

    public void clearBoard() {
        int count = mLists.size();
        for (int i = count - 1; i >= 0; i--) {
            mColumnLayout.removeViewAt(i);
            mLists.remove(i);
        }
    }

    public void removeColumn(int column) {
        if (column >= 0 && mLists.size() > column) {
            mColumnLayout.removeViewAt(column);
            int key = getKey(dragPager.getItem(column));
            PageHolder holder = cachePage.get(key);
            cachePage.remove(key);
            mLists.remove(holder);
        }
    }

    public boolean isDragEnabled() {
        return mDragEnabled;
    }

    public void setDragEnabled(boolean enabled) {
        mDragEnabled = enabled;
        if (mLists.size() > 0) {
            for (PageHolder list : mLists) {
                list.recyclerView.setDragEnabled(mDragEnabled);
            }
        }
    }

    /**
     * @param width the width of columns in both portrait and landscape. This must be called before {@link #} is
     *              called for the width to take effect.
     */
    public void setColumnWidth(int width) {
        mColumnWidth = width;
    }

    /**
     * @param snapToColumn true if scrolling should snap to columns. Only applies to portrait mode.
     */
    public void setSnapToColumnsWhenScrolling(boolean snapToColumn) {
        mSnapToColumnWhenScrolling = snapToColumn;
    }

    /**
     * @param snapToColumn true if dragging should snap to columns when dragging towards the edge. Only applies to portrait mode.
     */
    public void setSnapToColumnWhenDragging(boolean snapToColumn) {
        mSnapToColumnWhenDragging = snapToColumn;
        mAutoScroller.setAutoScrollMode(snapToColumnWhenDragging() ? AutoScroller.AutoScrollMode.COLUMN : AutoScroller.AutoScrollMode
                .POSITION);
    }

    /**
     * @param snapToColumnInLandscape true if dragging should snap to columns when dragging towards the edge also in landscape mode.
     */
    public void setSnapToColumnInLandscape(boolean snapToColumnInLandscape) {
        mSnapToColumnInLandscape = snapToColumnInLandscape;
        mAutoScroller.setAutoScrollMode(snapToColumnWhenDragging() ? AutoScroller.AutoScrollMode.COLUMN : AutoScroller.AutoScrollMode
                .POSITION);
    }

    /**
     * @param snapToTouch true if the drag item should snap to touch position when a drag is started.
     */
    public void setSnapDragItemToTouch(boolean snapToTouch) {
        mDragItem.setSnapToTouch(snapToTouch);
    }

    public void setBoardListener(BoardListener listener) {
        mBoardListener = listener;
    }

    public void setCustomDragItem(DragItem dragItem) {
        DragItem newDragItem;
        if (dragItem != null) {
            newDragItem = dragItem;
        } else {
            newDragItem = new DragItem(getContext());
        }

        newDragItem.setSnapToTouch(mDragItem.isSnapToTouch());
        mDragItem = newDragItem;
        mRootLayout.removeViewAt(1);
        mRootLayout.addView(mDragItem.getDragItemView());
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private float mStartScrollX;

        @Override
        public boolean onDown(MotionEvent e) {
            mStartScrollX = getScrollX();
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Calc new column to scroll to
            int currentColumn = getCurrentColumn(e2.getX() + getScrollX());
            int newColumn;
            if (Math.abs(velocityY) >= Math.abs(velocityX)) {
                newColumn = currentColumn;
            } else {
                if (velocityX < 0) {
                    newColumn = getScrollX() >= mStartScrollX ? currentColumn + 1 : currentColumn;
                } else {
                    newColumn = getScrollX() <= mStartScrollX ? currentColumn - 1 : currentColumn;
                }
            }

            if (newColumn < 0 || newColumn > mLists.size() - 1) {
                newColumn = newColumn < 0 ? 0 : mLists.size() - 1;
            }
            // Calc new scrollX position
            scrollToColumn(newColumn, true);
            return true;
        }
    }


    //    update
    protected int currectPager = 0;

    public void notifyItem(int index) {
        IPageModel object = dragPager.getItem(index);
        cachePage.remove(getKey(object));
        mLists.remove(object);
        mColumnLayout.removeViewAt(getRealIndex(index));
        addPage(index);
    }


    public void notifyItemInsert(final int index) {
        scrollToColumn(index, true);
    }


    public void notifyData(int index) {
        this.currectPager = index;
        addPage(currectPager);
        int pageUp = currectPager - 1;
        if (pageUp >= 0) {
            addPage(pageUp);
        }

        int pageDown = currectPager + 1;
        if (pageDown < dragPager.getPagerCount()) {
            addPage(pageDown);
        }

        pageDown = currectPager + 2;
        if (pageDown < dragPager.getPagerCount()) {
            addPage(pageDown);
        }
    }


    public PageHolder addPage(int index) {
        IPageModel object = dragPager.getItem(index);
        PageHolder holder = cachePage.get(getKey(object));
        if (holder != null) {
            return cachePage.get(getKey(object));
        }
        PageHolder pageHolder = dragPager.addColumnList(index);
        pageHolder.root.setLayoutParams(new FrameLayout.LayoutParams(mColumnWidth, FrameLayout.LayoutParams.MATCH_PARENT));
        mColumnLayout.addView(pageHolder.root, getRealIndex(index));
        mLists.add(pageHolder);
        cachePage.put(getKey(object), pageHolder);
        return pageHolder;
    }

    private int getKey(IPageModel object) {
        return object.getKey();
    }

    private int getRealIndex(int index) {
        return index;
    }

    public View getItemParentView(PageHolder holder) {
        return holder.root;
    }
}
