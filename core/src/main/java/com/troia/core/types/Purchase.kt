package com.troia.core.types

import java.util.*
import kotlin.math.abs


class Purchase(){
    var date: Date? = Date()
    var value: Double = 0.0
    var debt: Double = 0.0
    var log: String = ""

    fun debbit(value: Double): Double {
        debt -= value
        if (debt <= 0) {
            val r = abs(debt)
            debt = 0.0
            return r
        }
        return 0.0
    }
}