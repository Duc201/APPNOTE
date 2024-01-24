package com.example.project_note.Activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.project_note.ViewModal.AuthViewModel
import com.example.project_note.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mViewModelAuth: AuthViewModel
    private lateinit var sharedpreferences: SharedPreferences

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
        const val CHECKBOX_KEY = "checkbox_key"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        mViewModelAuth = ViewModelProvider(this).get(AuthViewModel::class.java)


        clickbutton()
        checkSharedPreference()



        mViewModelAuth.userResponse.observe(this){


            val email = binding.txtEmail.text.toString()
            val pass = binding.txtPass.text.toString()

            if(it.email.equals(email)&& it.pass.equals(pass)){
                    if (binding.checkbox.isChecked) {
                        val editor = sharedpreferences.edit()
                        editor.putBoolean(CHECKBOX_KEY, true)
                        editor.putString(EMAIL_KEY,  binding.txtEmail.text.toString())
                        editor.putString(PASSWORD_KEY, binding.txtPass.text.toString())
                        editor.commit()
                    }
            }
            openMainActivity()

        }
        setContentView(binding.root)

    }

    private fun checkSharedPreference() {
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        binding.txtEmail.setText(sharedpreferences.getString(EMAIL_KEY, ""))
        binding.txtPass.setText(sharedpreferences.getString(PASSWORD_KEY, ""))
        binding.checkbox.isChecked = sharedpreferences.getBoolean(CHECKBOX_KEY, true)
    }

    private fun clickbutton() {
        binding!!.btnLogin.setOnClickListener{
            login()
        }
        binding.txtRegister.setOnClickListener{
            openActivityRegister()
        }
        binding.txtResetpass.setOnClickListener {
            openActivityForgetPass()
        }
    }

    private fun openActivityForgetPass() {
        var intent = Intent(this, ForgotPassActivity::class.java)
        startActivity(intent)
    }

    private fun openActivityRegister() {
        var intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun openMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun login() {
        val email = binding?.txtEmail?.text.toString().trim()
        val pass = binding?.txtPass?.text.toString().trim()
      if( mViewModelAuth.checkValidEmail(this,email)  && mViewModelAuth.checkValidPassword(this,pass)){
                        mViewModelAuth.loginRequest(this,email,pass)

      }
    }




}