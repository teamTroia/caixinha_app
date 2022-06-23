package com.troia.core.repository

import com.troia.core.types.Product
import com.troia.core.utils.UserUtils

object ProductsRepository {
    private val productsFBList: ArrayList<Product> = arrayListOf()

    fun addProduct(products: ArrayList<Product>) {
        productsFBList.clear()
        productsFBList.addAll(products)
    }

    fun get_products(): ArrayList<Product> {
        return productsFBList
    }
}