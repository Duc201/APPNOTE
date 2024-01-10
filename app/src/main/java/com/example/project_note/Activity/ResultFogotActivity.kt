package com.example.project_note.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_note.databinding.ActivityResultForgorpassBinding


class ResultFogotActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultForgorpassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultForgorpassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClick()

    }

    private fun setupClick() {
        binding.btnOpenemail.setOnClickListener {
            var intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")  // This sets the data for the intent to be of email type
            startActivity(intent)
        }
        binding.backfogot.setOnClickListener {
            finish()
        }
        binding.skip.setOnClickListener {
            /*
            * Hỏi lại đoạn này logic có được không
            * */
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}
