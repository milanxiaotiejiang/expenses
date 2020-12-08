package com.nominalista.expenses.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nominalista.expenses.R
import com.nominalista.expenses.home.presentation.HomeActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        HomeActivity.start(this)
        finish()
    }

}