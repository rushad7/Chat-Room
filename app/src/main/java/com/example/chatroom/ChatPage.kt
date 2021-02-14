package com.example.chatroom

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChatPage: AppCompatActivity() {

    private var list: ArrayList<String> = ArrayList()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_page)

        val chatBoxField = findViewById<TextView>(R.id.chatBoxField)
        val sendChatButton = findViewById<Button>(R.id.sendChatButton)
        val chatView = findViewById<ListView>(R.id.chatView)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        sendChatButton.setOnClickListener {
            list.add(chatBoxField.text.toString())
            chatBoxField.text = ""
            arrayAdapter.notifyDataSetChanged()
            chatView.adapter = arrayAdapter
        }
    }
}