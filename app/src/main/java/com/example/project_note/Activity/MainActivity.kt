package com.example.project_note.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.project_note.R
import com.example.project_note.ViewModal.ViewModalNote
import com.example.project_note.fragment.HomeFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModalNote
    private lateinit var auth: FirebaseAuth
// ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ViewModalNote::class.java)
        // Initialize Firebase Auth
        auth = Firebase.auth

        if (savedInstanceState == null) {
            openHomeFragment()
        }

    }

    fun openHomeFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frag_layout, HomeFragment(),null)
            .commit()
    }

    }
