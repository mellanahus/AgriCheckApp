package com.mellnahus.c23pc613.capstoneteam.agricheckapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.databinding.ActivityCropPredictionBinding
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.CropResponse
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.Features
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CropPredictionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCropPredictionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCropPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Rekomendasi Tanaman"

        binding.buttonPrediksi.setOnClickListener {

            if (isInputValid()) {
                val phospor = binding.PhosporIdCrop.editableText.toString().toDouble()
                val potassium = binding.PotassiumIdCrop.editableText.toString().toDouble()
                val nitrogen = binding.NitrogenIdCrop.editableText.toString().toDouble()
                val temperature = binding.TemperatureIdCrop.editableText.toString().toDouble()
                val humidity = binding.HumidityIdCrop.editableText.toString().toDouble()
                val ph = binding.PHIdCrop.editableText.toString().toDouble()
                val rainfall = binding.RainfallIdCrop.editableText.toString().toDouble()

                val features = listOf<Double?>(
                    phospor,
                    potassium,
                    nitrogen,
                    temperature,
                    humidity,
                    ph,
                    rainfall
                )

                @Suppress("UNCHECKED_CAST")
                val featuresModel = Features(features as List<Double>)

                val request = ApiConfig.getNewApiService().getPredictCrop(featuresModel)
                showLoading(true)
                request.enqueue(object : Callback<CropResponse> {
                    override fun onResponse(
                        call: Call<CropResponse>,
                        response: Response<CropResponse>,
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            binding.HasilPrediksi.text = response.body()?.predicted_crop
                            clearInput()
                        } else {
                            Toast.makeText(
                                this@CropPredictionActivity,
                                "Gagal memperoleh prediksi tanaman",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<CropResponse>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(
                            this@CropPredictionActivity,
                            "Terjadi kesalahaan saat ini, coba periksa koneksi internet anda",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            }
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

    // Cek Kondisi Inputan

    private fun isInputValid(): Boolean {
        val phospor = binding.PhosporIdCrop.text.toString()
        val potassium = binding.PotassiumIdCrop.text.toString()
        val nitrogen = binding.NitrogenIdCrop.text.toString()
        val temperature = binding.TemperatureIdCrop.text.toString()
        val humidity = binding.HumidityIdCrop.text.toString()
        val ph = binding.PHIdCrop.text.toString()
        val rainfall = binding.RainfallIdCrop.text.toString()

        if (phospor.isEmpty()) {
            binding.PhosporIdCrop.error = "Masukkan kadar phospor"
            return false
        }

        if (potassium.isEmpty()) {
            binding.PotassiumIdCrop.error = "Masukkan kadar potassium"
            return false
        }

        if (nitrogen.isEmpty()) {
            binding.NitrogenIdCrop.error = "Masukkan kadar nitrogen"
            return false
        }

        if (temperature.isEmpty()) {
            binding.TemperatureIdCrop.error = "Masukkan kadar temperature"
            return false
        }

        if (humidity.isEmpty()) {
            binding.HumidityIdCrop.error = "Masukkan kadar humidity"
            return false
        }

        if (ph.isEmpty()) {
            binding.PHIdCrop.error = "Masukkan kadar ph"
            return false
        }

        if (rainfall.isEmpty()) {
            binding.RainfallIdCrop.error = "Masukkan kadar rainfall"
            return false
        }

        return true
    }

    private fun clearInput() {
        binding.PhosporIdCrop.setText("")
        binding.PotassiumIdCrop.setText("")
        binding.NitrogenIdCrop.setText("")
        binding.TemperatureIdCrop.setText("")
        binding.HumidityIdCrop.setText("")
        binding.PHIdCrop.setText("")
        binding.RainfallIdCrop.setText("")
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}