package com.dekisolutions.holidaychecker.di

import com.dekisolutions.holidaychecker.data.CountryRepository
import com.dekisolutions.holidaychecker.data.HolidayRepository
import com.dekisolutions.holidaychecker.network.createHolidayApi
import org.koin.dsl.module

val dataModule = module {
    single { createHolidayApi() }

    single { HolidayRepository(get()) }

    single { CountryRepository(get()) }
}