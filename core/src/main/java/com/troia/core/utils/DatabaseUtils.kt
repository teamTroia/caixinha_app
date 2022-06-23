package com.troia.core.utils

import com.troia.core.types.Purchase

object DatabaseUtils {
    interface IDatabaseAccess {
        fun getAllPurchases(): List<Purchase>
        fun insertPurchase(purchase: Purchase)
    }

    private lateinit var adapter: IDatabaseAccess

    fun init(adapter: IDatabaseAccess){
        this.adapter = adapter
    }

    fun getAllPurchases(): List<Purchase> = adapter.getAllPurchases()
    fun insertPurchase(purchase: Purchase) = adapter.insertPurchase(purchase)

}
