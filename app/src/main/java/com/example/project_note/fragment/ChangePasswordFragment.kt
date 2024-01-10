package com.example.project_note.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_note.databinding.FragmentChangePasswordBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ChangePasswordFragment : Fragment() {

    lateinit var binding: FragmentChangePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)


        binding.imgaBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.btnSendEmail.setOnClickListener {
            changepassword()
        }
        return binding.root
    }

    private fun changepassword() {
        val user = Firebase.auth.currentUser

        val newPassword = binding.txtPass.text.toString().trim()
        val renewPassword = binding.txtRepass.text.toString().trim()
        if(!newPassword.equals(renewPassword)){
            Toast.makeText(requireContext(),"Nhập mật khẩu chưa khớp",Toast.LENGTH_SHORT).show()
        }

        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
    }
}