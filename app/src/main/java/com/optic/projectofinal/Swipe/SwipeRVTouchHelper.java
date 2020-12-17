package com.optic.projectofinal.Swipe;

import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.optic.projectofinal.R;


public class SwipeRVTouchHelper extends ItemTouchHelper.SimpleCallback {

    private SwipeRVTouchHelperListener listener;

    public interface SwipeRVTouchHelperListener {
        void onSwiped(int direction, int position);
    }

    public SwipeRVTouchHelper(SwipeRVTouchHelperListener listener, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    // Return false since we disable dragging
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        listener.onSwiped(i, viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foreground = ((SwipeRVAdapter.SwipeRVViewHolder) viewHolder).getForegroundContainer();
            getDefaultUIUtil().onSelected(foreground);
        }
    }

    // Draw background differently, depending if it is a LEFT or RIGHT swipe
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // User swipes the item
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            RelativeLayout backgroundLayout = viewHolder.itemView.findViewById(R.id.swipe_background_container);
            ImageView rightIcon = backgroundLayout.findViewById(R.id.swipe_item_icon_right);
            ImageView leftIcon = backgroundLayout.findViewById(R.id.swipe_item_icon_left);

            // User swipes left
            float translationX=dX;
            if (dX < 0) {
                leftIcon.setImageResource(android.R.color.transparent);
                rightIcon.setImageResource(R.drawable.ic_delete_24dp);
                backgroundLayout.setBackgroundResource(R.color.swipe_remove);

                    translationX = Math.max(dX, (-1) * viewHolder.itemView.getWidth() / 2);

            } // User swipes right
            else if (dX > 0) {
                rightIcon.setImageResource(android.R.color.transparent);
                leftIcon.setImageResource(R.drawable.ic_create);
                backgroundLayout.setBackgroundResource(R.color.swipe_edit);

                translationX =(Math.min(dX, viewHolder.itemView.getWidth() / 2));


            }



            View foreground = ((SwipeRVAdapter.SwipeRVViewHolder) viewHolder).getForegroundContainer();
            getDefaultUIUtil().onDraw(c, recyclerView, foreground, translationX, dY, actionState, isCurrentlyActive);


        }


    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.5f;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foreground = ((SwipeRVAdapter.SwipeRVViewHolder) viewHolder).getForegroundContainer();
        getDefaultUIUtil().clearView(foreground);
    }

    // Enable swiping
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    // Disable dragging
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }
}
