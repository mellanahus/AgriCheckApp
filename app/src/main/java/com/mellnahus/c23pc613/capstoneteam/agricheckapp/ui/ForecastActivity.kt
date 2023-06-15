package com.mellnahus.c23pc613.capstoneteam.agricheckapp.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.databinding.ActivityForecastBinding
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.WeatherResponse
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastBinding
    private val apiService = ApiConfig.getApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Perkiraan Cuaca"

        binding.btnSearch.setOnClickListener {
            val location = binding.etQuery.text.toString()
            // fungsi untuk mengambil data dari API berdasarkan lokasi
            fetchWeatherData(location)
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

    private fun fetchWeatherData(location: String) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getSearchLocation(location)

        showLoading(true)

        call.enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        // Ambil data forecast dari response
                        val forecast = it.forecast.firstOrNull()
                        forecast?.let { forecastData ->

                            binding.cuaca.text = forecastData.text
                            binding.suhu.text = forecastData.temperature.toString()
                            binding.kelembapan.text = forecastData.humidity.toString()
                            binding.angin.text = forecastData.windSpeed.toString()
                        }
                    }
                } else {
                    Toast.makeText(this@ForecastActivity, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@ForecastActivity, "Terjadi kesalahaan saat ini, coba periksa koneksi internet anda", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}