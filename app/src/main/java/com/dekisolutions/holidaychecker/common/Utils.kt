package com.dekisolutions.holidaychecker.common

import java.util.*


object Utils {

    fun addDays(date: Date, days: Int): Date {
        val cal = Calendar.getInstance()
        cal.setTime(date)
        cal.add(Calendar.DATE, days) //minus number would decrement the days
        return cal.getTime()
    }
}