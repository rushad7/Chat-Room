package com.example.chatroom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val loginSettings = LoginSettings(this@MainActivity)
        val isLoggedIn = loginSettings.getState()
        val roomLobbyIntent = Intent(this@MainActivity, RoomLobby::class.java)

            try {
                if (isLoggedIn) {
                    val userCredentials = loginSettings.getUserInfo()
                    val username = userCredentials.first
                    val password = userCredentials.second

                        if (username != null && password != null) {
                            startActivity(roomLobbyIntent)
                        }

                } else {
                    setContentView(R.layout.activity_main)

                    val signUpButton = findViewById<TextView>(R.id.signup)
                    val logInButton = findViewById<Button>(R.id.login)

                    signUpButton.setOnClickListener {
                        val signUpIntent = Intent(this, SignUp::class.java)
                        startActivity(signUpIntent)
                    }
                    logInButton.setOnClickListener {
                        val logInIntent = Intent(this, LogIn::class.java)
                        startActivity(logInIntent)
                    }
                }
            } catch (e: Exception) {
                setContentView(R.layout.network_error)
        }
    }
}