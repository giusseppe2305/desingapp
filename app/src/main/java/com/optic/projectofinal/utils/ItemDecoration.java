package com.optic.projectofinal.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends DividerItemDecoration {
    private int mDividerHeight;
    private int tOrientation;
    private Context context;
    public ItemDecoration(Context context, int orientation, int mDividerHeight) {
        super(context, orientation);
        this.mDividerHeight = mDividerHeight;
        this.tOrientation = orientation;
        this.context = context;
    }

    public ItemDecoration(Context context, int tOrientation) {
        super(context,tOrientation);
        mDividerHeight=0;
        this.context = context;
        this.tOrientation = tOrientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDividerHeight > 0) {
            if (tOrientation == 1) {
                outRect.set(0, 0, 0, mDividerHeight);
            } else {
                outRect.set(0, 0, mDividerHeight, 0);
            }
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
