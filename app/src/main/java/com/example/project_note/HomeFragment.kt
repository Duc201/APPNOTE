package com.example.project_note

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_note.DataBase.Note
import com.example.project_note.DataBase.NoteDatabase
import com.example.project_note.ItemTouchHelperListener
import com.example.project_note.NoteAdapter
import com.example.project_note.NoteAdapter.NoteViewHodel
import com.example.project_note.R
import com.example.project_note.RecyclerViewItemTouchHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), ItemTouchHelperListener {
    private lateinit var mLayoutNotData: LinearLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFloatingActionButton: FloatingActionButton
    private lateinit var mlistNote: MutableList<Note>
    private lateinit var rootView: RelativeLayout
    private lateinit var mImageButtonIntroduct: ImageButton
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var searchView: SearchView
    private lateinit var mBtnSearch: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initViews(view)
        checkInitData()

        // action open Frame Edit
        mFloatingActionButton.setOnClickListener(View.OnClickListener { openFragmentEdit() })
        // action open Introduct
        mImageButtonIntroduct.setOnClickListener(View.OnClickListener { //                showCustomDialog();
            openDialogIntroduce(Gravity.CENTER)
        })

        // swip delete item
        val simpleCallback: ItemTouchHelper.SimpleCallback =
            RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerView)

        // setSearchView
        setSearchView()


//        requireActivity().getOnBackPressedDispatcher()
//            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    if (searchView != null && !searchView!!.isIconified) {
//                        // Nếu SearchView đang mở, thu nhỏ nó thay vì thoát ứng dụng
//                        searchView!!.isIconified = true
//                    } else {
//                        // Nếu không, thực hiện hành động mặc định khi nhấn nút Back
//                        requireActivity().finish()
//                    }
//                }
//            })
        return view
    }
    private fun initViews(view: View) {
        mLayoutNotData = view.findViewById(R.id.linear_non)
        mRecyclerView = view.findViewById(R.id.ryc_notes)
        mFloatingActionButton = view.findViewById(R.id.btn_floating)
        mImageButtonIntroduct = toolbar.findViewById(R.id.imgbut_introduce)
        searchView = toolbar.findViewById(R.id.imgbut_search)
        rootView = view.findViewById(R.id.root_view)
        mBtnSearch = view.findViewById(R.id.btnSearch)
    }
    private fun setupRecyclerView() {
        mlistNote = NoteDatabase.getInstance(requireContext()).noteDAO().getListNote() as MutableList<Note>
        noteAdapter = NoteAdapter(mlistNote)
        noteAdapter.onItemClick = {
                note -> openFragmentVisible(note)
        }

        val linearLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.adapter = noteAdapter
    }

    private fun setSearchView() {
//        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Khi người dùng nhấn nút tìm kiếm trên bàn phím
                // Áp dụng bộ lọc dữ liệu vào Adapter để lọc dữ liệu dựa trên truy vấn tìm kiếm
                noteAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Khi giá trị của ô tìm kiếm thay đổi
                // Áp dụng bộ lọc dữ liệu vào Adapter để ngay lập tức lọc dữ liệu dựa trên văn bản mới nhập
                noteAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun checkInitData() {
        if (isexistData()) {
            mLayoutNotData.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
        } else {
            mLayoutNotData.visibility = View.VISIBLE
            mRecyclerView.visibility = View.GONE
        }
    }

    private fun openDialogIntroduce(gravity: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_introduce)
        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAtrributes = window.attributes
        windowAtrributes.gravity = gravity
        window.attributes = windowAtrributes
        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true)
        } else {
            dialog.setCancelable(false)
        }
        dialog.show()
    }

    private fun openFragmentVisible(note: Note) {
        val bundle = Bundle()
        bundle.putSerializable("note", note)
        parentFragmentManager.setFragmentResult("dataFromHome", bundle)
        val visibleItemFragment = VisibleItemFragment()
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frag_layout, visibleItemFragment)
        fragmentTransaction.addToBackStack(null) // Nếu bạn muốn thêm vào back stack
        fragmentTransaction.commit()
    }

    private fun openFragmentEdit() {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frag_layout, EditNoteFragment())
        fragmentTransaction.addToBackStack("FragmentHome")
        fragmentTransaction.commit()
    }

    fun isexistData(): Boolean {
        val mList = NoteDatabase.getInstance(requireContext()).noteDAO().getListNote()
        return mList != null && !mList.isEmpty()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is NoteViewHodel) {
            // Lấy title của một Note
            val nameNoteDelete = mlistNote!![viewHolder.getAdapterPosition()].title
            val noteDelete = mlistNote!![viewHolder.getAdapterPosition()]
            val indexDelte = viewHolder.getAdapterPosition()

            //remove item
            noteAdapter!!.removeItem(indexDelte)
            // Note use RXAndroid
            NoteDatabase.getInstance(requireContext()).noteDAO().deleteNote(noteDelete)
            checkInitData()
            val snackbar =
                Snackbar.make(rootView!!, "$nameNoteDelete remove!", Snackbar.LENGTH_LONG)
            snackbar.setAction("UNDO") {
                noteAdapter!!.undoItem(noteDelete, indexDelte)
                // Note use RXAndroid
                NoteDatabase.getInstance(requireContext()).noteDAO().insertNote(noteDelete)
                mLayoutNotData!!.visibility = View.GONE
                mRecyclerView!!.visibility = View.VISIBLE
            }
            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.show()
        }
    }
}