package com.example.chatroom

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class ChatPage : AppCompatActivity(), CoroutineScope {

    //START COROUTINE
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var list: ArrayList<String> = ArrayList()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_page)

        val loginSettings = LoginSettings(this)
        Toast.makeText(applicationContext, "Welcome ${loginSettings.getUsername()}", Toast.LENGTH_LONG).show()

        val chatBoxField = findViewById<TextView>(R.id.chatBoxField)
        val sendChatButton = findViewById<Button>(R.id.sendChatButton)
        val chatView = findViewById<ListView>(R.id.chatView)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        sendChatButton.setOnClickListener {
            val currentDateTime = LocalDateTime.now()
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val dateTime = currentDateTime.format(dateTimeFormatter).toString()
            val message = chatBoxField.text.toString()

            launch {
                try {
                    val response =
                        Utils.sendMessage(loginSettings.getUsername().toString(), dateTime, message).readText()
                            .toBoolean()
                    if (!response) {
                        Toast.makeText(
                            this@ChatPage,
                            "Unable to send message, please try again later",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@ChatPage,
                        "Unable to send message, check your internet connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            list.add(message)
            chatBoxField.text = ""
            arrayAdapter.notifyDataSetChanged()
            chatView.adapter = arrayAdapter
        }
    }
}