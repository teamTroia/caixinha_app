package com.troia.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.troia.core.database.DataObserver
import com.troia.core.repository.CaixinhaRepository
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

    fun loadProductsList() {
        loadPurchasesHistoryFromDB()
        UserUtils.userCleanEmail()?.let { CaixinhaRepository.loadProducts(it) }
    }

    private fun loadPurchasesHistoryFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            CaixinhaRepository.loadPurchasesFromDatabase()
        }
    }

    fun getProductsList(search: String): ArrayList<UserProduct> {
        return CaixinhaRepository.productsList.filter { it.name.lowercase().contains(search.lowercase()) } as ArrayList<UserProduct>
    }

    fun getTotal(): Double {
        return CaixinhaRepository.productsList.sumOf {
            it.getTotal()
        }
    }

    fun checkout() {
        viewModelScope.launch(Dispatchers.IO) {
            val purchase = Purchase(
                Date(),
                getTotal()
            )
            CaixinhaRepository.addPurchase(purchase)
            CaixinhaRepository.resetList()
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
                if(creatingNewList) {
                    CaixinhaRepository.createList()
                    saveProducts()
                    updatedProductsList.postValue(true)
                }
            }
            NotificationType.QuantityLoad -> {
                val load = data as Boolean
                if(load) {
                    updatedProductsList.postValue(true)
                } else {
                    creatingNewList = true
                    FirebaseUtils.get_all_products()
                }
            }
            else -> {}
        }
    }

    fun getNewList() {
        //TODO implementar
    }

    fun saveProducts(){
        UserUtils.userCleanEmail()?.let { FirebaseUtils.save_user_cart(it,CaixinhaRepository.productsList) }
    }

    fun getPurchaseList(): ArrayList<Purchase> {
        return ArrayList<Purchase>().apply{
            addAll(CaixinhaRepository.purchasesList)
        }
    }
}