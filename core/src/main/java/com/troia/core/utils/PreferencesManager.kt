package com.troia.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

object PreferencesManager {
    private lateinit var prefs: SharedPreferences
    private lateinit var prefsEditor: SharedPreferences.Editor
    const val PREFS_NAME = "TROIA_PREFS"
    const val PREFS_MODE = Context.MODE_PRIVATE

    const val DOWNLOAD_DATE = "keyDownloadDate"
    const val USER_EMAIL = "keyUserEmail"
    const val USER_PASS = "keyUserPass"

    fun initialize (context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, PREFS_MODE)
        prefsEditor = prefs.edit()
    }

    fun eraseLogin() {
        prefsEditor.remove(USER_EMAIL)
        prefsEditor.apply()
        prefsEditor.remove(USER_PASS)
        prefsEditor.apply()
    }

    fun setLogin(email: String, pass:String) {
        prefsEditor.putString(USER_EMAIL,email)
        prefsEditor.apply()
        prefsEditor.putString(USER_PASS,pass)
        prefsEditor.apply()
    }

    fun getLogin(): Pair<String?, String?> {
        val email = prefs.getString(USER_EMAIL, null)
        val pass = prefs.getString(USER_PASS, null)
        return Pair(email, pass)
    }

    @SuppressLint("SimpleDateFormat")
    fun setDownloadDate(date: Date){
        val formater = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        prefsEditor.putString(DOWNLOAD_DATE,formater.format(date))
        prefsEditor.apply()
    }

    @SuppressLint("SimpleDateFormat")
    fun getDownloadDate(): Date? {
        val formater = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        prefs.getString(DOWNLOAD_DATE, null)?.let {
            return formater.parse(it)
        }
        return null
    }
}