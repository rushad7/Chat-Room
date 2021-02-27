package com.example.chatroom

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.websocket.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.network.selector.*
import kotlinx.coroutines.*
import okhttp3.*
import kotlin.coroutines.CoroutineContext

class ChatPage : AppCompatActivity(), CoroutineScope {

    //START COROUTINE
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var list: ArrayList<String> = ArrayList()
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var loginSettings: LoginSettings

    private val client = OkHttpClient()
    private val request: Request = Request.Builder().url("ws://192.168.0.7:8000/chat").build()

    private lateinit var chatBoxField: TextView
    private lateinit var sendChatButton: Button
    private lateinit var chatView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_page)

        loginSettings = LoginSettings(this@ChatPage)
        Toast.makeText(applicationContext, "Welcome ${loginSettings.getUsername()}", Toast.LENGTH_LONG).show()

        chatBoxField = findViewById(R.id.chatBoxField)
        sendChatButton = findViewById(R.id.sendChatButton)
        chatView = findViewById(R.id.chatView)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        chatWebSocket(loginSettings.getUsername().toString())

        sendChatButton.setOnClickListener {

            val message = chatBoxField.text.toString()

            launch {
                try {
                    chatWebSocket(loginSettings.getUsername().toString(), message)
                } catch (e: Exception) {
                    Toast.makeText(
                        this@ChatPage,
                        "Oops! Something went wrong, check your internet connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            chatBoxField.text = ""
        }
    }

    private fun chatWebSocket(username: String, message: String? = null) {

        val webSocketListenerCoinPrice: WebSocketListener = object : WebSocketListener() {

            override fun onMessage(webSocket: WebSocket, text: String) {
                runOnUiThread(Runnable {
                    list.add(text)
                    arrayAdapter.notifyDataSetChanged()
                    chatView.adapter = arrayAdapter
                })
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {

                val dateTime = Utils.dateTimeString()
                webSocket.send("$username $dateTime $message Closing Connection")

                runOnUiThread(Runnable {
                    Toast.makeText(this@ChatPage, "Closing Connection", Toast.LENGTH_SHORT).show()
                })
                webSocket.close(1000, null)
                webSocket.cancel()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                runOnUiThread(Runnable {
                    Toast.makeText(this@ChatPage, "Connection closed : Code $code", Toast.LENGTH_SHORT).show()
                })
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                runOnUiThread(Runnable {
                    Toast.makeText(this@ChatPage, "Connection Failed", Toast.LENGTH_SHORT).show()
                })
            }
        }

        val chatClient = client.newWebSocket(request, webSocketListenerCoinPrice)
        if (message != null) chatClient.send(message)
        //client.dispatcher.executorService.shutdown()
    }
}