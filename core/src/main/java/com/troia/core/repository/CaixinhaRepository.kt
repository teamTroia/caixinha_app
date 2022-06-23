package com.troia.core.repository

import com.troia.core.database.DataNotifier
import com.troia.core.database.NotificationType
import com.troia.core.types.*
import com.troia.core.utils.DatabaseUtils
import com.troia.core.utils.FirebaseUtils
import com.troia.core.utils.UserUtils

object CaixinhaRepository {
    val productsList: ArrayList<UserProduct> = arrayListOf()
    val purchasesList: ArrayList<Purchase> = arrayListOf()

    fun loadProducts(user:String) {
        FirebaseUtils.get_quantity_for_user(user)
    }

    fun setList(list: ArrayList<UserProduct>){
        productsList.clear()
        productsList.addAll(list)
    }

    fun createList() {
        ProductsRepository.get_products().forEach {
            productsList.add(UserProduct().apply {
                name = it.name
                quantity = 0
                price = it.price
            })
        }
    }

    fun resetList() {
        productsList.forEach {
            it.quantity = 0
        }
    }

    fun addPurchase(purchase: Purchase){
        purchasesList.add(purchase)
        DatabaseUtils.insertPurchase(purchase)
    }

    fun loadPurchasesFromDatabase(){
        purchasesList.clear()
        purchasesList.addAll(DatabaseUtils.getAllPurchases())
        DataNotifier.notifyData(NotificationType.ProductsLoad)
    }
}