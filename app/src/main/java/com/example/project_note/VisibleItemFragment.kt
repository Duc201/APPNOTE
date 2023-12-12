package com.example.project_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.project_note.DataBase.Note
import com.example.project_note.R

class VisibleItemFragment : Fragment() {
    private var id = 0
    private lateinit var mTxtTitle: TextView
    private lateinit var mTxtDetail: TextView
    private lateinit var mImageButtonBack: ImageButton
    private lateinit var mImageButtonEdit: ImageButton
    var note2: Note? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_visible_item, container, false)
         mTxtDetail  = view.findViewById(R.id.txt_detail_visible)
        mTxtTitle = view.findViewById(R.id.txt_title_visible)
        mImageButtonBack = view.findViewById(R.id.imgbut_back)
        mImageButtonEdit = view.findViewById(R.id.imgbut_edit)
        mImageButtonBack.setOnClickListener(View.OnClickListener {
            if (parentFragmentManager != null) {
                parentFragmentManager.popBackStack()
            }
        })
        mImageButtonEdit.setOnClickListener(View.OnClickListener {
            val note = Note(mTxtTitle.getText().toString(), mTxtDetail.getText().toString())
            openFragmentEdit(note, id)
        })
        setFragmentResultListeners()
        return view
    }

    // Hàm lắng nghe sự kiện khi Fragment khác chuyển tới Fragment này
    private fun setFragmentResultListeners() {
        val keys = listOf("dataFromEdit", "dataFromHome", "dataFromSearch")

        for (key in keys) {
            parentFragmentManager.setFragmentResultListener(key, this) { _, result ->
                // as này là ép kiểu
                val note = result.getSerializable("note") as? Note
                // Ví dụ nếu note != null sẽ thực hiện let,....it nó thay thế cho Note . Dùng Note cũng đc
                note?.let {
                    mTxtDetail.text = it.detail
                    mTxtTitle.text = it.title
                    id = it.id
                }
            }
        }
    }

    // Open Fragment
    private fun openFragmentEdit(note: Note, id: Int) {
        val editNoteFragment = EditNoteFragment()
        val bundle = Bundle().apply {
            putSerializable("note1", note)
            putInt("KeyInt", id)
        }
        parentFragmentManager.setFragmentResult("dataFromVisible", bundle)

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frag_layout, editNoteFragment)
            addToBackStack(null)
            commit()
        }
    }
}