package com.example.chatroom

import android.content.Context
import android.content.SharedPreferences


class LoginSettings(context: Context) {

    private val preferenceName = "loginState"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun login(username: String, password: String) {
        editor.putString("username", username)
        editor.putString("password", password)
        editor.putBoolean("loginState", true)
        editor.apply()
    }

    fun logout(username: String, password: String) {
        editor.clear()
        editor.apply()
    }

    fun getUsername(): String? {
        return sharedPref.getString("username", null)
    }

    fun getUserInfo(): Pair<String?, String?> {
        val username = sharedPref.getString("username", null)
        val password = sharedPref.getString("password", null)
        return Pair(username, password)
    }

    fun getState(): Boolean {
        return sharedPref.getBoolean("loginState", false)
    }
}
