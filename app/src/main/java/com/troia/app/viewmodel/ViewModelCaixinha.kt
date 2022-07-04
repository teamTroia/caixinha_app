package com.troia.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.troia.core.database.DataObserver
import com.troia.core.database.NotificationType
import com.troia.core.types.UserProduct
import com.troia.core.types.Purchase
import com.troia.core.utils.FirebaseUtils
import com.troia.core.utils.UserUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ViewModelCaixinha: ViewModel(), DataObserver {
    val updatedProductsList = MutableLiveData(false)
    val updatedPurchaseList = MutableLiveData(false)
    var creatingNewList = false

    fun getProductsList(search: String): ArrayList<UserProduct> {
        return UserUtils.productsList.filter { it.name.lowercase().contains(search.lowercase()) } as ArrayList<UserProduct>
    }

    fun getTotal(): Double {
        return UserUtils.productsList.sumOf {
            it.getTotal()
        }
    }

    fun checkout() {
        viewModelScope.launch(Dispatchers.IO) {
            val purchase = Purchase().apply {
                date = Date()
                value = getTotal()
                debt = value
            }
            UserUtils.addPurchase(purchase)
            UserUtils.resetList()
            saveProducts()
            updatedProductsList.postValue(true)
            updatedPurchaseList.postValue(true)
        }
    }

    override fun notify(type: NotificationType, data: Any?) {
        when (type) {
            NotificationType.PurchasesLoad -> {
                updatedPurchaseList.postValue(true)
            }
            NotificationType.ProductsLoad -> {
                createNewList()
            }
            NotificationType.UserCartLoad -> {
                val load = data as Boolean
                if(load) {
                    updatedProductsList.postValue(true)
                } else {
                    creatingNewList = true
                    createNewList()
                }
            }
            else -> {}
        }
    }

    fun createNewList() {
        if(creatingNewList) {
            UserUtils.createList()
            saveProducts()
            updatedProductsList.postValue(true)
            creatingNewList = false
        }
    }

    fun getNewList() {
        UserUtils.userCleanEmail()?.let { FirebaseUtils.clearUserCart(it) }
        UserUtils.productsList.clear()
        creatingNewList = true
        createNewList()
    }

    fun saveProducts(){
        UserUtils.userCleanEmail()?.let { FirebaseUtils.saveUserCart(it,UserUtils.productsList) }
    }

    fun getPurchaseList(): ArrayList<Purchase> {
        return ArrayList<Purchase>().apply{
            addAll(UserUtils.purchasesList)
        }
    }
}