package com.mellnahus.c23pc613.capstoneteam.agricheckapp.helper

import android.content.Context
import android.content.SharedPreferences
import com.mellnahus.c23pc613.capstoneteam.agricheckapp.model.LoginResponse

class UserPreferences(context: Context) {

    private val pref = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)

    // Menyimpan data sesi pengguna
    fun setSession(username: String) {

        val edit = pref.edit()
        edit.putString("username", username)
        edit.apply()

    }

    // Menghapus sesi pengguna
    fun clearSession() {
        val edit = pref.edit()
        edit.clear()
        edit.apply()
    }

    fun isLogin(): Boolean {
        return pref.contains("username")
    }

    fun getUsername(): String? {
        return pref.getString("username", null)
    }

    fun logout(){
        val edit = pref.edit()
        edit.remove("username")
        edit.apply()
    }

    companion object {
        private const val MESSAGE = "message"
        private const val UID = "uid"
    }
}