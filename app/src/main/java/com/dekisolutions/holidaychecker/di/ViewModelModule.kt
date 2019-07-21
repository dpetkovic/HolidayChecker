package com.dekisolutions.holidaychecker.di

import com.dekisolutions.holidaychecker.ui.HolidayCheckerViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HolidayCheckerViewModel(get(), get(), get()) }
}