package com.troia.core.utils

import java.util.*

object DateUtils {
    const val DAYS_DIVIDER = 86400000

    fun daysBetween(before: Date, after: Date): UInt{
        val time = after.time - before.time
        return (time / DAYS_DIVIDER).toUInt()
    }
}