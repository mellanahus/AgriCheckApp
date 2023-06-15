package com.mellnahus.c23pc613.capstoneteam.agricheckapp.autentikasi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.databinding.ActivityRegisterBinding
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.RegisterResponse
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.User
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val address = binding.etAdress.text.toString()
            val pass = binding.etPassword.text.toString()

            val user = User(name, email, address, pass)

            //melakukan register
            val request = ApiConfig.getApiService().signup(user)
            request.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@RegisterActivity, "Berhasil Mendaftarkan Akun", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Gagal melakukan pendaftaran", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Gagal melakukan pendaftaran. Periksa koneksi internet anda.", Toast.LENGTH_SHORT).show()

                }

            })

        }

        binding.login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        playAnimation()
    }

    private fun playAnimation(){
        val title = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val address = ObjectAnimator.ofFloat(binding.etAdress, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val garisView = ObjectAnimator.ofFloat(binding.view, View.ALPHA, 1f).setDuration(500)
        val questionLog = ObjectAnimator.ofFloat(binding.tvQuestionlog, View.ALPHA,1f).setDuration(500)
        val log = ObjectAnimator.ofFloat(binding.login, View.ALPHA,1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(garisView, questionLog, log )
        }

        AnimatorSet().apply {
            playSequentially(title, name, email, address, pass, register, together)
            start()
        }

    }
}