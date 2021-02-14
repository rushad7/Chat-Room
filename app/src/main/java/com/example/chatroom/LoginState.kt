package com.example.chatroom

import android.content.Context
import android.content.SharedPreferences


class LoginState(context: Context) {

    private val preferenceName = "loginState"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    fun login() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean("loginState", true)
        editor.apply()
    }

    fun logout() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean("loginState", false)
        editor.apply()
    }

    fun getState(): Boolean {
        return sharedPref.getBoolean("loginState", false)
    }
}
