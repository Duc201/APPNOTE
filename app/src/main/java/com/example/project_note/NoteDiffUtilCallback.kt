package com.example.project_note

import androidx.recyclerview.widget.DiffUtil
import com.example.project_note.DataBase.Note

class NoteDiffUtilCallback : DiffUtil.ItemCallback<Note> () {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.idNote == newItem.idNote
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {

        return oldItem.equals(newItem)

    }

}