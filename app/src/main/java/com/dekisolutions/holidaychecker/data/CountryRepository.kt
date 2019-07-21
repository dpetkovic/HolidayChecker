package com.dekisolutions.holidaychecker.data

import com.dekisolutions.holidaychecker.BuildConfig
import com.dekisolutions.holidaychecker.network.HolidayApi
import com.dekisolutions.holidaychecker.network.response.CountriesResponse
import io.reactivex.Single

class CountryRepository(
    private val api: HolidayApi
) {

    fun getCountries(): Single<CountriesResponse> {
        return api.getCountries(BuildConfig.API_KEY)
    }
}