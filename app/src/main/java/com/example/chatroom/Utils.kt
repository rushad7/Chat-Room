package com.example.chatroom

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.security.MessageDigest

object Utils {

    private val gson = Gson()

    //USER CREDENTIALS CLASS
    data class UserInfo(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String
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

    //COMMON REQUEST METHOD
    suspend fun sendRequest(requestAction: String, username: String, password: String): HttpResponse {
        val httpClient = HttpClient(Android) {
            engine {
                connectTimeout = 2000
                socketTimeout = 2000
            }
        }

        val url = "http://192.168.0.7:8000/$requestAction"

        return httpClient.post(url) {
            val userinfo = UserInfo(username = username, password = password)
            val postData = gson.toJson(userinfo)
            body = postData
        }
    }
}