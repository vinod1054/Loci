package loci.vk.com.myapplication.touchhelper;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import loci.vk.com.myapplication.R;

/**
 * Created by vinod on 1/3/16.
 */
public class RecycletViewItemTouchHelper  extends ItemTouchHelper.Callback {
    final String TAG="ItemTouchHelper";
    TouchListenerPresenter touchListenerPresenter;

    public RecycletViewItemTouchHelper(TouchListenerPresenter touchListenerPresenter){
        this.touchListenerPresenter=touchListenerPresenter;
    }



    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.i(TAG,"onMove");
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.i(TAG,"onSwiped");
        if(direction==ItemTouchHelper.RIGHT)
        touchListenerPresenter.swiped(viewHolder.getAdapterPosition());

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        viewHolder.itemView.findViewById(R.id.layout_item_container).setX(dX);
        viewHolder.itemView.findViewById(R.id.hidden_view).setVisibility(View.VISIBLE);
        if (dX==0)
            viewHolder.itemView.findViewById(R.id.hidden_view).setAlpha(0);
        else
            viewHolder.itemView.findViewById(R.id.hidden_view).setAlpha(1);
        //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Log.i(TAG, "onChildDraw");
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.i(TAG, "clearView1");
        super.clearView(recyclerView, viewHolder);
        Log.i(TAG, "clearView");
        viewHolder.itemView.findViewById(R.id.layout_item_container).setX(0);
    }


}
