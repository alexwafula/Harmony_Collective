package com.example.harmonycollective

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class LoginActivity : AppCompatActivity() {

    private val CLIENT_ID = "e3f2b2705f314f8fa4a08092b833cc36"
    private val REDIRECT_URI = "http://localhost:8000/callback"
    private val REQUEST_CODE = 1337

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            val builder = AuthorizationRequest.Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URI
            )
            builder.setScopes(arrayOf("user-read-private", "playlist-read", "playlist-read-private"))
            val request = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    // Handle successful login and save access token
                    val accessToken = response.accessToken
                    // Navigate to the main activity
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("access_token", accessToken)
                    startActivity(intent)
                    finish()
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("LoginActivity", "Authorization error: ${response.error}")
                    showErrorToast("Login failed, please try again.")
                }
                else -> {
                    Log.w("LoginActivity", "Unexpected response type: ${response.type}")
                    showErrorToast("Unexpected error, please try again.")
                }
            }
        }
    }
    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}