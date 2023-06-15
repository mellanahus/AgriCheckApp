package com.mellnahus.c23pc613.capstoneteam.agricheckapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.databinding.ActivityCottonPredictionBinding
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.CuttonResponse
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.networking.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CottonPredictionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCottonPredictionBinding
    private var getPhotoFile: File? = null

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@CottonPredictionActivity)
                getPhotoFile = myFile
                binding.previewImageView.setImageURI(uri)
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("photo", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("photo")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                getPhotoFile = file
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCottonPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Prediksi Penyakit Kapas"

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnGaleri.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Pilih Gambar")
            launcherIntentGallery.launch(chooser)
        }

        binding.btnCamera.setOnClickListener {
            val intent = Intent(this@CottonPredictionActivity, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.btnPrediksi.setOnClickListener {
            showLoading(true)
            uploadImage()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadImage() {
        if (getPhotoFile != null) {
            val file = getPhotoFile as File

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )

            val apiService = ApiConfig.getNewApiService()
            val uploadImageRequest = apiService.getPredictCutton(imageMultipart)
            uploadImageRequest.enqueue(object : Callback<CuttonResponse> {
                override fun onResponse(
                    call: Call<CuttonResponse>,
                    response: Response<CuttonResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        binding.tvHasilPrediksi.text = response.body()?.predicted_class
                    }
                    else {
                        Toast.makeText(
                            this@CottonPredictionActivity,
                            "Gagal melakukan prediksi kapas.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                override fun onFailure(call: Call<CuttonResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(
                        this@CottonPredictionActivity,
                        "Terjadi kesalahaan saat ini, coba periksa koneksi internet anda",
                        Toast.LENGTH_SHORT
                    ).show()                }
            })

        } else {
            Toast.makeText(this@CottonPredictionActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}