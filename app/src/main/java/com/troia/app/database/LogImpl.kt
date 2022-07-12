package com.troia.app.database

import android.content.Context
import android.os.Environment
import android.util.Log
import com.troia.core.database.LogAdapter
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class LogImpl(var appconcontext: Context): LogAdapter {

    override fun log(text: String?) {
        val logFile = File(appconcontext.getExternalFilesDir("logs"), "log.txt")
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                Log.println(Log.ASSERT, "sdfs", e.toString())
            }
        }
        try {
            val buf = BufferedWriter(FileWriter(logFile, true))
            buf.append(text)
            buf.newLine()
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.println(Log.ASSERT, "sdfs", e.toString())
        }
    }
}