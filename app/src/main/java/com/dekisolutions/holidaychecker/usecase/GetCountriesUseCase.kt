package com.dekisolutions.holidaychecker.usecase

import com.dekisolutions.holidaychecker.data.CountryRepository
import com.dekisolutions.holidaychecker.network.response.Country
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class GetCountriesUseCase(private val countryRepository: CountryRepository) {

    fun execute() : Single<List<Country>> {
        return countryRepository.getCountries().subscribeOn(Schedulers.io()).map {
            it.countries
        }
    }
}