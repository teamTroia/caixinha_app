package com.troia.core.utils

import com.troia.core.repository.ProductsRepository
import com.troia.core.types.Purchase
import com.troia.core.types.User
import com.troia.core.types.UserProduct

object UserUtils {
    val productsList: ArrayList<UserProduct> = arrayListOf()
    val purchasesList: ArrayList<Purchase> = arrayListOf()
    private var user: User? = null

    fun setProductsList(list: ArrayList<UserProduct>){
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

    fun addPurchase(purchase: Purchase){
        purchasesList.add(purchase)
        userCleanEmail()?.let { FirebaseUtils.savePurchase(it,purchase) }
    }

    fun setPurchaseList(list: ArrayList<Purchase>){
        purchasesList.clear()
        purchasesList.addAll(list)
    }

    fun resetList() {
        productsList.forEach {
            it.quantity = 0
        }
    }

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