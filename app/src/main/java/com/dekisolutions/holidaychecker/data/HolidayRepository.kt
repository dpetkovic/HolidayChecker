package com.dekisolutions.holidaychecker.data

import com.dekisolutions.holidaychecker.BuildConfig
import com.dekisolutions.holidaychecker.network.HolidayApi
import com.dekisolutions.holidaychecker.network.response.HolidaysResponse
import io.reactivex.Single

class HolidayRepository(
    private val api: HolidayApi
) {

    fun getHolidays(country: String): Single<HolidaysResponse> {
        return api.getHolidays(BuildConfig.API_KEY, country)
    }
}