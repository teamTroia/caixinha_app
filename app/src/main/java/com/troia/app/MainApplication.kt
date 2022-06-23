package com.troia.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.troia.app.database.DatabaseAccess
import com.troia.app.database.FirebaseAccess
import com.troia.core.utils.FirebaseUtils
import com.troia.core.utils.PreferencesManager

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesManager.initialize(applicationContext)
        DatabaseAccess.initialize(applicationContext)
        FirebaseUtils.init(FirebaseAccess(applicationContext))
    }
}