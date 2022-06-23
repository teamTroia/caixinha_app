package com.troia.core.utils

import com.troia.core.types.Product
import com.troia.core.types.UserProduct

object FirebaseUtils {

    interface FirebaseUtilsAdapter {
        fun save_product(products: Product)
        fun save_user_cart(user: String, products: ArrayList<UserProduct>)
        fun get_all_products()
        fun get_quantity_for_user(user: String)
        fun validate_email(email: String)
        fun get_user_data(email: String)
        fun register_user(email: String, name: String, pass: String)
    }

    lateinit var adapter: FirebaseUtilsAdapter
    fun init(adapter: FirebaseUtilsAdapter) {
        this.adapter = adapter
    }

    fun get_quantity_for_user(user: String) = adapter.get_quantity_for_user(user)
    fun save_product(product: Product) = adapter.save_product(product)
    fun save_user_cart(user: String, products: ArrayList<UserProduct>) = adapter.save_user_cart(user, products)
    fun get_all_products() = adapter.get_all_products()
    fun validate_email(email:String) = adapter.validate_email(email)
    fun get_user_data(email:String) = adapter.get_user_data(email)
    fun register_user(email: String, name: String, pass: String) = adapter.register_user(email, name, pass)
}