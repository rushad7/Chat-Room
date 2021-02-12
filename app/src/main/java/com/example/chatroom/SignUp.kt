package com.example.chatroom

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class SignUp : AppCompatActivity(), CoroutineScope {

    //START COROUTINE
    private val gson = Gson()
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
            val password = passwordField.text.toString()

            launch {
                try {
                    val userExists = Utils.checkUserExists(username).content.toString().toInt()
                    if (userExists == 1) {
                        signup(username, password)
                        Toast.makeText(applicationContext, "Registration completed successfully !", Toast.LENGTH_LONG)
                            .show()
                    }

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

    //SIGNUP METHOD
    private suspend fun signup(username: String, password: String): HttpResponse {
        val httpClient = HttpClient(Android) {
            engine {
                connectTimeout = 2000
                socketTimeout = 2000
            }
        }

        return httpClient.post("http://192.168.0.7:8000/adduser") {
            val userinfo = UserInfo(username = username, password = password)
            val postData = gson.toJson(userinfo)
            body = postData
        }
    }
}