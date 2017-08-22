package com.woxthebox.draglistview;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * 作者：husongzhen on 17/7/12 14:41
 * 邮箱：husongzhen@musikid.com
 */

public abstract class DragPager {
    public abstract int getPagerCount();

    public abstract Object getItem(int index);


    protected BoardView boardView;


    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }


    public void notifyData() {
        boardView.notifyData(0);
    }

    public PageHolder addColumnList(int index) {
        PageHolder pageHolder = onCreatePager(boardView, null, index);
        return pageHolder;
    }

    public void initRecycler(final BoardView boardView, DragItemAdapter adapter, final DragItemRecyclerView recyclerView) {
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setMotionEventSplittingEnabled(false);
        recyclerView.setDragItem(boardView.mDragItem);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(boardView.getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDragItemListener(new DragItemRecyclerView.DragItemListener() {
            @Override
            public void onDragStarted(int itemPosition, float x, float y) {
                boardView.mDragStartColumn = boardView.getColumnOfList(recyclerView);
                boardView.mDragStartRow = itemPosition;
                boardView.mCurrentRecyclerView = recyclerView;
                boardView.mDragItem.setOffset(((View) boardView.mCurrentRecyclerView.getParent()).getX(), boardView.mCurrentRecyclerView.getY());
                if (boardView.mBoardListener != null) {
                    boardView.mBoardListener.onItemDragStarted(boardView.mDragStartColumn, boardView.mDragStartRow);
                }
                boardView.invalidate();
            }

            @Override
            public void onDragging(int itemPosition, float x, float y) {
                int column = boardView.getColumnOfList(recyclerView);
                int row = itemPosition;
                boolean positionChanged = column != boardView.mLastDragColumn || row != boardView.mLastDragRow;
                if (boardView.mBoardListener != null && positionChanged) {
                    boardView.mLastDragColumn = column;
                    boardView.mLastDragRow = row;
                    boardView.mBoardListener.onItemChangedPosition(boardView.mDragStartColumn, boardView.mDragStartRow, column, row);
                }
            }

            @Override
            public void onDragEnded(int newItemPosition) {
                boardView.mLastDragColumn = NO_POSITION;
                boardView.mLastDragRow = NO_POSITION;
                if (boardView.mBoardListener != null) {
                    boardView.mBoardListener.onItemDragEnded(boardView.mDragStartColumn, boardView.mDragStartRow, boardView.getColumnOfList(recyclerView), newItemPosition);
                }
            }
        });
        recyclerView.setDragEnabled(boardView.mDragEnabled);
        recyclerView.setAdapter(adapter);
        adapter.setDragStartedListener(new DragItemAdapter.DragStartCallback() {
            @Override
            public boolean startDrag(View itemView, long itemId) {
                return recyclerView.startDrag(itemView, itemId, boardView.getListTouchX(recyclerView), boardView.getListTouchY(recyclerView));
            }

            @Override
            public boolean isDragging() {
                return recyclerView.isDragging();
            }
        });
    }


    protected abstract PageHolder onCreatePager(BoardView boardView, ViewGroup pager, int index);
//    {
//        PageHolder pageHolder = new PageHolder();
//        pageHolder.root = (ViewGroup) LayoutInflater.from(boardView.getContext()).inflate(R.layout.drag_item_recycler_view, boardView, false);
//        ((TextView) pageHolder.root.findViewById(R.id.text)).setText("Column ");
//        ((TextView) pageHolder.root.findViewById(R.id.item_count)).setText("");
//        return pageHolder;
//    }
}
