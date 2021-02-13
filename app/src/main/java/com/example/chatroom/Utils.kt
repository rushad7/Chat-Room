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

    data class UserInfo(
        @SerializedName("username") val username: String? = null,
        @SerializedName("password") val password: String? = null
    )

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

    fun hashString(type: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }
}