package com.example.project_note

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast


import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.example.project_note.DataBase.Note
import com.example.project_note.DataBase.NoteDatabase
import com.example.project_note.R

class EditNoteFragment : Fragment() {
    private lateinit var mImgButtonBack: ImageButton
    private lateinit var mImgButtonSave: ImageButton
    private lateinit var mImgButtonVisible: ImageButton
    private lateinit var mEdtTitle: EditText
    private lateinit var mEdtDetail: EditText
    private lateinit var note1: Note
    var id = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_note, container, false)
        initViews(view)
        setupButtons()
        setupListeners()
        setupOnBackPressed()
        return view
    }

    private fun setupOnBackPressed() {
        TODO("Not yet implemented")
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val bundle = Bundle().apply {
                putSerializable("note5", note1)
            }
            parentFragmentManager.setFragmentResult("dataFromEdit", bundle)
            if (parentFragmentManager.backStackEntryCount > 0) {
                openDialogReturn(Gravity.CENTER)
            } else {
                requireActivity().onBackPressed()
            }
        }

    }

    private fun setupListeners() {
        TODO("Not yet implemented")
        parentFragmentManager.setFragmentResultListener("dataFromVisible", this) { _, result ->
            note1 = result.getSerializable("note1") as Note
            mEdtTitle.setText(note1?.title)
            mEdtDetail.setText(note1?.detail)
            id = result.getInt("KeyInt")
        }
    }

    private fun setupButtons() {
        mImgButtonSave?.setOnClickListener { openDialogSave(Gravity.CENTER) }
        mImgButtonBack?.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("note5", note1)
            }
            parentFragmentManager.setFragmentResult("dataFromEdit", bundle)
            openDialogReturn(Gravity.CENTER)
        }
    }

    private fun initViews(view: View) {
        mImgButtonBack = view.findViewById(R.id.img_back)
        mImgButtonSave = view.findViewById(R.id.imgbut_save)
        mImgButtonVisible = view.findViewById(R.id.imgbut_visible)
        mEdtTitle = view.findViewById(R.id.edt_title)
        mEdtDetail = view.findViewById(R.id.edt_detail)
    }
    private fun openDialogReturn(gravity: Int) {
        val dialog = createDialog(R.layout.dialog_discardsave, gravity)
        val btnKeep = dialog.findViewById<Button>(R.id.btn_keep)
        val btnDiscard = dialog.findViewById<Button>(R.id.btn_discard)

        btnKeep.setOnClickListener {
            parentFragmentManager.popBackStack()
            dialog.dismiss()
        }
        btnDiscard.setOnClickListener { dialog.dismiss() }

        dialog.show()

    }
    private fun createDialog(layoutResId: Int, gravity: Int): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutResId)

        val window = dialog.window ?: throw IllegalStateException("Dialog has no window")

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.attributes.gravity = gravity

        dialog.setCancelable(Gravity.CENTER == gravity)
        return dialog
    }

    private fun saveNotetoDataBase() {
        val note = Note(
            mEdtTitle.text.toString() ?: "",
            mEdtDetail.text.toString() ?: ""
        )

        if (id == -1) {
            NoteDatabase.getInstance(requireContext()).noteDAO()?.insertNote(note)
            note.id = NoteDatabase.getInstance(requireContext()).noteDAO()
                .getID(mEdtTitle.text.toString(), mEdtDetail.text.toString())
            hideSoftKeyboard()
            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
            returnVisibleItemFragment(note)
        }
        else {
            note.id = id
            NoteDatabase.getInstance(requireContext())?.noteDAO()?.updateNote(note)
            hideSoftKeyboard()
            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            returnVisibleItemFragment(note)
        }
    }

    private fun openDialogSave(gravity: Int) {
        val dialog = createDialog(R.layout.dialog_save, gravity)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnDiscard = dialog.findViewById<Button>(R.id.btn_discard)

        btnSave.setOnClickListener {
            saveNotetoDataBase()
            dialog.dismiss()
        }
        btnDiscard.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun returnVisibleItemFragment(note: Note) {
//        val bundle = Bundle()
//        bundle.putSerializable("note5", note)
        val bundle = Bundle().apply {
            putSerializable("note5", note)
        }
        parentFragmentManager.setFragmentResult("dataFromEdit", bundle)
        if (parentFragmentManager != null) {
            parentFragmentManager.popBackStack()
        }
    }

    private fun hideSoftKeyboard() {
        if (requireActivity() != null) {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = requireActivity().currentFocus
//            if (view != null) {
//                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//            }
            view?.let {
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }
}