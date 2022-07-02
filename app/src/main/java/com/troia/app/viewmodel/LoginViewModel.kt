package com.troia.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troia.core.database.DataObserver

import com.troia.core.database.NotificationType
import com.troia.core.utils.FirebaseUtils
import java.security.MessageDigest

class LoginViewModel: ViewModel(), DataObserver {

    val userValidateResult = MutableLiveData<Pair<Boolean, String>?>(null)
    val userData = MutableLiveData<Triple<String, String, String>?>(null)
    fun validateEmail(email: String) {
        FirebaseUtils.validateEmail(email.clean())
    }

    fun register(name:String, email:String, pass:String): Boolean {
        val password = encrypt(pass)
        FirebaseUtils.registerUser(email.clean(), name, password)
        return true
    }

    fun String.clean(): String {
        return this.replace(".","dot").replace("@","at")
    }

    fun encrypt(pass: String): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val result = digest.digest(pass.toByteArray(Charsets.UTF_8))
        val sb = StringBuilder()
        for (b in result) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }

    fun getUserData(email: String) {
        FirebaseUtils.getUserData(email.clean())
    }

    override fun notify(type: NotificationType, data: Any?) {
        when (type) {
            NotificationType.EmailVerification -> {
                val valid = data as Boolean
                if(valid) {
                    userValidateResult.postValue(Pair(valid,""))
                } else {
                    userValidateResult.postValue(Pair(valid,"Email já cadastrado"))
                }
            }
            NotificationType.UserData -> {
                if (data == null) {
                    userData.postValue(null)
                } else {
                    val user = data as Triple<String, String, String>
                    userData.postValue(user)
                }
            }
            else -> {}
        }
    }
}