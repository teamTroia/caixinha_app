package com.troia.core.types

import com.troia.core.utils.GeneralUtils
import java.io.Serializable

class UserProduct(): Serializable {
    var name: String = ""
    var quantity: Int = 0
    var price: Double = 0.0

    fun getTotal(): Double {
        return price.times(quantity)
    }

    fun increaseQuantity(value: Int) {
        if(quantity + value >= 0)
            quantity += value
        GeneralUtils.log("PRODUCT - increased $value in $name to ${quantity}un")
    }
}