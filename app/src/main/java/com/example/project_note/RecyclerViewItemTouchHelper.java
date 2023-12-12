package com.example.project_note;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


/*
* gọi ItemTouchHelper.SimpleCallback  lắng nghe sự kiện "di chuyển" và "vuốt"
* */
public class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ItemTouchHelperListener mlistener;


    //dragDirs Hướng kéo
    // swipe Dirs Hướng vuốt
    public RecyclerViewItemTouchHelper(int dragDirs, int swipeDirs, ItemTouchHelperListener listener) {

        super(dragDirs, swipeDirs);
        this.mlistener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    // Thông báo mListener khi swiped để thực hiện khi swip
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        if(mlistener != null){
            mlistener.onSwiped(viewHolder);
        }
    }

    //Được gọi khi một item được chọn.
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null){
            View foreGroundView = ((NoteAdapter.NoteViewHodel) viewHolder).mLayoutForceGround;
            getDefaultUIUtil().onSelected(foreGroundView);

        }
    }

    //Được gọi khi một item được vẽ trên cùng của các item khác.
    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGroundView = ((NoteAdapter.NoteViewHodel) viewHolder).mLayoutForceGround;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
    }

    //Được gọi khi một item được vẽ.
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGroundView = ((NoteAdapter.NoteViewHodel) viewHolder).mLayoutForceGround;
        getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
    }

    //Được gọi khi việc vẽ item kết thúc
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foreGroundView = ((NoteAdapter.NoteViewHodel) viewHolder).mLayoutForceGround;
        getDefaultUIUtil().clearView(foreGroundView);
    }
}
