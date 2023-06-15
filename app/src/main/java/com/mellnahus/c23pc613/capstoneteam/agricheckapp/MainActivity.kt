package com.mellnahus.c23pc613.capstoneteam.agricheckapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.databinding.ActivityMainBinding
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.ui.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icAkun.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.buttonPerkiraan.setOnClickListener {
            val intent = Intent(this@MainActivity, ForecastActivity::class.java)
            startActivity(intent)
        }

        binding.buttonPrediksiKapas.setOnClickListener {
            val intent = Intent(this@MainActivity, CottonPredictionActivity::class.java)
            startActivity(intent)
        }

        binding.buttonRekomendasi.setOnClickListener {
            val intent = Intent(this@MainActivity, FertilizerActivity::class.java)
            startActivity(intent)
        }

        binding.buttonIdentifikasi.setOnClickListener {
            val intent = Intent(this@MainActivity, CropPredictionActivity::class.java)
            startActivity(intent)
        }

        binding.buttonPrediksiTanah.setOnClickListener {
            val intent = Intent(this@MainActivity, SoilPredictionActivity::class.java)
            startActivity(intent)
        }

    }
}