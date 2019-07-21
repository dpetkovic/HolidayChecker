package com.dekisolutions.holidaychecker.di

import com.dekisolutions.holidaychecker.usecase.GetCountriesUseCase
import com.dekisolutions.holidaychecker.usecase.GetHolidaysUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetCountriesUseCase(get()) }

    factory { GetHolidaysUseCase(get()) }
}