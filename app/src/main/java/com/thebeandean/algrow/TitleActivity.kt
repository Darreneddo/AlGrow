package com.thebeandean.algrow

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess


class TitleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.title_main)

        val cultureScreenButton = findViewById<Button>(R.id.cultureScreenButton)
        val libraryButton = findViewById<Button>(R.id.libraryButton)
        val closeButton = findViewById<Button>(R.id.closeButton)

        //Set action for button to take user to Culture screen
        cultureScreenButton.setOnClickListener{
            startActivity(Intent(this, CultureActivity::class.java))
        }
        //Set action for button to take user to library screen
        libraryButton.setOnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
        }
        //Close Button Actions
        closeButton.setOnClickListener {
            moveTaskToBack(true)
            exitProcess(-1)
        }
    }
}
