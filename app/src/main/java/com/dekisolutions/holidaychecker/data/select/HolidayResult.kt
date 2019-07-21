package com.dekisolutions.holidaychecker.data.select

data class HolidayResult(
    val myHolidayName: String,
    val partnerHolidayName: String,
    val startDate: String,
    var endDate: String
)

