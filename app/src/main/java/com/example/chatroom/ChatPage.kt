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

    private lateinit var chatBoxField: TextView
    private lateinit var sendChatButton: Button
    private lateinit var chatView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_page)

        loginSettings = LoginSettings(this@ChatPage)

        chatBoxField = findViewById(R.id.chatBoxField)
        sendChatButton = findViewById(R.id.sendChatButton)
        chatView = findViewById(R.id.chatView)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        chatWebSocket("global")

        sendChatButton.setOnClickListener {

            val broadcastMessage = "${loginSettings.getUsername().toString()}: ${chatBoxField.text}"
            val displayMessage = "Me: ${chatBoxField.text}"

            launch {
                list.add(displayMessage)
                arrayAdapter.notifyDataSetChanged()
                chatView.adapter = arrayAdapter
                chatWebSocket("global", broadcastMessage)
            }
            chatBoxField.text = ""
        }
    }

    private fun chatWebSocket(room: String, message: String? = null) {

        val userCredentials: Pair<String?, String?> = loginSettings.getUserInfo()
        val username = userCredentials.first.toString()
        val password = userCredentials.second.toString()
        val hash = "${Utils.sha512(username)}$password"

        val client = OkHttpClient()
        val request: Request = Request.Builder().url("ws://chat-service.herokuapp.com/chat/${room}/${hash}").build()

        val webSocketListener: WebSocketListener = object : WebSocketListener() {

            override fun onMessage(webSocket: WebSocket, text: String) {
                runOnUiThread(Runnable {
                    list.add(text)
                    arrayAdapter.notifyDataSetChanged()
                    chatView.adapter = arrayAdapter
                })
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {

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
                    Toast.makeText(
                        this@ChatPage,
                        "Connection failed, message will be sent when you are connected to the internet",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }
        }

        val chatClient = client.newWebSocket(request, webSocketListener)
        if (message != null) {
            chatClient.send("$message")
            runOnUiThread(Runnable {
                chatView.transcriptMode = ListView.TRANSCRIPT_MODE_NORMAL
                chatView.isStackFromBottom = true
            })
        }
    }
}