package com.example.project_note.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.project_note.Activity.LoginActivity
import com.example.project_note.DataBase.Note
import com.example.project_note.R
import com.example.project_note.RecycleView.IClickListener
import com.example.project_note.RecycleView.ItemTouchHelperListener
import com.example.project_note.RecycleView.NoteAdapter
import com.example.project_note.RecycleView.NoteAdapter.NoteViewHodel
import com.example.project_note.RecycleView.RecyclerViewItemTouchHelper
import com.example.project_note.ViewModal.AuthViewModel
import com.example.project_note.ViewModal.ViewModalImage
import com.example.project_note.ViewModal.ViewModalNote
import com.example.project_note.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class HomeFragment : Fragment(), ItemTouchHelperListener {

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var mViewModalNote: ViewModalNote
    lateinit var mViewModalImage: ViewModalImage
    private lateinit var mViewModelAuth : AuthViewModel


    private var binding_Home: FragmentHomeBinding?=null
    private val binding get() = binding_Home!!
    private lateinit var sharedpreferences: SharedPreferences

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_Home = FragmentHomeBinding.inflate(inflater,container,false)
        mViewModalNote = ViewModelProvider(requireActivity()).get(ViewModalNote::class.java)
        mViewModalImage = ViewModelProvider(requireActivity()).get(ViewModalImage::class.java)
        mViewModelAuth = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        sharedpreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        onClickNavigationDrawer()

        setupRecyclerView()
        mViewModalNote.mListNote.observe(viewLifecycleOwner){
            checkInitData(it)
            Log.d("submitList", ""+ it.toString())
            setSearchView()
            noteAdapter.submitList(it?.toMutableList())
        }
        mViewModelAuth.getInformationUser(requireContext())
        mViewModelAuth.userResponse.observe(viewLifecycleOwner){
            val headerView = binding_Home?.navigationView?.getHeaderView(0)
            val textViewUsername = headerView?.findViewById<TextView>(R.id.txt_name)
            val imageView = headerView?.findViewById<ImageView>(R.id.img_user)
            val textViewEmail = headerView?.findViewById<TextView>(R.id.txt_email)
           textViewUsername?.setText(it.name)
            textViewEmail?.setText(it.email)
            Picasso.with(context)
                .load(it.image)
                .placeholder(R.drawable.icon_person)
                .error(R.drawable.icon_person)
                .into(imageView);
        }

        // swip delete item
        val simpleCallback: ItemTouchHelper.SimpleCallback = RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(simpleCallback).attachToRecyclerView( binding_Home?.rycNotes)

        openActionButton()
        binding_Home!!.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_changepass -> {
                    openFragmentChangePassword()
                    binding.dlLayout.closeDrawer(GravityCompat.START)

                    true
                }
                R.id.nav_logout -> {
                    val editor = sharedpreferences.edit()
                    editor.clear()
                    editor.apply()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireActivity(),LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    true
                }
                R.id.nav_inforAccount ->{
                    openFragmentInformationAccount()
                    true
                }
                else -> {
                    false
                }
            }


        }


        overrideBack()
        mViewModalImage.onClearListImageLiveData()

        return binding.root
    }

    private fun onClickNavigationDrawer() {
//        val window: Window = requireActivity().window

        binding.dlLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val angle = slideOffset * 180
                binding.btnDrawer.setRotation(angle)
                binding.navigationView.bringToFront()
                binding.navigationView.requestFocus()
            }

            override fun onDrawerOpened(drawerView: View) {
                binding.btnDrawer.setRotation(90f)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    window.statusBarColor = getColor(R.color.transparent)
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.btnDrawer.setRotation(0f)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    window.statusBarColor = getColor(R.color.blue_gradient_end)
                }
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }


    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter( object : IClickListener {
            override fun onClickItem(note: Note) {
                mViewModalNote.updateSelectedNote(note)
                openFragmentVisible()
            }
        })

        binding_Home?.rycNotes?.adapter = noteAdapter
    }


    private fun openActionButton() {
        binding_Home!!.btnFloating.setOnClickListener { openFragmentEdit() }
        binding_Home!!.imgbutIntroduce.setOnClickListener({ openDialogIntroduce(Gravity.CENTER) })
        binding.btnDrawer.setOnClickListener { v ->
            if (binding.dlLayout.isDrawerOpen(GravityCompat.START)) {
                binding.dlLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.dlLayout.openDrawer(GravityCompat.START)
                binding.navigationView.bringToFront()
                binding.navigationView.requestFocus()
            }
        }

    }

    private fun setSearchView() {
        binding_Home!!.imgbutSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mViewModalNote.onFilterChange(query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                mViewModalNote.onFilterChange(newText)
                return false
            }
        })
    }

    private fun checkInitData(mListNote : MutableList<Note>) {
        if (isexistData(mListNote)) {
            binding_Home!!.linearNon.visibility = View.GONE
            binding_Home!!.rycNotes.visibility = View.VISIBLE
        } else {

            binding_Home!!.linearNon.visibility = View.VISIBLE
            binding_Home!!.rycNotes.visibility = View.GONE
        }
    }
    private fun isexistData(mListNote:MutableList<Note>): Boolean {

//        Log.d("checkInitData","Kiem tra list co rong"+ mListNote.toString())
        return mListNote.isNotEmpty()
    }


    fun overrideBack(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if ((binding_Home?.imgbutSearch != null) && !binding_Home?.imgbutSearch!!.isIconified()) {
                    // Nếu SearchView đang mở, thu nhỏ nó thay vì thoát ứng dụng
                    binding_Home?.imgbutSearch!!.onActionViewCollapsed()

                } else {
                    // Nếu không, thực hiện hành động mặc định khi nhấn nút Back
                    requireActivity().finish()
                }
            }
        })
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

    private fun openFragmentVisible() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frag_layout, VisibleItemFragment::class.java,null)
            .addToBackStack(null)
            .commit()
    }
    private fun openFragmentInformationAccount() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frag_layout, UpdateUserFragment::class.java,null)
            .addToBackStack(null)
            .commit()
    }

    private fun openFragmentEdit() {
        val fragment = EditFragment()
        val bundle = Bundle()
        bundle.putString("HometoEdit1","Anh")
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.frag_layout, fragment,null)
            .addToBackStack(null)
            .commit()
    }

    private fun openFragmentChangePassword(){

        parentFragmentManager.beginTransaction()
            .replace(R.id.frag_layout, ChangePasswordFragment::class.java,null)
            .addToBackStack(null)
            .commit()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?) {
            if (viewHolder is NoteViewHodel) {
                val indexDelte = viewHolder.getAdapterPosition()
                val noteDelete = noteAdapter.getNoteAtPosition(indexDelte)
                var undoClicked = false
                mViewModalImage.getListImage(noteDelete.idNote)


                mViewModalNote.onClickDeleteNote(noteDelete)
                Log.d("Swip"," Xóa trong adapter "+ noteDelete.title)

                val snackbar = Snackbar.make(binding_Home!!.rootView, "${noteDelete.title} remove!", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO") {
                    Log.d("Swip","Hoi phuc lai")
                    mViewModalNote.onClickAddNote(indexDelte,noteDelete)
                    undoClicked = true
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if(undoClicked == false) {
                            Log.d("Swip", "Xóa trong SQL ")


                            mViewModalImage.deleteImageInlistInDB()

                            mViewModalNote.deleteNote(noteDelete)

                            mViewModalImage.onClearListImageLiveData()

                        }
                    }
                })
                snackbar.show()
            }
    }



    override fun onDestroy() {
        super.onDestroy()
        binding_Home= null
    }
}