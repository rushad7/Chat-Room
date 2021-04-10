package com.example.chatroom

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.security.MessageDigest


object Utils {

    private const val BASE_URL = "https://chat-service.herokuapp.com"
    private val gson = Gson()

    //USER CREDENTIALS CLASS
    data class UserCredentials(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String
    )

    //ROOM DATA CLASS
    data class Room(
        @SerializedName("name") val name: String,
        @SerializedName("creator") val creator: String
    )

    //JOIN ROOM DATA CLASS
    data class JoinRoom(
        @SerializedName("roomname") val roomname: String,
        @SerializedName("username") val username: String,
        @SerializedName("description") val description: String?
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

        val url = "${BASE_URL}/${requestAction}"

        return httpClient.post(url) {
            val userinfo = UserCredentials(username, password)
            val postData = gson.toJson(userinfo)
            body = postData
        }
    }


    //CREATE ROOM REQUEST METHOD
    suspend fun createRoom(roomname: String, creator: String): HttpResponse {
        val httpClient = HttpClient(Android) {
            engine {
                connectTimeout = 2000
                socketTimeout = 2000
            }
        }

        val url = "${BASE_URL}/create-room"
        return httpClient.post(url) {
            val roominfo = Room(roomname, creator)
            val postData = gson.toJson(roominfo)
            body = postData
        }
    }


    suspend fun  pingServer(): HttpResponse {
        val httpClient = HttpClient(Android) {
            engine {
                connectTimeout = 2000
                socketTimeout = 2000
            }
        }
        val url = "${BASE_URL}/ping"
        return httpClient.get(url)
    }


    suspend fun requestRoomJoin(roomname: String, username: String, description: String?): HttpResponse {
        val httpClient = HttpClient(Android) {
            engine {
                connectTimeout = 2000
                socketTimeout = 2000
            }
        }

        val url = "${BASE_URL}/join-room"
        return httpClient.post(url) {
            val roominfo = JoinRoom(roomname, username, description)
            val postData = gson.toJson(roominfo)
            body = postData
        }
    }
}