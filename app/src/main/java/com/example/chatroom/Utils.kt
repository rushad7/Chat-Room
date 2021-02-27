package com.example.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {

    private val gson = Gson()

    //USER CREDENTIALS CLASS
    data class UserCredentials(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String
    )

    data class Message(
        @SerializedName("sent_by") val username: String,
        @SerializedName("date_time") val date_time: String,
        @SerializedName("message") val message: String
    )

    //SHA-512 ENCODER METHOD
    fun sha512(input: String): String {
        val chars = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance("SHA-512")
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(chars[i shr 4 and 0x0f])
            result.append(chars[i and 0x0f])
        }

        return result.toString()
    }

    //COMMON LOG IN/OUT REQUEST METHOD
    suspend fun sendRequest(requestAction: String, username: String, password: String): HttpResponse {
        val httpClient = HttpClient(Android) {
            engine {
                connectTimeout = 2000
                socketTimeout = 2000
            }
        }

        val url = "http://192.168.0.7:8000/$requestAction"

        return httpClient.post(url) {
            val userinfo = UserCredentials(username, password)
            val postData = gson.toJson(userinfo)
            body = postData
        }
    }

    //POST MESSAGE METHOD
    suspend fun sendMessage(username: String, date_time: String, message: String): HttpResponse {
        val httpClient = HttpClient(Android) {
            engine {
                connectTimeout = 2000
                socketTimeout = 2000
            }
        }

        return httpClient.post("http://192.168.0.7:8000/sendmessage") {
            val messageObj = Message(username, date_time, message)
            val postData = gson.toJson(messageObj)
            body = postData
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateTimeString(): String {
        val currentDateTime = LocalDateTime.now()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return currentDateTime.format(dateTimeFormatter).toString()
    }
}