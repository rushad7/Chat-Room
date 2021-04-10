package com.example.chatroom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.ktor.client.statement.*
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
        val loginSettings = LoginSettings(this)

        //LOGIN USER ON BUTTON CLICK
        loginButton.setOnClickListener {

            val username = usernameField.text.toString()
            val password = Utils.sha512(passwordField.text.toString())

            launch {
                try {
                    val loginSuccessful = Utils.sendRequest("login", username, password).readText().toBoolean()
                    if (loginSuccessful) {
                        loginSettings.login(username, password)
                        Toast.makeText(applicationContext, "Logged in successfully", Toast.LENGTH_SHORT)
                            .show()
                        val roomIntent = Intent(this@LogIn, Room::class.java)
                        startActivity(roomIntent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Incorrect credentials, please try again later",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Oops! Something went wrong, please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}