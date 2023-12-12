package com.example.project_note

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project_note.DataBase.Note
import com.example.project_note.NoteAdapter.NoteViewHodel
import java.util.Arrays
import java.util.Locale

class NoteAdapter(private var mlistNote: MutableList<Note> ) :
    RecyclerView.Adapter<NoteViewHodel>(), Filterable {

    var onItemClick:((Note)->Unit)? = null

    private lateinit var mListNoteOld: MutableList<Note>
    private  var mContext: Context? = null
    init {
        mListNoteOld = mlistNote
//        mIClickListener = iClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHodel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return NoteViewHodel(view)
    }

    override fun onBindViewHolder(holder: NoteViewHodel, position: Int) {
        val note:Note = mlistNote.get(position)

        holder.mTxtTitle.setText(note.title)

        // Creat ColorItem
        val colorIndex = position % colorList.size
        val colorResId = colorList[colorIndex]
        val selectedColor = ContextCompat.getColor(holder.itemView.context, colorResId)
        holder.mLayoutForceGround.setBackgroundColor(selectedColor)
            // hàm bắt sử kiện khi bấm 1 ItemView
//        holder.mLayoutForceGround.setOnClickListener { mIClickListener.onClickItem(note) }
//
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(note)
        }

    }

    private val colorList = Arrays.asList(
        R.color.DeepPink,
        R.color.LightPink,
        R.color.LightGreen,
        R.color.Yellow,
        R.color.LightBlue,
        R.color.MediumPurple
    )



    override fun getItemCount(): Int {
        return if (mlistNote != null) {
           mlistNote.size
        } else 0
    }

    inner class NoteViewHodel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTxtTitle: TextView
        val mCardView: CardView

        var mLayoutForceGround: LinearLayout

        init {
             mTxtTitle = itemView.findViewById(R.id.edt_title_item)
             mCardView = itemView.findViewById(R.id.cardview_note)
            mLayoutForceGround = itemView.findViewById(R.id.layout_forceground)
        }
    }

    fun removeItem(index: Int) {
        mlistNote.removeAt(index)
        notifyItemRemoved(index)
    }

    fun undoItem(note: Note, index: Int) {
        mlistNote.add(index, note)
        notifyItemInserted(index)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            // Thực hiện truy vấn tìm kiếm
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val strSearch = constraint.toString()
                // Nếu chưa nhập gì
                mlistNote = if (strSearch.isEmpty()) {
                    mListNoteOld
                }
                else {
                    val list: MutableList<Note> = ArrayList()
                    for (note in mListNoteOld) {
                        if (note.title.lowercase(Locale.getDefault())
                                .contains(strSearch.lowercase(Locale.getDefault()))
                        ) {
                            list.add(note)
                        }
                    }
                     list
                }
                // Tạo một filterResults là lưu kết quả
                val filterResults = FilterResults()
                filterResults.values = mlistNote
                return filterResults
            }

            // Cập nhật giao diện người dùng với kết quả đã lọc
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                mlistNote = results.values as MutableList<Note>
                // Thông báo adapter đã thay đổi
                notifyDataSetChanged()
            }
        }
    }
}