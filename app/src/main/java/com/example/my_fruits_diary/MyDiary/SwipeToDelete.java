package com.example.my_fruits_diary.MyDiary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.R;

abstract class SwipeToDelete extends ItemTouchHelper.SimpleCallback {
        private Drawable icon;
        private ColorDrawable background;
        private Context mContext;

    public SwipeToDelete( Context context) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        background = new ColorDrawable(Color.RED);
        mContext = context;
        icon = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_delete_24px);
    }


//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        int position = viewHolder.getAdapterPosition();
//        mAdapter.deleteItem(position);
//    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;

        int itemHeight = itemView.getBottom() - itemView.getTop();
        int deleteMargin = (itemHeight - icon.getIntrinsicHeight())/2;
        int intrinsicHeight = icon.getIntrinsicWidth();
        int deleteIconLeft = itemView.getRight() - deleteMargin - icon.getIntrinsicWidth();
        int deleteIconRight = itemView.getRight() - deleteMargin;
        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;
        icon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);

        background.draw(c);
        icon.draw(c);

    }
}
