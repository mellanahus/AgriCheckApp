package com.mellnahus.c23pc613.capstoneteam.agricheckapp.autentikasi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.MainActivity
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.databinding.ActivityLoginBinding
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.helper.UserPreferences
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.LoginResponse
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.User
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val userPreferences = UserPreferences(applicationContext)
        if (userPreferences.isLogin()) {
            val intent =  Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.btnLogin.setOnClickListener {
            binding.btnLogin.isEnabled = false
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            val user = User(email = email, pass = pass)

            // melakukan login
            val request = ApiConfig.getApiService().signin(user)
            request.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    binding.btnLogin.isEnabled = true
                    if (response.isSuccessful) {
                        val body = response.body()!!
                        if (body.error) {
                            Toast.makeText(this@LoginActivity, "Data yang anda masukkan tidak terdaftar", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, body.message, Toast.LENGTH_SHORT).show()

                            userPreferences.setSession("username")
                            val intent =  Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Terjadi kesalahan saat melakukan login", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this@LoginActivity, "Gagal melakukan login. Periksa koneksi internet anda.", Toast.LENGTH_SHORT).show()
                }
            })
        }

            binding.register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        playAnimation()
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val garisView = ObjectAnimator.ofFloat(binding.view, View.ALPHA, 1f).setDuration(500)
        val questionReg = ObjectAnimator.ofFloat(binding.tvQuestionreg, View.ALPHA,1f).setDuration(500)
        val reg = ObjectAnimator.ofFloat(binding.register, View.ALPHA,1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(garisView, questionReg, reg )
        }

        AnimatorSet().apply {
            playSequentially(title, email, pass, login, together)
            start()
        }


    }
}