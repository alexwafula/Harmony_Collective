package com.example.harmonycollective

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.sdk.android.auth.AuthorizationClient

class SettingsActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        layoutInflater.inflate(R.layout.activity_settings, findViewById(R.id.content_frame))

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_settings

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_recs -> {
                    startActivity(Intent(this, RecsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_playlist -> {
                    startActivity(Intent(this, PlaylistActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_settings -> {
                    // Do nothing, we're already here
                    true
                }
                else -> false
            }
        }

        findViewById<Button>(R.id.btn_logout).setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Clear the access token or any session data
        AuthorizationClient.clearCookies(this)
        // Optionally, you can clear any stored preferences or session variables here

        // Navigate back to the login activity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
