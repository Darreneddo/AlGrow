package com.thebeandean.algrow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LibraryActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.library_screen_main)

        val returnButton = findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener{
            finishAndRemoveTask()
            startActivity(Intent(this, TitleActivity::class.java))
        }


    }
}