package com.troia.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troia.core.database.DataObserver
import com.troia.core.database.NotificationType
import com.troia.core.repository.CaixinhaRepository
import com.troia.core.types.Purchase
import com.troia.core.types.User
import com.troia.core.utils.UserUtils
import com.troia.core.utils.UserUtils.clean

class HistoryViewModel: ViewModel(), DataObserver {
    val updatedPurchaseList = MutableLiveData(false)
    private var user: User? = null

    fun setUser(user: User?) {
        this.user = user
    }

    fun getPurchaseList(): ArrayList<Purchase> {
        user?.email?.clean()?.let {
            return ArrayList<Purchase>().apply{
                addAll(CaixinhaRepository.getPurchases(it))
            }
        }
        return arrayListOf()
    }

    override fun notify(type: NotificationType, data: Any?) {
        when (type) {
            NotificationType.PurchasesLoad -> {
                updatedPurchaseList.postValue(true)
            }
            else -> {}
        }
    }
}