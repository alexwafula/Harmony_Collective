package com.example.harmonycollective

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

class HomeActivity : AppCompatActivity() {

    private lateinit var textViewUserName: TextView
    private lateinit var imageViewProfile: ImageView
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        textViewUserName = findViewById(R.id.text_view_user_name)
        imageViewProfile = findViewById(R.id.image_view_profile)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        val accessToken = intent.getStringExtra("access_token") ?: return

        fetchSpotifyUserProfile(accessToken)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> true
                R.id.navigation_recs -> {
                    startActivity(Intent(this, RecsActivity::class.java))
                    true
                }
                R.id.navigation_playlist -> {
                    startActivity(Intent(this, PlaylistActivity::class.java))
                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchSpotifyUserProfile(accessToken: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val spotifyService = retrofit.create(SpotifyService::class.java)
        val call = spotifyService.getCurrentUserProfile("Bearer $accessToken")

        call.enqueue(object : retrofit2.Callback<SpotifyUserProfile> {
            override fun onResponse(
                call: retrofit2.Call<SpotifyUserProfile>,
                response: retrofit2.Response<SpotifyUserProfile>
            ) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    profile?.let {
                        textViewUserName.text = "Hello, ${it.displayName}"
                        Glide.with(this@HomeActivity)
                            .load(it.images.firstOrNull()?.url)
                            .into(imageViewProfile)
                    }
                } else {
                    // Handle the case where the response is not successful
                    showErrorToast("Failed to retrieve profile information.")
                    Log.e("HomeActivity", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<SpotifyUserProfile>, t: Throwable) {
                // Handle failure
                showErrorToast("Network error, please try again.")
                Log.e("HomeActivity", "Network error: ", t)
            }
        })
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    interface SpotifyService {
        @GET("me")
        fun getCurrentUserProfile(
            @Header("Authorization") authorization: String
        ): retrofit2.Call<SpotifyUserProfile>
    }

    data class SpotifyUserProfile(
        @SerializedName("display_name") val displayName: String,
        val images: List<Image>
    )

    data class Image(
        val url: String
    )
}
