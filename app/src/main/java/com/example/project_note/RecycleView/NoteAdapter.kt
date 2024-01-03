package com.example.project_note.RecycleView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_note.DataBase.Note
import com.example.project_note.NoteDiffUtilCallback
import com.example.project_note.R
import com.example.project_note.RecycleView.NoteAdapter.NoteViewHodel
import com.example.project_note.databinding.ItemNotesBinding
import java.util.Arrays
import java.util.Locale

class NoteAdapter( private val mIClickListener: IClickListener)
    : ListAdapter<Note,NoteAdapter.NoteViewHodel>(DIFF_CALLBACK) {

    private val colorList = Arrays.asList(
        R.color.DeepPink,
        R.color.LightPink,
        R.color.LightGreen,
        R.color.Yellow,
        R.color.LightBlue,
        R.color.MediumPurple
    )

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.idNote == newItem.idNote
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }

    inner class NoteViewHodel(val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {}



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHodel {
        val binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHodel(binding)
    }


    override fun onBindViewHolder(holder: NoteViewHodel, position: Int) {
        // Creat ColorItem
        val colorIndex = position % colorList.size
        val colorResId = colorList[colorIndex]
        val selectedColor = ContextCompat.getColor(holder.itemView.context, colorResId)

        val note = getItem(position)
        holder.apply {
            binding.edtTitleItem.setText(note.title)
            binding.layoutForceground.setBackgroundColor(selectedColor)
            binding.layoutForceground.setOnClickListener { mIClickListener.onClickItem(note) }
        }

    }

    fun getNoteAtPosition(position: Int): Note {
        return getItem(position)
    }
}

interface IClickListener {
    fun onClickItem(note: Note)
}
