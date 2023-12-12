package com.example.project_note

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.project_note.NoteAdapter.NoteViewHodel

/*
* gọi ItemTouchHelper.SimpleCallback  lắng nghe sự kiện "di chuyển" và "vuốt"
* */
class RecyclerViewItemTouchHelper    //dragDirs Hướng kéo
// swipe Dirs Hướng vuốt
    (dragDirs: Int, swipeDirs: Int, private val mlistener: ItemTouchHelperListener?) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    // Thông báo mListener khi swiped để thực hiện khi swip
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mlistener?.onSwiped(viewHolder)
    }

    //Được gọi khi một item được chọn.
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foreGroundView: View = (viewHolder as NoteViewHodel).mLayoutForceGround
            getDefaultUIUtil().onSelected(foreGroundView)
        }
    }

    //Được gọi khi một item được vẽ trên cùng của các item khác.
    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foreGroundView: View = (viewHolder as NoteViewHodel).mLayoutForceGround
        getDefaultUIUtil().onDrawOver(
            c,
            recyclerView,
            foreGroundView,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    //Được gọi khi một item được vẽ.
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foreGroundView: View = (viewHolder as NoteViewHodel).mLayoutForceGround
        getDefaultUIUtil().onDraw(
            c,
            recyclerView,
            foreGroundView,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    //Được gọi khi việc vẽ item kết thúc
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foreGroundView: View = (viewHolder as NoteViewHodel).mLayoutForceGround
        getDefaultUIUtil().clearView(foreGroundView)
    }
}