package com.example.project_note.RecycleView

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.project_note.RecycleView.NoteAdapter.NoteViewHodel

/*
* gọi ItemTouchHelper.SimpleCallback  lắng nghe sự kiện "di chuyển" và "vuốt"
* */
class RecyclerViewItemTouchHelper(dragDirs: Int, swipeDirs: Int, private val mlistener: ItemTouchHelperListener?)
    : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    //dragDirs Hướng kéo
// swipe Dirs Hướng vuốt

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    // Thông báo mListener khi swiped để thực hiện khi swip
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mlistener?.onSwiped(viewHolder)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foreGroundView: View = (viewHolder as NoteViewHodel).binding.layoutForceground
            getDefaultUIUtil().onSelected(foreGroundView)
        }
    }


    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foreGroundView: View = (viewHolder as NoteViewHodel).binding.layoutForceground

        getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foreGroundView: View = (viewHolder as NoteViewHodel).binding.layoutForceground

        getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foreGroundView: View = (viewHolder as NoteViewHodel).binding.layoutForceground

        getDefaultUIUtil().clearView(foreGroundView)
    }
}