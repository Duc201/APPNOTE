package com.example.project_note.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.project_note.ViewModal.AuthViewModel
import com.example.project_note.databinding.ActivityForgotpasswordBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotpasswordBinding
    private lateinit var mViewModelAuth: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotpasswordBinding.inflate(layoutInflater)
        mViewModelAuth = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding.btnSendEmail.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            mViewModelAuth.checkValidEmail(this,email)
            mViewModelAuth.sendPassordReset(this,email)
        }

        binding.imgaBack.setOnClickListener{
           finish()
        }
        mViewModelAuth.checkreplacepass.observe(this){
            if(it == true){
                openActivityResultFogot()
            }
            else{
                Toast.makeText(this,"Gửi mail thất bại ",Toast.LENGTH_SHORT).show()
            }
        }

        setContentView(binding.root)
    }

    private fun openActivityResultFogot() {
        val intent = Intent(this, ResultFogotActivity::class.java)
        startActivity(intent)
    }
}