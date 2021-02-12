package com.example.chatroom

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class LogIn : AppCompatActivity(), CoroutineScope {

    //START COROUTINE
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val usernameField = findViewById<TextView>(R.id.usernameField)
        val passwordField = findViewById<TextView>(R.id.passwordField)
        val loginButton = findViewById<Button>(R.id.loginButton)

        //LOGIN USER ON BUTTON CLICK
        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            launch {
                try {
                    login(username, password)
                    Toast.makeText(applicationContext, "Logged in successfully", Toast.LENGTH_LONG)
                        .show()
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Oops!, something went wrong, please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    //LOGIN METHOD
    private fun login(username: String, password: String) {
        Toast.makeText(applicationContext, "Log in request feature", Toast.LENGTH_LONG).show()
        //IMPLEMENT SESSION
    }
}