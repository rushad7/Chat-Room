package com.example.chatroom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
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
}