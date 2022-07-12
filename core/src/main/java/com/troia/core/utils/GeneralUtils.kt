package com.troia.core.utils

import com.troia.core.database.LogAdapter
import java.util.*

object GeneralUtils {
    const val DAYS_DIVIDER = 86400000
    lateinit var logger: LogAdapter

    fun daysBetween(before: Date, after: Date): UInt{
        val time = after.time - before.time
        return (time / DAYS_DIVIDER).toUInt()
    }

    fun initLogger(logger: LogAdapter) {
        this.logger = logger
    }

    fun log(text: String?) = logger.log(text)
}