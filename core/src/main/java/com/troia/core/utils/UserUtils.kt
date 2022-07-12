package com.troia.core.utils

import com.troia.core.repository.CaixinhaRepository
import com.troia.core.repository.ProductsRepository
import com.troia.core.types.Purchase
import com.troia.core.types.User
import com.troia.core.types.UserProduct

object UserUtils {
    val productsList: ArrayList<UserProduct> = arrayListOf()
    private var user: User? = null

    fun getUser() = user

    fun setProductsList(list: ArrayList<UserProduct>){
        productsList.clear()
        productsList.addAll(list)
    }

    fun getPurchases(): ArrayList<Purchase> {
        return userEmail()?.let { CaixinhaRepository.getPurchases(it) } ?: run { arrayListOf() }
    }

    fun createList() {
        ProductsRepository.getProducts().forEach {
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