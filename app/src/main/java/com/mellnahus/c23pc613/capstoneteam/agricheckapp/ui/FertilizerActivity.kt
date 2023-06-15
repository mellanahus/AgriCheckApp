package com.mellnahus.c23pc613.capstoneteam.agricheckapp.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.databinding.ActivityFertilizerBinding
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.Features
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.FertilizerResponse
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FertilizerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFertilizerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFertilizerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Rekomendasi Pemupukan"

        binding.PredictButton.setOnClickListener {

            if (isInputValid()) {
                val temperature = binding.TemperatureId.editableText.toString().toDouble()
                val humidity = binding.HumidityId.editableText.toString().toDouble()
                val moisture = binding.MoistureId.editableText.toString().toDouble()
                val soilType =
                    getIndexOfSoilType(binding.SoilTypeId.editableText.toString().lowercase())
                val cropType =
                    getIndexOfCropType(binding.CropTypeId.editableText.toString().lowercase())
                val nitrogen = binding.NitrogenId.editableText.toString().toDouble()
                val potassium = binding.PotassiumId.editableText.toString().toDouble()
                val phospor = binding.PhosporId.editableText.toString().toDouble()

                val features = listOf<Double?>(
                    temperature,
                    humidity,
                    moisture,
                    soilType,
                    cropType,
                    nitrogen,
                    potassium,
                    phospor
                )

                @Suppress("UNCHECKED_CAST")
                val featuresModel = Features(features as List<Double>)

                val request = ApiConfig.getNewApiService().getPredictedFertilizer(featuresModel)
                showLoading(true)
                request.enqueue(object : Callback<FertilizerResponse> {
                    override fun onResponse(
                        call: Call<FertilizerResponse>,
                        response: Response<FertilizerResponse>,
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            binding.HasilPrediksi2.text = response.body()?.predicted_fert
                            clearInput()
                        } else {
                            Toast.makeText(
                                this@FertilizerActivity,
                                "Gagal memperoleh prediksi pupuk",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<FertilizerResponse>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(
                            this@FertilizerActivity,
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
        val temperature = binding.TemperatureId.text.toString()
        val humidity = binding.HumidityId.text.toString()
        val moisture = binding.MoistureId.text.toString()
        val soilType = binding.SoilTypeId.text.toString()
        val cropType = binding.CropTypeId.text.toString()
        val nitrogen = binding.NitrogenId.text.toString()
        val potassium = binding.PotassiumId.text.toString()
        val phosphor = binding.PhosporId.text.toString()

        if (temperature.isEmpty()) {
            binding.TemperatureId.error = "Masukkan temperatur"
            return false
        }

        if (humidity.isEmpty()) {
            binding.HumidityId.error = "Masukkan kelembaban"
            return false
        }

        if (moisture.isEmpty()) {
            binding.MoistureId.error = "Masukkan kelembaban tanah"
            return false
        }

        if (soilType.isEmpty()) {
            binding.SoilTypeId.error = "Masukkan jenis tanah"
            return false
        }

        if (cropType.isEmpty()) {
            binding.CropTypeId.error = "Masukkan jenis tanaman"
            return false
        }

        if (nitrogen.isEmpty()) {
            binding.NitrogenId.error = "Masukkan kadar nitrogen"
            return false
        }

        if (potassium.isEmpty()) {
            binding.PotassiumId.error = "Masukkan kadar potassium"
            return false
        }

        if (phosphor.isEmpty()) {
            binding.PhosporId.error = "Masukkan kadar phosphor"
            return false
        }

        return true
    }

    private fun getIndexOfSoilType(soilType: String): Double {
        return when(soilType) {
            "black" -> 0.0
            "clayey" -> 1.0
            "loamy" -> 2.0
            "red" -> 3.0
            "sandy" -> 4.0
            else -> -1.0
        }
    }

    private fun getIndexOfCropType(cropType: String): Double {
        return when(cropType) {
            "barley" -> 0.0
            "cotton" -> 1.0
            "ground nuts" -> 2.0
            "maize" -> 3.0
            "millets" -> 4.0
            "oil seeds" -> 5.0
            "paddy" -> 6.0
            "pulses" -> 7.0
            "sugarcane" -> 8.0
            "tobacco" -> 9.0
            "wheat" -> 10.0
            else -> -1.0
        }
    }

    private fun clearInput() {
        binding.TemperatureId.setText("")
        binding.HumidityId.setText("")
        binding.MoistureId.setText("")
        binding.SoilTypeId.setText("")
        binding.CropTypeId.setText("")
        binding.NitrogenId.setText("")
        binding.PotassiumId.setText("")
        binding.PhosporId.setText("")
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}