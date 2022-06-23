package com.troia.app.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.troia.core.types.Purchase
import com.troia.core.utils.DatabaseUtils

object DatabaseAccess: DatabaseUtils.IDatabaseAccess {
    private lateinit var database: AppDatabase

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE purchases_history(id INTEGER NOT NULL PRIMARY KEY, value REAL NOT NULL, date INTEGER NOT NULL)")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE product_list")
        }
    }

    fun initialize(context: Context){
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "products_db"
        ).addMigrations(
            MIGRATION_1_2,
            MIGRATION_2_3,
        ).build()
        DatabaseUtils.init(this)
    }

    override fun getAllPurchases(): List<Purchase> {
        return database.purchaseDAO().getPurchases()
    }

    override fun insertPurchase(purchase: Purchase) {
        database.purchaseDAO().insertPurchase(purchase)
    }
}