package com.example.chatroom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {

    //START COROUTINE
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val loginSettings = LoginSettings(this)
        val isLoggedIn = loginSettings.getState()
        val chatIntent = Intent(this, ChatPage::class.java)

        launch {
            if (isLoggedIn) {
                val userCredentials = loginSettings.getUserInfo()
                val username = userCredentials.first.toString()
                val password = userCredentials.second.toString()

                Utils.sendRequest("login", username, password)
                startActivity(chatIntent)
            } else {

                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)

                val signUpButton = findViewById<TextView>(R.id.signup)
                val logInButton = findViewById<Button>(R.id.login)

                signUpButton.setOnClickListener {
                    val signUpIntent = Intent(this@MainActivity, SignUp::class.java)
                    startActivity(signUpIntent)
                }
                logInButton.setOnClickListener {
                    val logInIntent = Intent(this@MainActivity, LogIn::class.java)
                    startActivity(logInIntent)
                }
            }
        }
    }
}