package com.example.project_note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.project_note.ViewModal.ViewModalNote
import com.example.project_note.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModalNote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ViewModalNote::class.java)

        if (savedInstanceState == null) {
            // Hiển thị FragmentA khi lần đầu tiên mở ứng dụng
            openHomeFragment()
        }

    }
        fun openHomeFragment() {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frag_layout, HomeFragment(), "Home")
                .addToBackStack(null)
                .commit()
        }

    }
