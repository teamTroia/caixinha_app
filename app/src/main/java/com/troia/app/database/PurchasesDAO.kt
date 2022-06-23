package com.troia.app.database

import androidx.room.*
import com.troia.core.types.Purchase

@Dao
interface PurchasesDAO{

    @Query("SELECT * FROM purchases_history")
    fun getPurchases(): List<Purchase>

    @Insert
    fun insertPurchase(vararg purchase: Purchase)

    @Delete
    fun deletePurchase(purchase: Purchase)

    @Update
    fun updatePurchase(vararg purchase: Purchase)
}