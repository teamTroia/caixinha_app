package com.troia.core.utils

import com.troia.core.types.Product
import com.troia.core.types.Purchase
import com.troia.core.types.UserProduct

object FirebaseUtils {

    interface FirebaseUtilsAdapter {
        fun saveProduct(products: Product)
        fun saveUserCart(user: String, products: ArrayList<UserProduct>)
        fun getAllProducts()
        fun getUserCart(user: String)
        fun clearUserCart(user: String)
        fun validateEmail(email: String)
        fun getUserData(email: String)
        fun registerUser(email: String, name: String, pass: String)
        fun savePurchase(user:String, purchase: Purchase)
        fun getPurchases(user: String)
    }

    lateinit var adapter: FirebaseUtilsAdapter
    fun init(adapter: FirebaseUtilsAdapter) {
        this.adapter = adapter
    }

    fun getUserCart(user: String) = adapter.getUserCart(user)
    fun saveProduct(product: Product) = adapter.saveProduct(product)
    fun saveUserCart(user: String, products: ArrayList<UserProduct>) = adapter.saveUserCart(user, products)
    fun getAllProducts() = adapter.getAllProducts()
    fun validateEmail(email:String) = adapter.validateEmail(email)
    fun getUserData(email:String) = adapter.getUserData(email)
    fun registerUser(email: String, name: String, pass: String) = adapter.registerUser(email, name, pass)
    fun clearUserCart(user: String) = adapter.clearUserCart(user)
    fun savePurchase(user:String, purchase: Purchase) = adapter.savePurchase(user,purchase)
    fun getPurchases(user: String) = adapter.getPurchases(user)
}