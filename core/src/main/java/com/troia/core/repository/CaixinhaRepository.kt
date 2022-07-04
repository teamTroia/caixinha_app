package com.troia.core.repository

import com.troia.core.types.User

object CaixinhaRepository {
    val membersList: ArrayList<User> = arrayListOf()

    fun setMembersList(list: ArrayList<User>) {
        membersList.clear()
        membersList.addAll(list)
    }
}