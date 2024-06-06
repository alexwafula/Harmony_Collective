package com.example.harmonycollective

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class LoginActivity : AppCompatActivity() {

    private val CLIENT_ID = "e3f2b2705f314f8fa4a08092b833cc36"
    private val REDIRECT_URI = "http://127.0.0.1:8000"
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
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("access_token", accessToken)
                    startActivity(intent)
                    finish()
                }
                AuthorizationResponse.Type.ERROR -> {
                    // Handle error response
                    // You can show a message to the user or log the error
                }
                else -> {
                    // Handle other response types if any
                }
            }
        }
    }
}