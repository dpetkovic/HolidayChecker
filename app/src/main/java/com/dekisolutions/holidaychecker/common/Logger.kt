package com.dekisolutions.holidaychecker.common

import android.util.Log

object Logger {

    val tag = "Holiday"

    fun d(message: String) {
        Log.d(tag, message)
    }
}