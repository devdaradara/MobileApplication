package com.example.mobileapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val backExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val mainExecutor: Executor = ContextCompat.getMainExecutor(this)
        backExecutor.schedule({
            mainExecutor.execute {
                startActivity(Intent(applicationContext, FirstActivity::class.java))
                finish()
            }
        }, 2, TimeUnit.SECONDS)
    }
}
