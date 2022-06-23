package com.troia.core.database

interface DataObserver {
    fun notify(type: NotificationType, data: Any? = null)
}