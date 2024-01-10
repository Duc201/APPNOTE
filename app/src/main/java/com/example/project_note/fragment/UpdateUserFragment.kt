package com.example.project_note.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project_note.R
import com.example.project_note.ViewModal.AuthViewModel
import com.example.project_note.databinding.FragmentInformationAccountBinding
import com.squareup.picasso.Picasso
import java.io.IOException

class UpdateUserFragment : Fragment() {

    private lateinit var binding : FragmentInformationAccountBinding
    private lateinit var mViewModelAuth : AuthViewModel
    var PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationAccountBinding.inflate(inflater,container,false)
        mViewModelAuth = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        mViewModelAuth.getInformationUser(requireContext())
        mViewModelAuth.userResponse.observe(viewLifecycleOwner){
            binding.txtEmail.setText(it.email)
            binding.txtName.setText(it.name)
            Picasso.with(context)
                .load(it.image)
                .placeholder(R.drawable.icon_person_large)
                .error(R.drawable.icon_person_large)
                .into(binding.imagePerson);

        }
        binding.btnSave.setOnClickListener {

           val name =  binding.txtName.text.toString()
            mViewModelAuth.updateInformationUser(requireContext(),name)
        }
        binding.imagePerson.setOnClickListener{
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                getImage()
            }
            activity?.let {
                if (hasPermissions(it as Context, PERMISSIONS)) {
                    getImage()
                } else {
                    permReqLauncher.launch(PERMISSIONS)
                }
            }
        }
        return binding.root
    }

    private val permReqLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
        val granted = permissions.entries.all {
            it.value == true
        }
        if (granted) {
            getImage()
        }
    }
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imagedata: Intent? = result.data
            if (imagedata != null && imagedata.getData() != null) {
                val selectedImageUri = imagedata.data
                mViewModelAuth.updateUri(selectedImageUri!!)
                binding.imagePerson.setImageURI(selectedImageUri)
            }
        }
    }
}