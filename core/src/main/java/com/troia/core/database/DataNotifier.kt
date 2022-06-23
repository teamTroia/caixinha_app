package com.troia.core.database

object DataNotifier {
    val listeners: ArrayList<DataObserver> = arrayListOf()

    fun subscribe(listener: DataObserver) {
        listeners.add(listener)
    }

    fun unsubscribe(listener: DataObserver){
        listeners.remove(listener)
    }

    fun notifyData(type: NotificationType, data: Any? = null) {
        listeners.forEach {
            it.notify(type, data)
        }
    }
}