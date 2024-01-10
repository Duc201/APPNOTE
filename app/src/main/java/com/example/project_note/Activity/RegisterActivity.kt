package com.example.project_note.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.project_note.ViewModal.AuthViewModel
import com.example.project_note.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var mViewModelAuth: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        mViewModelAuth = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding.imgaBack.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener{
            registerUser()
        }

        setContentView(binding.root)
    }

    private fun registerUser() {
        val email = binding.txtEmail.text.toString().trim()
        val pass = binding.txtPass.text.toString().trim()
        val confirmPass = binding.txtConfpass.text.toString().trim()


        if(mViewModelAuth.checkValidEmail(this,email) && mViewModelAuth.checkValidPassword(this, pass) && mViewModelAuth.checkValidPassword(this,confirmPass)
            && mViewModelAuth.checkValidRePassword(this, pass, confirmPass)){
            mViewModelAuth.register(this,email,pass)
        }

    }

}