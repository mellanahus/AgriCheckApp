package com.mellnahus.c23pc613.capstoneteam.agricheckapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.autentikasi.LoginActivity
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.databinding.ActivityProfileBinding
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.helper.UserPreferences
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.ProfileResponse
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val apiService = ApiConfig.getApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profil"

        getProfileData()

        val userPreferences = UserPreferences(applicationContext)
        binding.btnLogout.setOnClickListener {
            userPreferences.logout()

            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

    private fun getProfileData() {
        apiService.getUser().enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    profileResponse?.let {
                        binding.namaPengguna.text = it.name
                        binding.pengguna.text = it.name
                        binding.emailPengguna.text = it.email
                        binding.alamatPengguna.text = it.address
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Terjadi kesalahaan saat ini, coba periksa koneksi internet anda",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}