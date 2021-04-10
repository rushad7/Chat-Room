package com.example.chatroom

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.create_room_dialog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class Room : AppCompatActivity(), CoroutineScope {

    //START COROUTINE
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var loginSettings: LoginSettings

    private lateinit var title: TextView

    private lateinit var joinGlobalRoom: Button
    private lateinit var joinRoom: Button
    private lateinit var createRoom: Button
    private lateinit var logoutButton: Button

    private lateinit var roomnameField: TextView
    private lateinit var createRoomButton: Button
    private lateinit var joinRoomButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        loginSettings = LoginSettings(this@Room)

        joinGlobalRoom = findViewById(R.id.globalChatButton)
        joinRoom = findViewById(R.id.joinRoomButton)
        createRoom = findViewById(R.id.createRoomButton)
        logoutButton = findViewById(R.id.logoutButton)

        val loginSettings = LoginSettings(this@Room)

        joinGlobalRoom.setOnClickListener {
            val globalChatIntent = Intent(this@Room, ChatPage::class.java)
            startActivity(globalChatIntent)
        }

        createRoom.setOnClickListener {
            launch {
                createRoomDialog()
            }
        }

        joinRoom.setOnClickListener {
            launch {
                joinRoomDialog()
            }
        }

        logoutButton.setOnClickListener {
            loginSettings.logout()
            val homeIntent = Intent(this@Room, MainActivity::class.java)
            startActivity(homeIntent)
        }
    }

    private fun createRoomDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.create_room_dialog)
        dialog.window?.setLayout(900, 900)

        roomnameField = dialog.findViewById(R.id.roomname)
        createRoomButton = dialog.findViewById(R.id.create_new_room_button)

        createRoomButton.setOnClickListener {
            launch {
                val roomname = roomnameField.text.toString()
                val creator = loginSettings.getUsername().toString()
                Utils.createRoom(roomname, creator)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun joinRoomDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.create_room_dialog)

        roomnameField = dialog.findViewById(R.id.roomname)
        joinRoomButton = dialog.findViewById(R.id.create_new_room_button)

        joinRoomButton.text = "REQUEST"
        title = dialog.findViewById(R.id.title)
        title.text = "REQUEST TO JOIN ROOM"

        dialog.window?.setLayout(900, 900)

        joinRoomButton.setOnClickListener {
            launch {
                val roomname = roomnameField.text.toString()
                val username = loginSettings.getUsername().toString()
                Utils.requestRoomJoin(roomname, username, description = null)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}