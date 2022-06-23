package com.troia.core.utils

import com.troia.core.types.User

object UserUtils {
    private var user: User? = null

    fun userName() = user?.name

    fun userEmail() = user?.email

    fun userCleanEmail() = user?.email?.clean()

    fun String.clean(): String {
        return this.replace(".","dot").replace("@","at")
    }

    fun setUser(user: User) {
        this.user = user
    }

    fun isUserAdmin(): Boolean {
        return user?.admin ?: false
    }
}