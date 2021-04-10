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


class SignUp : AppCompatActivity(), CoroutineScope {

    //START COROUTINE
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        val usernameField = findViewById<TextView>(R.id.usernameField)
        val passwordField = findViewById<TextView>(R.id.passwordField)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        //REGISTER USER ON BUTTON CLICK
        signUpButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = Utils.sha512(passwordField.text.toString())

            //CHECK FIELD IS NOT EMPTY
            if (username.isEmpty() or (" " in username) or (password.length < 8)) {
                Toast.makeText(applicationContext, "Please enter a valid username and password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                launch {
                    try {
                        //SIGNUP IF USER DOES NOT EXIST
                        val status = Utils.sendRequest("signup", username, password).readText().toBoolean()
                        if (status) {
                            Toast.makeText(
                                applicationContext,
                                "Registration completed successfully !",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            val loginIntent = Intent(this@SignUp, LogIn::class.java)
                            startActivity(loginIntent)

                        } else {
                            Toast.makeText(applicationContext, "Username already exists", Toast.LENGTH_SHORT)
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
}