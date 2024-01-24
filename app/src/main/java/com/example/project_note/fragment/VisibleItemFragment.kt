package com.example.project_note.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.project_note.DataBase.Image
import com.example.project_note.DataBase.Note
import com.example.project_note.R
import com.example.project_note.RecycleView.IClickListener1
import com.example.project_note.RecycleView.ImageAdapter
import com.example.project_note.ViewModal.ViewModalImage
import com.example.project_note.ViewModal.ViewModalNote
import com.example.project_note.databinding.FragmentVisibleItemBinding

class VisibleItemFragment : Fragment() {

    lateinit var binding_Visible: FragmentVisibleItemBinding
    private val binding get() = binding_Visible!!

    private lateinit var mViewModalNote: ViewModalNote
    private lateinit var mViewModalImage: ViewModalImage

    private lateinit var mImageAdapter : ImageAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("Visible","Sang Thang Visible rồi")
        binding_Visible = FragmentVisibleItemBinding.inflate(inflater, container,false)
        mViewModalNote = ViewModelProvider(requireActivity()).get(ViewModalNote::class.java)
        mViewModalImage = ViewModelProvider(requireActivity()).get(ViewModalImage::class.java)



        setupRecycleView()
        mViewModalNote.selectNote.observe(viewLifecycleOwner){

            binding_Visible.txtTitleVisible.setText(it?.title.toString())
            binding_Visible.txtDetailVisible.setText(it?.detail.toString())
            showdata(it)
        }
        setupButtons()
        mViewModalImage.setListImageDeleteNull()
        return binding.root
    }

    private fun setupRecycleView() {

        mImageAdapter = ImageAdapter(object : IClickListener1{
            override fun onClickImage(image: Image) {
                mViewModalImage.updateSelectImage(image)
                openImageFragment()
            }

        },requireContext())

        binding_Visible.rcyPhotos.adapter = mImageAdapter
    }

    private fun openImageFragment() {
        val bundle = Bundle()
        bundle.putString("keyvisible", "visible")
        parentFragmentManager.beginTransaction()
            .replace(R.id.frag_layout, ImageFragment::class.java,bundle)
            .addToBackStack(null)
            .commit()
    }

    private fun setupButtons() {
        binding_Visible.imgbutEdit.setOnClickListener {
            openFragmentEdit()
        }
        binding_Visible.imgbutBack.setOnClickListener {
            backPressed()
        }
    }

    private fun openFragmentEdit() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frag_layout, EditFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showdata(it: Note) {

        mViewModalImage.getListImage(it.idNote)
        mViewModalImage.mListImage.observe(viewLifecycleOwner){
            mImageAdapter.submitList(it)
        }
    }

    private fun backPressed(){
        val fragmentManager = requireActivity().supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            requireActivity().finish()
        }
    }


}