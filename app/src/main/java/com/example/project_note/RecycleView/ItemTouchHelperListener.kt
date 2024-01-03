package com.example.project_note.RecycleView

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {
    fun onSwiped(viewHolder: RecyclerView.ViewHolder?)

}