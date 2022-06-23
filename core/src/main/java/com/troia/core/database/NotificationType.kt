package com.troia.core.database

enum class NotificationType(value: Int) {
    ProductsLoad(0),
    PurchasesLoad(1),
    QuantityLoad(2),
    EmailVerification(3),
    UserData(4)
}