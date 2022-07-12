package com.troia.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troia.core.database.DataObserver
import com.troia.core.database.NotificationType
import com.troia.core.repository.CaixinhaRepository
import com.troia.core.types.User
import com.troia.core.utils.FirebaseUtils
import com.troia.core.utils.UserUtils.clean

class MembersViewModel: ViewModel(), DataObserver {
    val updatedMembers = MutableLiveData(false)

    fun getMembersList(): ArrayList<User> {
        return ArrayList(
            CaixinhaRepository.membersList
        )
    }

    fun getAllPurchases() {
        CaixinhaRepository.membersList.forEach {
            it.email?.clean()?.let { email ->
                FirebaseUtils.getPurchases(email)
            }
        }
    }

    fun closeAll() {

    }

    override fun notify(type: NotificationType, data: Any?) {
        when (type) {
            NotificationType.MembersLoaded -> {
                val loaded = data as Boolean
                updatedMembers.postValue(loaded)
            }
            else -> {}
        }
    }


}