package com.example.harmonycollective

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var albumCoverImageView: ImageView
    private lateinit var messageTextView: TextView

    private val images = arrayOf(
        R.drawable.album_cover1,
        R.drawable.album_cover2,
        R.drawable.album_cover3
    )

    private val messages = arrayOf(
        "All your favorite artists in one place",
        "Good music recommendations",
        "Share music with your loved ones"
    )

    private var currentIndex = 0

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (currentIndex < images.size) {
                albumCoverImageView.setImageResource(images[currentIndex])
                messageTextView.text = messages[currentIndex]
                currentIndex++
                handler.postDelayed(this, 2000) // Change image and text every 2 seconds
            } else {
                // Navigate to the login activity
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        albumCoverImageView = findViewById(R.id.img_album_cover)
        messageTextView = findViewById(R.id.txt_message)

        // Start image and message rotation
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}

