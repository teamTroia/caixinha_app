package com.troia.app

import android.app.Application
import com.troia.app.database.FirebaseAccess
import com.troia.core.utils.FirebaseUtils
import com.troia.core.utils.PreferencesManager

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesManager.initialize(applicationContext)
        FirebaseUtils.init(FirebaseAccess(applicationContext))
        FirebaseUtils.getAllProducts()
    }
}