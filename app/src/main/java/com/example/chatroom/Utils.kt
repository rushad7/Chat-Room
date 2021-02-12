package com.example.chatroom

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

object Utils {

    private val gson = Gson()

    suspend fun checkUserExists(username: String): HttpResponse {
        val httpClient = HttpClient(Android) {
            engine {
                connectTimeout = 2000
                socketTimeout = 2000
            }
        }

        return httpClient.post("http://192.168.0.7:8000/checkuser") {
            val userinfo = UserInfo(username = username)
            val postData = gson.toJson(userinfo)
            body = postData
        }
    }
}