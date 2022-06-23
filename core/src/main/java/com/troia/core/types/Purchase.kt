package com.troia.core.types

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "purchases_history")
class Purchase(
    @ColumnInfo(name = "date")
    var date: Date,
    @ColumnInfo(name = "value")
    var value: Double
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}