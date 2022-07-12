package com.troia.core.repository

import com.troia.core.types.Product
import com.troia.core.types.Purchase
import com.troia.core.utils.FirebaseUtils
import com.troia.core.utils.UserUtils
import com.troia.core.utils.UserUtils.clean

object ProductsRepository {
    private val productsFBList: ArrayList<Product> = arrayListOf()

    fun addProduct(products: ArrayList<Product>) {
        productsFBList.clear()
        productsFBList.addAll(products)
    }

    fun getProducts(): ArrayList<Product> {
        return productsFBList
    }

    fun deleteProduct(product: Product) {
        productsFBList.remove(product)
        FirebaseUtils.removeProduct(product)
    }
}