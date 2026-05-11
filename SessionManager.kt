package com.example.mahasale

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME      = "MahaSaleSession"
        private const val KEY_IS_LOGIN   = "isLogin"
        private const val KEY_NAMA       = "nama"
        private const val KEY_EMAIL      = "email"
        private const val KEY_PASSWORD   = "password"
        private const val KEY_REGISTERED = "isRegistered"
        private const val KEY_ALAMAT     = "alamat"
        private const val KEY_HAS_ALAMAT = "hasAlamat"
    }

    fun register(nama: String, email: String, password: String) {
        prefs.edit().apply {
            putString(KEY_NAMA, nama)
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            putBoolean(KEY_REGISTERED, true)
            apply()
        }
    }

    fun login(email: String, password: String): Boolean {
        val savedEmail    = prefs.getString(KEY_EMAIL, "")
        val savedPassword = prefs.getString(KEY_PASSWORD, "")
        return if (email == savedEmail && password == savedPassword) {
            prefs.edit().putBoolean(KEY_IS_LOGIN, true).apply()
            true
        } else false
    }

    fun saveAlamat(alamat: String) {
        prefs.edit()
            .putString(KEY_ALAMAT, alamat)
            .putBoolean(KEY_HAS_ALAMAT, true)
            .apply()
    }

    fun isLoggedIn()   : Boolean = prefs.getBoolean(KEY_IS_LOGIN, false)
    fun isRegistered() : Boolean = prefs.getBoolean(KEY_REGISTERED, false)
    fun hasAlamat()    : Boolean = prefs.getBoolean(KEY_HAS_ALAMAT, false)
    fun getNama()      : String  = prefs.getString(KEY_NAMA, "User") ?: "User"
    fun getEmail()     : String  = prefs.getString(KEY_EMAIL, "") ?: ""
    fun getAlamat()    : String  = prefs.getString(KEY_ALAMAT, "") ?: ""

    fun clearSession() {
        prefs.edit().putBoolean(KEY_IS_LOGIN, false).apply()
    }
}
