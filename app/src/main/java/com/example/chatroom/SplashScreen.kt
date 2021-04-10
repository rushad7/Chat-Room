package com.example.chatroom

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class SplashScreen : AppCompatActivity(), CoroutineScope {

    //START COROUTINE
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        //CHECK INTERNET CONNECTION
        val mainActivityIntent = Intent(this@SplashScreen, MainActivity::class.java)
        launch {
            try {
                Utils.pingServer()
            } catch (e: Exception) {
                Toast.makeText(
                    this@SplashScreen,
                    "Check your Internet connection and try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
            startActivity(mainActivityIntent)
        }
    }
}