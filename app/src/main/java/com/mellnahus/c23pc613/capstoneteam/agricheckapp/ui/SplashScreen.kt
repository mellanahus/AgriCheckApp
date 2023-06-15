package com.mellnahus.c23pc613.capstoneteam.agricheckapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.R
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.autentikasi.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        Handler().postDelayed({
            val intent = Intent(this@SplashScreen,
                LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
