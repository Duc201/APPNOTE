package com.example.project_note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        openHomeFragment();
    }

    private fun openHomeFragment() {
        TODO("Not yet implemented")

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frag_layout, HomeFragment())
        transaction.commit()
    }
}