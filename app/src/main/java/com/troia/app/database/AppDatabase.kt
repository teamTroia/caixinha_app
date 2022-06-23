package com.troia.app.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.troia.core.types.Purchase

@Database(
    version = 3,
    entities = [Purchase::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract  fun purchaseDAO(): PurchasesDAO
}